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
public class AdminSubcategoryFieldsHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminSubcategoryFieldsHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("Post/Put SubcategoryField");

        String returnJson = "";


        if (isPost(fullHttpRequest) || isPut(fullHttpRequest)) {
            String messageContent = fullHttpRequest.content().toString(CharsetUtil.UTF_8);
            logger.info(messageContent);
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
