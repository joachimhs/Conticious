package no.haagensoftware.contentice.data;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 25.11.13
 * Time: 10:48
 * To change this template use File | Settings | File Templates.
 */
public class SubcategoryField {
    private String id;
    private String name;
    private String type;
    private boolean required;
    private String value;
    private String relation;
    private String category;
    private String subcategory;
    private List<String> addedRelations;

    public SubcategoryField() {
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public void setSubcategory(String subcategory) {
        this.subcategory = subcategory;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public List<String> getAddedRelations() {
        return addedRelations;
    }

    public void setAddedRelations(List<String> addedRelations) {
        this.addedRelations = addedRelations;
    }
}
