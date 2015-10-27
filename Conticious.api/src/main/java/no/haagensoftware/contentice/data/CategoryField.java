package no.haagensoftware.contentice.data;

import com.google.gson.annotations.Expose;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/21/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryField {
    @Expose private String id;
    @Expose private String name;
    @Expose private String type;
    @Expose private boolean required;
    @Expose private String relation;

    public CategoryField() {
    }

    public CategoryField(String id, String name, String type, boolean required) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.required = required;
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

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }
}
