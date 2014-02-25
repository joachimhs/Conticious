package no.haagensoftware.contentice.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 15.11.13
 * Time: 12:13
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoryData {
    private String id;
    private String name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public List<String> getListForKey(String key) {
        List<String> retList = new ArrayList<>();

        JsonElement element = getKeyMap().get(key);

        if (element != null && element.isJsonArray()) {
            JsonArray jsonArray = element.getAsJsonArray();

            for (JsonElement jsonElement : jsonArray) {
                retList.add(jsonElement.getAsString());
            }
        }

        return retList;
    }

    public List<String> getListForKey(String key, String delimeter) {
        List<String> retList = new ArrayList<>();

        String value = getValueForKey(key);

        if (value.contains(delimeter)) {
            String[] values = value.split(delimeter);
            for (String val : values) {
                retList.add(val);
            }
        } else {
            retList.add(value);
        }

        return retList;
    }

    public String getValueForKey(String key) {
        String value = null;

        JsonElement element = getKeyMap().get(key);

        if (element != null) {
            value = element.getAsString();
        }

        return value;
    }

    public Long getLongValueForKey(String key) {
        String valueString = getValueForKey(key);
        Long value = null;

        try {
            value = Long.parseLong(valueString);
        } catch (NumberFormatException nfe) {
            //Nothing to do, really
        }

        return value;
    }

    public Date getDateForKey(String key) {
        String dateString = getValueForKey(key);

        Date date = null;

        try {
            if (dateString != null && dateString.length() == 10 ) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                date = sdf.parse(dateString);
            } else if (dateString != null && dateString.length() == 16 ) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
                date = sdf.parse(dateString);
            }
        } catch (ParseException pe) {
            pe.printStackTrace();
        }

        return date;
    }
}
