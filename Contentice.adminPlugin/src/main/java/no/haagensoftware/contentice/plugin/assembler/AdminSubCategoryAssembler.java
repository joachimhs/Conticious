package no.haagensoftware.contentice.plugin.assembler;

import com.google.gson.JsonObject;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubCategoryAssembler {

    public static JsonObject buildAdminJsonFromSubCategoryData(SubCategoryData subCategoryData, String category) {
        JsonObject subCategoryObject = new JsonObject();
        if (subCategoryData != null) {
            subCategoryObject.addProperty("id", subCategoryData.getId());
            subCategoryObject.addProperty("name", subCategoryData.getName());

            JsonObject json = new JsonObject();
            json.addProperty("category", category);
            //json.addProperty("content", subCategoryData.getContent());
            for (String key : subCategoryData.getKeyMap().keySet()) {
                json.add(key, subCategoryData.getKeyMap().get(key));
            }

            subCategoryObject.add("json", json);
        }

        return subCategoryObject;
    }
}
