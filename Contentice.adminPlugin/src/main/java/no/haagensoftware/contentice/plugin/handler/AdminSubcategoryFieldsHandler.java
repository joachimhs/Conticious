package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.CategoryFieldObject;
import no.haagensoftware.contentice.plugin.admindata.SubcategoryFieldObject;
import org.apache.log4j.Logger;
import sun.org.mozilla.javascript.internal.json.JsonParser;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/23/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminSubcategoryFieldsHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminSubcategoryFieldsHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("Post/Put SubcategoryField");

        String returnJson = "";

        String subcategoryFieldId = getParameter("subcategoryField");

        if (isPut(fullHttpRequest) && subcategoryFieldId != null) {
            String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            logger.info(messageContent);

            SubcategoryFieldObject subcategoryFieldObject = new Gson().fromJson(messageContent, SubcategoryFieldObject.class);

            String category = subcategoryFieldId.substring(0, subcategoryFieldId.indexOf("_"));
            String fieldName = subcategoryFieldId.substring(subcategoryFieldId.indexOf("_")+1, subcategoryFieldId.length());
            String subcategory = fieldName.substring(0, fieldName.indexOf("_"));
            fieldName = fieldName.substring(fieldName.indexOf("_")+1, fieldName.length());


            if (subcategoryFieldObject != null && subcategoryFieldId != null && subcategoryFieldId.contains("_")) {
                logger.info("category: " + category + " subcategory: " + subcategory + " fieldName: " + fieldName);

                SubCategoryData subCategoryData = getStorage().getSubCategory(category, subcategory);

                logger.info(new Gson().toJson(subCategoryData).toString());
                if (subCategoryData != null) {
                    if (subcategoryFieldObject.getSubcategoryField().getType().equals("textfield") || subcategoryFieldObject.getSubcategoryField().getType().equals("textarea")) {
                        subCategoryData.getKeyMap().put(subcategoryFieldObject.getSubcategoryField().getName(), new JsonPrimitive(subcategoryFieldObject.getSubcategoryField().getValue()));
                    } else if (subcategoryFieldObject.getSubcategoryField().getType().equals("array")) {
                        logger.info("array: " + subcategoryFieldObject.getSubcategoryField().getValue());
                        String value = subcategoryFieldObject.getSubcategoryField().getValue();
                        JsonElement jsonElement = new com.google.gson.JsonParser().parse(value);
                        if (jsonElement.isJsonArray()) {
                            subCategoryData.getKeyMap().put(subcategoryFieldObject.getSubcategoryField().getName(), jsonElement.getAsJsonArray());
                        }
                    } else if (subcategoryFieldObject.getSubcategoryField().getType().equals("boolean")) {
                        subCategoryData.getKeyMap().put(subcategoryFieldObject.getSubcategoryField().getName(), new JsonPrimitive(Boolean.parseBoolean(subcategoryFieldObject.getSubcategoryField().getValue())));
                    }

                    subcategoryFieldObject.getSubcategoryField().setId(category + "_" + subcategory + "_" + fieldName);
                }

                getStorage().setSubCategory(category, subcategory, subCategoryData);

                returnJson = new Gson().toJson(subcategoryFieldObject);
            }

            /*CategoryFieldObject categoryField = new Gson().fromJson(messageContent, CategoryFieldObject.class);

            if (categoryField != null && categoryField.getCategoryField() != null) {
                CategoryField newCategoryField = categoryField.getCategoryField();

                String category = newCategoryField.getId().substring(0, newCategoryField.getId().indexOf("_"));

                CategoryData categoryData = getStorage().getCategory(category);
                boolean updated = false;
                for (CategoryField cf : categoryData.getDefaultFields()) {
                    if (cf.getId().equals(categoryField.getCategoryField().getId())) {
                        cf.setName(newCategoryField.getName());
                        cf.setType(newCategoryField.getType());
                        updated = true;
                    }
                }

                if (!updated) {
                    categoryData.getDefaultFields().add(newCategoryField);
                    updated = true;
                }

                if (updated) {
                    getStorage().setCategory(category, categoryData);
                }

                categoryField = new CategoryFieldObject();
                categoryField.setCategoryField(newCategoryField);

                returnJson = new Gson().toJson(categoryField);
            }*/
        }

        writeContentsToBuffer(channelHandlerContext, returnJson, "application/json; charset=UTF-8");
    }
}
