package no.haagensoftware.contentice.plugin.assembler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.sun.tools.javac.util.List;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/22/13
 * Time: 5:55 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoryAssembler {

    public static JsonObject buildAdminCategoryJson(CategoryData categoryData) {
        JsonObject categoryObject = new JsonObject();
        categoryObject.addProperty("id", categoryData.getId());

        JsonArray subCategoriesArray = new JsonArray();
        for (SubCategoryData subCategoryData : categoryData.getSubCategories()) {
            subCategoriesArray.add(new JsonPrimitive(subCategoryData.getId()));
        }
        categoryObject.add("subcategories", subCategoriesArray);

        JsonArray defaultFields = new JsonArray();
        for (CategoryField field : categoryData.getDefaultFields()) {
            defaultFields.add(new JsonPrimitive(field.getId()));
        }

        categoryObject.add("defaultFields", defaultFields);
        return categoryObject;
    }
}
