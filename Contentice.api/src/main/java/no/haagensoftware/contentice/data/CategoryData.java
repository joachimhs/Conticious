package no.haagensoftware.contentice.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 15.11.13
 * Time: 12:11
 * To change this template use File | Settings | File Templates.
 */
public class CategoryData {
    public String id;
    public List<SubCategoryData> subCategories = new ArrayList<>();

    public CategoryData() {

    }

    public CategoryData(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<SubCategoryData> getSubCategories() {
        return subCategories;
    }

    public void addSubCategory(SubCategoryData subCategoryData) {
        subCategories.add(subCategoryData);
    }
}
