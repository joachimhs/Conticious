package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.ContenticeParameterMap;
import no.haagensoftware.contentice.plugin.assembler.AdminCategoryAssembler;
import no.haagensoftware.contentice.plugin.assembler.AdminSubCategoryAssembler;
import org.apache.log4j.Logger;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoryHandler extends ContenticeHandler  {
    private static final Logger logger = Logger.getLogger(AdminCategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading CategoryHandler and writing contents to buffer");

        String category = getParameter("category");
        CategoryData categoryData = getStorage().getCategory(category);

        if (isPost(fullHttpRequest)) {
            logger.info("POSTING CATEGORY: " + category);

        } else if (isPut(fullHttpRequest) && category != null) {
            logger.info("PUTTING CATEGORY: " + category);
            logger.info(getHttpMessageContent(fullHttpRequest));

        } else if (isGet(fullHttpRequest)) {
            if (categoryData == null) {
                write404ToBuffer(channelHandlerContext);
            } else {
                JsonObject topLevelObject = new JsonObject();


                JsonArray subCategoriesArray = new JsonArray();
                JsonArray defaultFieldsArray = new JsonArray();

                for (SubCategoryData subcategoryData : getStorage().getSubCategories(categoryData.getId())) {
                    categoryData.addSubCategory(subcategoryData);
                    subCategoriesArray.add(AdminSubCategoryAssembler.buildAdminJsonFromSubCategoryData(subcategoryData, categoryData.getId()));
                }

                topLevelObject.add("category", AdminCategoryAssembler.buildAdminCategoryJson(categoryData));

                for (CategoryField field : categoryData.getDefaultFields()) {
                    defaultFieldsArray.add(new Gson().toJsonTree(field));
                }

                topLevelObject.add("subcategories", subCategoriesArray);
                topLevelObject.add("categoryFields", defaultFieldsArray);
                writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json; charset=UTF-8");
            }
        }
    }
}
