package no.haagensoftware.contentice.assembler;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created by jhsmbp on 1/24/14.
 */
public class DataAssembler {

    public static JsonObject buildJsonFromSubCategoryData(String categoryName, boolean forceArray, String appendToId, SubCategoryData ... subCategoryDataList) {
        JsonObject topLevelObject = new JsonObject();
        if (subCategoryDataList.length > 1 || forceArray) {
            JsonArray subCategoryArray = new JsonArray();

            for (SubCategoryData subCategoryData : subCategoryDataList) {
                subCategoryArray.add(buildJson(subCategoryData, categoryName, appendToId));
            }

            topLevelObject.add(categoryName, subCategoryArray);
        } else if (subCategoryDataList.length == 1) {
            SubCategoryData subCategoryData = subCategoryDataList[0];
            topLevelObject.add(categoryName , buildJson(subCategoryData, categoryName, appendToId));
        } else if (subCategoryDataList == null || subCategoryDataList.length == 0) {
            topLevelObject.add(categoryName, new JsonArray());
        }

        return topLevelObject;
    }

    private static JsonObject buildJson(SubCategoryData subCategoryData, String category, String appendToId) {
        JsonObject subCategoryObject = new JsonObject();

        if (subCategoryData != null) {
            subCategoryObject.addProperty("id", appendToId + subCategoryData.getName());
            subCategoryObject.addProperty("name", subCategoryData.getName());
            subCategoryObject.addProperty("category", category);
            subCategoryObject.addProperty("content", subCategoryData.getContent());


            for (String key : subCategoryData.getKeyMap().keySet()) {
                JsonElement element = subCategoryData.getKeyMap().get(key);
                if (element.isJsonArray()) {
                    JsonArray newArray = new JsonArray();

                    JsonArray array = element.getAsJsonArray();
                    for (int index = 0; index < array.size(); index++) {
                        JsonElement currElement = array.get(index);
                        if (currElement.isJsonPrimitive() && currElement.getAsString().startsWith(key) && currElement.getAsString().contains("_")) {
                            String currVal = currElement.getAsString().substring(currElement.getAsString().indexOf("_") + 1);
                            newArray.add(new JsonPrimitive(currVal));
                        } else if (currElement.isJsonPrimitive() && currElement.getAsString().startsWith(category) && currElement.getAsString().contains("_")) {
                            String currVal = currElement.getAsString().substring(currElement.getAsString().indexOf("_") + 1);
                            newArray.add(new JsonPrimitive(currVal));
                        }
                    }

                    if (newArray.size() == 0) {
                        newArray = array;
                    }

                    subCategoryData.getKeyMap().put(key, newArray);
                } else if (element.isJsonPrimitive() && element.getAsString().startsWith(key) && element.getAsString().contains("_")) {
                    String currVal = element.getAsString().substring(element.getAsString().indexOf("_") + 1);
                    subCategoryData.getKeyMap().put(key, new JsonPrimitive(currVal));
                }

                subCategoryObject.add(key, subCategoryData.getKeyMap().get(key));
            }
        }

        return subCategoryObject;
    }
}
