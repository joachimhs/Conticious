package no.haagensoftware.contentice.plugin.assembler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
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
                SubcategoryField subField = new SubcategoryField();
                subField.setId(categoryData.getId() + "_" + cf.getName());
                subField.setRequired(cf.getRequired());
                subField.setType(cf.getType());
                subField.setName(cf.getName());
                if (subCategoryData.getKeyMap().get(cf.getName()) != null) {
                    subField.setValue(subCategoryData.getKeyMap().get(cf.getName()).getAsString());
                }

                subcategoryFieldArray.add(new Gson().toJsonTree(subField));
            }

            subCategoryObject.add("fields", subcategoryFieldArray);
        }

        return subCategoryObject;
    }


}
