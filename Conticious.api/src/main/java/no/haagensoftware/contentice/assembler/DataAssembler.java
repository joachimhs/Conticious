package no.haagensoftware.contentice.assembler;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
                subCategoryObject.add(key, subCategoryData.getKeyMap().get(key));
            }
        }

        return subCategoryObject;
    }
}
