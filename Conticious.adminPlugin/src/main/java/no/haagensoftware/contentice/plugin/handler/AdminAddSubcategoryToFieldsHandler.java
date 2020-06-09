package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.*;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/23/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminAddSubcategoryToFieldsHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminAddSubcategoryToFieldsHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("Post Add Subcategory to Field Handler");

        String returnJson = "";

        String subcategoryIdToAdd = getParameter("subcategoryToAdd");
        String subcategoryFieldId = getParameter("subcategoyFieldToAddTo");

        logger.info("subcategoryIdToAdd: " + subcategoryIdToAdd);
        logger.info("subcategoryFieldId: " + subcategoryFieldId);

        if (isPost(fullHttpRequest) && subcategoryIdToAdd != null && subcategoryFieldId != null) {
            //subchapters_arduino_ch1_koble_til

            //categoryFrom
            //subcategoryFrom
            //categoryTo
            //subcategoryTo
            //fieldTo

            //The category to add
            String categoryFromId = subcategoryIdToAdd.substring(0, subcategoryIdToAdd.indexOf("_"));
            String subcategoryFromId = subcategoryIdToAdd.substring(subcategoryIdToAdd.indexOf("_")+1);


            //The field to add to
            String categoryToId = subcategoryFieldId.substring(0, subcategoryFieldId.indexOf("_"));
            String fieldName = subcategoryFieldId.substring(subcategoryFieldId.indexOf("_")+1, subcategoryFieldId.length());
            String subcategoryToId = fieldName.substring(0, fieldName.lastIndexOf("_")).replaceAll("\\%20", " ");
            fieldName = fieldName.substring(fieldName.lastIndexOf("_")+1, fieldName.length());

            logger.info("categoryFrom: " + categoryFromId);
            logger.info("subcategoryFrom: " + subcategoryFromId);
            logger.info("categoryToId: " + categoryToId);
            logger.info("subcategoryToId: " + subcategoryToId);
            logger.info("fieldName: " + fieldName);

            SubCategoryData subCategoryDataToAdd = getStorage().getSubCategory(getDomain().getDocumentsName(), categoryFromId, subcategoryFromId);
            SubCategoryData subCategoryDataToAddTo = getStorage().getSubCategory(getDomain().getDocumentsName(), categoryToId, subcategoryToId);

            if (subCategoryDataToAdd != null && subCategoryDataToAddTo != null) {
                JsonElement fieldElement = subCategoryDataToAddTo.getKeyMap().get(fieldName);
                if (fieldElement == null) {
                    fieldElement = new JsonArray();
                    subCategoryDataToAddTo.getKeyMap().put(fieldName, fieldElement);
                }

                if (fieldElement.isJsonArray()) {
                    fieldElement.getAsJsonArray().add(new JsonPrimitive(subcategoryIdToAdd));

                    getStorage().setSubCategory(getDomain().getDocumentsName(), categoryToId, subcategoryToId, subCategoryDataToAddTo);
                }
            }

            JsonObject topLevelObject = new JsonObject();
            topLevelObject.addProperty("subcategory", new Gson().toJson(subCategoryDataToAddTo));

            returnJson = topLevelObject.toString();
        }


        writeContentsToBuffer(channelHandlerContext, returnJson, "application/json");
    }
}
