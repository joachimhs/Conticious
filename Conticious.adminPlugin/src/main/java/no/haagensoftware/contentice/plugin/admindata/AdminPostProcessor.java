package no.haagensoftware.contentice.plugin.admindata;

import com.google.gson.annotations.Expose;
import no.haagensoftware.hyrrokkin.annotations.SerializedClassName;

/**
 * Created by jhsmbp on 12/09/15.
 */
@SerializedClassName("postProcessor")
public class AdminPostProcessor {
    @Expose private String id;
    @Expose private String name;

    public AdminPostProcessor(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public AdminPostProcessor() {
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
}
