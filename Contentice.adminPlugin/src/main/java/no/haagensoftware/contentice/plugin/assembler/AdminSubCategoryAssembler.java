package no.haagensoftware.contentice.plugin.assembler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.SubcategoryField;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 8:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubCategoryAssembler {

    public static JsonObject buildAdminJsonFromSubCategoryData(SubCategoryData subCategoryData, CategoryData categoryData) {
        JsonObject subCategoryObject = new JsonObject();
        if (subCategoryData != null) {
            subCategoryObject.addProperty("id", subCategoryData.getId());
            subCategoryObject.addProperty("name", subCategoryData.getName());
            subCategoryObject.addProperty("content", subCategoryData.getContent());


            JsonArray subcategoryFieldArray= new JsonArray();

            for (CategoryField cf : categoryData.getDefaultFields()) {
                subcategoryFieldArray.add(new JsonPrimitive(subCategoryData.getId() + "_" + cf.getName()));
            }

            subCategoryObject.add("fields", subcategoryFieldArray);
        }

        return subCategoryObject;
    }


}
