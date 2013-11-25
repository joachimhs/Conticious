package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.CharsetUtil;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.plugin.admindata.CategoryFieldObject;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/23/13
 * Time: 2:34 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoryFieldsHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminCategoryFieldsHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("Post/Put CategoryField");

        String returnJson = "";

        String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);

        if (isPost(fullHttpRequest)) {

            CategoryFieldObject categoryField = new Gson().fromJson(messageContent, CategoryFieldObject.class);

            if (categoryField != null && categoryField.getCategoryField() != null) {
                CategoryField newCategoryField = categoryField.getCategoryField();

                String category = newCategoryField.getId().substring(0, newCategoryField.getId().indexOf("_"));

                CategoryData categoryData = getStorage().getCategory(category);
                boolean updated = false;
                for (CategoryField cf : categoryData.getDefaultFields()) {
                    if (cf.getId().equals(categoryField.getCategoryField().getId())) {
                        cf.setName(newCategoryField.getName());
                        cf.setType(newCategoryField.getType());
                        cf.setRequired(newCategoryField.getRequired());
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
            }
        } else if (isPut(fullHttpRequest)) {
            String categoryFieldId = getParameter("categoryField");
            CategoryFieldObject categoryField = new Gson().fromJson(messageContent, CategoryFieldObject.class);

            String category = categoryFieldId.substring(0, categoryFieldId.indexOf("_"));
            String fieldId = categoryFieldId.substring(categoryFieldId.indexOf("_")+1, categoryFieldId.length());

            logger.info("Updating fieldid: " + categoryField.getCategoryField().getName() + " with: " + fieldId + " for category: " + category);

            CategoryField updatedField = null;
            CategoryData categoryData = getStorage().getCategory(category);
            for (CategoryField cf : categoryData.getDefaultFields()) {
                if (cf.getId().equals(categoryFieldId)) {
                    cf.setName(categoryField.getCategoryField().getName());
                    cf.setType(categoryField.getCategoryField().getType());
                    cf.setRequired(categoryField.getCategoryField().getRequired());
                    updatedField = cf;
                    break;
                }
            }

            getStorage().setCategory(category, categoryData);

            CategoryFieldObject cfObject = new CategoryFieldObject();
            cfObject.setCategoryField(updatedField);
            returnJson = new Gson().toJson(cfObject);
        } else if (isDelete(fullHttpRequest)) {
            String categoryFieldId = getParameter("categoryField");

            if (categoryFieldId.contains("_")) {
                String category = categoryFieldId.substring(0, categoryFieldId.indexOf("_"));
                String fieldId = categoryFieldId.substring(categoryFieldId.indexOf("_")+1, categoryFieldId.length());

                CategoryData categoryData = getStorage().getCategory(category);

                CategoryField fieldToDelete = null;
                for (CategoryField cf : categoryData.getDefaultFields()) {
                    if (cf.getName().equals(fieldId)) {
                        fieldToDelete = cf;
                        break;
                    }
                }

                if (fieldToDelete != null) {
                    categoryData.getDefaultFields().remove(fieldToDelete);
                }

                getStorage().setCategory(category, categoryData);
            }
        }

        writeContentsToBuffer(channelHandlerContext, returnJson, "application/json; charset=UTF-8");
    }
}
