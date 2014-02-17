package no.haagensoftware.contentice.plugin.admindata;

import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 17/02/14.
 */
public class AdminCategory {
    private String id;
    private List<String> subcategories = new ArrayList<>();
    private List<String> defaultFields = new ArrayList<>();
    private boolean isPublic;

    public AdminCategory() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getSubcategories() {
        return subcategories;
    }

    public void setSubcategories(List<String> subcategories) {
        this.subcategories = subcategories;
    }

    public List<String> getDefaultFields() {
        return defaultFields;
    }

    public void setDefaultFields(List<String> defaultFields) {
        this.defaultFields = defaultFields;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean isPublic) {
        this.isPublic = isPublic;
    }
}
