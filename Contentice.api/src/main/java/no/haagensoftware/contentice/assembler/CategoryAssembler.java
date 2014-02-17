package no.haagensoftware.contentice.assembler;

import com.google.gson.*;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 10:17
 * To change this template use File | Settings | File Templates.
 */
public class CategoryAssembler {
    public static JsonObject buildCategoryJsonFromCategoryData(CategoryData categoryData) {
        JsonObject categoryObject = new JsonObject();
        categoryObject.addProperty("id", categoryData.getId());

        JsonArray subCategories = new JsonArray();
        for (SubCategoryData subCategoryData : categoryData.getSubcategories()) {
            subCategories.add(new JsonPrimitive(subCategoryData.getId()));
        }
        categoryObject.add("subcategories", subCategories);

        return categoryObject;
    }
}
