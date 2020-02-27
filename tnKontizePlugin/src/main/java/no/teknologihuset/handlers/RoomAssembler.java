package no.teknologihuset.handlers;

import com.google.gson.JsonObject;
import no.haagensoftware.contentice.data.SubCategoryData;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/26/13
 * Time: 12:30 PM
 * To change this template use File | Settings | File Templates.
 */
public class RoomAssembler {

    public static JsonObject buildRoomJsonFromSubCategoryData(SubCategoryData subCategoryData) {
        JsonObject subCategoryObject = new JsonObject();
        if (subCategoryData != null) {
            String id = subCategoryData.getId();
            if (id != null && id.startsWith("rooms_")) {
                id = id.substring(6);
            }
            subCategoryObject.addProperty("id", id);
            subCategoryObject.addProperty("name", subCategoryData.getName());
            subCategoryObject.addProperty("content", subCategoryData.getContent());

            for (String key : subCategoryData.getKeyMap().keySet()) {
                subCategoryObject.add(key, subCategoryData.getKeyMap().get(key));
            }
        }

        return subCategoryObject;
    }
}
