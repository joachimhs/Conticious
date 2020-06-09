package no.haagensoftware.conticious.plugin.storage.data;

import com.datastax.driver.mapping.annotations.ClusteringColumn;
import com.datastax.driver.mapping.annotations.PartitionKey;
import com.datastax.driver.mapping.annotations.Table;
import com.datastax.driver.mapping.annotations.Transient;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.util.JsonUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by joachimhaagenskeie on 19/02/2017.
 */
@Table(keyspace = "conticious", name ="subcategories")
public class CassandraSubCategoryData {
    @PartitionKey private String id;
    @ClusteringColumn(value = 0) private String category;
    @ClusteringColumn(value = 1) private String host;

    private String name;
    private String fieldsjson;
    private String markdowncontent;

    private CassandraSubCategoryData() {

    }

    public CassandraSubCategoryData(String name, String category, String host) {
        this.id = name;
        this.name = name;
        this.category = category;
        this.host = host;
    }

    public CassandraSubCategoryData(SubCategoryData subCategoryData, String category, String host) {
        this();

        if (subCategoryData != null) {
            this.id = subCategoryData.getId();
            this.category = category;
            this.name = subCategoryData.getName();
            this.host = host;
            this.fieldsjson = JsonUtil.convertKeyMapToJson(subCategoryData.getKeyMap());
            this.markdowncontent = subCategoryData.getContent();
        }
    }

    @Transient
    public void setKeyMap(Map<String, JsonElement> keyMap) {
        this.fieldsjson = JsonUtil.convertKeyMapToJson(keyMap);
    }

    @Transient
    public Map<String, JsonElement> getKeyMap() {
        return JsonUtil.buildKeysMapFromJsonObject(this.getFieldsjson());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
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

    public String getMarkdowncontent() {
        return markdowncontent;
    }

    public void setMarkdowncontent(String markdowncontent) {
        this.markdowncontent = markdowncontent;
    }

    @Transient
    public SubCategoryData toSubCategoryData() {
        SubCategoryData scd = new SubCategoryData();
        scd.setId(this.getId());
        scd.setName(this.getName());
        scd.setKeyMap(JsonUtil.buildKeysMapFromJsonObject(this.getFieldsjson()));
        scd.setContent(this.getMarkdowncontent());

        return scd;
    }
}
