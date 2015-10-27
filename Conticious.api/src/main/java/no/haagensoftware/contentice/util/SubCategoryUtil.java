package no.haagensoftware.contentice.util;

import com.google.gson.*;
import no.haagensoftware.contentice.data.SubCategoryData;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by jhsmbp on 05/04/15.
 */
public class SubCategoryUtil {
    private static final Logger logger = Logger.getLogger(SubCategoryUtil.class.getName());

    public static SubCategoryData convertObjectToSubCateogory(Object input) {
        SubCategoryData newSubcategory = new SubCategoryData();

        JsonParser parser = new JsonParser();
        JsonObject topLevelObject = parser.parse(new Gson().toJson(input)).getAsJsonObject();
        for (Map.Entry<String, JsonElement> entry : topLevelObject.entrySet()) {
            
            if (entry.getKey().equals("name") && !entry.getValue().isJsonNull()) {
                newSubcategory.setName(entry.getValue().getAsString());
                newSubcategory.getKeyMap().put(entry.getKey(), entry.getValue());
            } else if (entry.getKey().equals("content") && !entry.getValue().isJsonNull()) {
                newSubcategory.setContent(entry.getValue().getAsString());
            } else if (entry.getKey().equals("id")  && !entry.getValue().isJsonNull()) {
                String newId = entry.getValue().getAsString();
                newSubcategory.setId(newId);
            } else if (!entry.getValue().isJsonNull() && entry.getValue().isJsonObject()) {
                JsonObject entryObject = entry.getValue().getAsJsonObject();

                newSubcategory.getKeyMap().put(entry.getKey(), entryObject.get("id"));
            } else if (!entry.getValue().isJsonNull() && entry.getValue().isJsonArray()) {
                JsonArray ids = new JsonArray();
                for (JsonElement arrayElement : entry.getValue().getAsJsonArray()) {
                    if (arrayElement.isJsonObject() && arrayElement.getAsJsonObject().has("id")) {
                        ids.add(arrayElement.getAsJsonObject().get("id"));
                    } else if (arrayElement.isJsonPrimitive()) {
                        ids.add(arrayElement.getAsJsonPrimitive());
                    }
                }

                newSubcategory.getKeyMap().put(entry.getKey(), ids);
            } else if ((!entry.getValue().isJsonNull())) {
                newSubcategory.getKeyMap().put(entry.getKey(), entry.getValue());
            }
        }

        return newSubcategory;
    }

    public static JsonObject convertSubcategoryToJsonObject(SubCategoryData sd) {
        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", sd.getId());
        if (sd.getName() != null) {
            jsonObject.addProperty("name", sd.getName());
        }
        if (sd.getContent() != null) {
            jsonObject.addProperty("content", sd.getContent());
        }

        for (String key : sd.getKeyMap().keySet()) {
            jsonObject.add(key, sd.getKeyMap().get(key));
        }

        return jsonObject;
    }

    public static <T> T convertSubcategoryToObject(String cateogry, SubCategoryData sd, Class<T> clazz) {
        if (sd.getId() != null && sd.getId().startsWith(cateogry)) {
            sd.setId(sd.getId().substring(cateogry.length()+1));
        }
        return new Gson().fromJson(convertSubcategoryToJsonObject(sd), clazz);
    }
}
