package no.haagensoftware.contentice.data;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/21/13
 * Time: 5:37 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoryField {
    private String id;
    private String name;
    private String type;
    private boolean required;

    public CategoryField() {
    }

    public CategoryField(String id, String name, String type) {
        this.id = id;
        this.name = name;
        this.type = type;
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
}
