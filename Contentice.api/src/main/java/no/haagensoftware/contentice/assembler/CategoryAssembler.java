package no.haagensoftware.contentice.assembler;

import com.google.gson.JsonObject;
import no.haagensoftware.contentice.data.CategoryData;

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

        return categoryObject;
    }
}
