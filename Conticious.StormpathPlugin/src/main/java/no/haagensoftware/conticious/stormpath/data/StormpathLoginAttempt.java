package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 31/03/15.
 */
public class StormpathLoginAttempt {
    private String type;
    private String value;

    public StormpathLoginAttempt() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
