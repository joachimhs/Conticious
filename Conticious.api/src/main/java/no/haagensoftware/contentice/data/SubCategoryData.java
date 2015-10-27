package no.haagensoftware.contentice.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.annotations.Expose;
import no.haagensoftware.hyrrokkin.annotations.SerializedClassName;

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
@SerializedClassName("subcategory")
public class SubCategoryData {
    @Expose private String id;
    @Expose private String name;
    @Expose private String content;
    private Map<String, JsonElement> keyMap;

    public SubCategoryData() {
        keyMap = new HashMap<>();
    }

    public SubCategoryData(String id) {
        this();
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

    public void setListForKey(String key, List<String> values) {
        JsonArray jsonArray = new JsonArray();

        for (String value : values) {
            jsonArray.add(new JsonPrimitive(value));
        }

        getKeyMap().put(key, jsonArray);
    }

    public List<String> getListForKey(String key, String delimeter) {
        List<String> retList = new ArrayList<>();

        if (keyIsJsonArray(key)) {
            JsonArray array = getJsonArrayForKey(key);
            for (JsonElement elem : array) {
                if (elem.isJsonPrimitive()) {
                    retList.add(elem.getAsString());
                }
            }
        } else {
            String value = getValueForKey(key);

            if (value != null && value.contains(delimeter)) {
                String[] values = value.split(delimeter);
                for (String val : values) {
                    retList.add(val);
                }
            } else {
                retList.add(value);
            }
        }

        return retList;
    }

    private boolean keyIsJsonArray(String key) {
        JsonElement element = getKeyMap().get(key);
        return element != null && element.isJsonArray();
    }

    private JsonArray getJsonArrayForKey(String key) {
        JsonElement element = getKeyMap().get(key);
        if (element != null && element.isJsonArray()) {
            return element.getAsJsonArray();
        }

        return null;
    }

    public String getValueForKey(String key) {
        String value = null;

        JsonElement element = getKeyMap().get(key);

        if (element != null) {
            value = element.getAsString();
        }

        return value;
    }

    public void setValueForKey(String key, String value) {
        if (value != null) {
            getKeyMap().put(key, new JsonPrimitive(value));
        }
    }

    public void setValueForKey(String key, Integer value) {
        if (value != null) {
            getKeyMap().put(key, new JsonPrimitive(value));
        }
    }

    public void setValueForKey(String key, Long value) {
        if (value != null) {
            getKeyMap().put(key, new JsonPrimitive(value));
        }
    }

    public void setValueForKey(String key, Double value) {
        if (value != null) {
            getKeyMap().put(key, new JsonPrimitive(value));
        }
    }

    public void setValueForKey(String key, Boolean value) {
        if (value != null) {
            getKeyMap().put(key, new JsonPrimitive(value.booleanValue()));
        }
    }

    public Integer getIntegerValueForKey(String key) {
        String valueString = getValueForKey(key);
        Integer value = null;

        try {
            value = Integer.parseInt(valueString);
        } catch (NumberFormatException nfe) {
            //Nothing to do, really
        } catch (NullPointerException npe) {

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
        } catch (NullPointerException npe) {

        }

        return value;
    }

    public Double getDoubleValueForKey(String key) {
        String valueString = getValueForKey(key);
        Double value = null;

        try {
            value = Double.parseDouble(valueString);
        } catch (NumberFormatException nfe) {
            //Nothing to do, really
        } catch (NullPointerException npe) {

        }

        return value;
    }

    public Boolean getBooleanValueForKey(String key) {
        String valueString = getValueForKey(key);
        Boolean value = null;

        try {
            value = Boolean.parseBoolean(valueString);
        } catch (NumberFormatException nfe) {
            //Nothing to do, really
        } catch (NullPointerException npe) {

        }

        if (value == null) {
            value = new Boolean(false);
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
        } catch (NullPointerException npe) {

        }

        return date;
    }
}
