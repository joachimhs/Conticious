package no.haagensoftware.contentice.data;

import com.google.gson.JsonElement;

import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 15.11.13
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoryData {
    private String id;
    private String content;
    private Map<String, JsonElement> keyMap;

    public SubCategoryData() {
        keyMap = new HashMap<>();
    }

    public SubCategoryData(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Map<String, JsonElement> getKeyMap() {
        return keyMap;
    }

    public void setKeyMap(Map<String, JsonElement> keyMap) {
        this.keyMap = keyMap;
    }
}
