package no.haagensoftware.conticious.plugin.storage.data;

import com.datastax.driver.mapping.annotations.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by joachimhaagenskeie on 19/02/2017.
 */
@Table(keyspace = "conticious", name ="categories")
public class CassandraCategoryData {
    @PartitionKey private String name;
    @ClusteringColumn private String host;
    private String fieldsjson;
    private boolean categoryIsPublic;
    private List<String> subcategories;

    public CassandraCategoryData() {
        subcategories = new ArrayList<>();
    }

    public CassandraCategoryData(CategoryData categoryData, String host) {
        this();

        if (categoryData != null) {
            this.name = categoryData.getId();
            this.host = host;
            this.fieldsjson = JsonUtil.convertCategoryFieldsToJsonString(categoryData.getDefaultFields());
            this.categoryIsPublic = categoryData.isPublic();
            for (SubCategoryData scd : categoryData.getSubcategories()) {
                this.subcategories.add(scd.getId());
            }
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getFieldsjson() {
        return fieldsjson;
    }

    public void setFieldsjson(String fieldsjson) {
        this.fieldsjson = fieldsjson;
    }

    public boolean isCategoryIsPublic() {
        return categoryIsPublic;
    }

    public void setCategoryIsPublic(boolean categoryIsPublic) {
        this.categoryIsPublic = categoryIsPublic;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    @Transient
    public CategoryData toCategoryData() {
        CategoryData cd = new CategoryData();
        cd.setId(this.getName());
        cd.setNumberOfSubcategories(this.getSubcategories() != null ? this.getSubcategories().size() : 0);
        cd.setPublic(this.isCategoryIsPublic());

        JsonElement jsonElement = new JsonParser().parse(this.getFieldsjson());
        if (jsonElement.isJsonArray()) {
            cd.setDefaultFields(JsonUtil.convertJsonToDefaultFields(jsonElement, cd.getId()));
        }

        return cd;
    }
}
