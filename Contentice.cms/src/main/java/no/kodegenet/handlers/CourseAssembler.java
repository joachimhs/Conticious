package no.kodegenet.handlers;

import com.google.gson.JsonObject;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/26/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class CourseAssembler {

    public static JsonObject buildPageJsonFromSubCategoryData(SubCategoryData subCategoryData) {
        JsonObject subCategoryObject = new JsonObject();
        if (subCategoryData != null) {
            subCategoryObject.addProperty("id", subCategoryData.getId());
            subCategoryObject.addProperty("name", subCategoryData.getName());
            subCategoryObject.addProperty("content", subCategoryData.getContent());

            for (String key : subCategoryData.getKeyMap().keySet()) {
                subCategoryObject.add(key, subCategoryData.getKeyMap().get(key));
            }
        }

        return subCategoryObject;
    }
}