package no.haagensoftware.contentice.assembler;

import com.google.gson.JsonObject;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 10:08
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoryAssembler {

    public static JsonObject buildJsonFromSubCategoryData(SubCategoryData subCategoryData, String category) {
        JsonObject subCategoryObject = new JsonObject();
        if (subCategoryData != null) {
            subCategoryObject.addProperty("id", subCategoryData.getId());
            subCategoryObject.addProperty("category", category);
            subCategoryObject.addProperty("content", subCategoryData.getContent());

            for (String key : subCategoryData.getKeyMap().keySet()) {
                subCategoryObject.add(key, subCategoryData.getKeyMap().get(key));
            }
        }

        return subCategoryObject;
    }
}
