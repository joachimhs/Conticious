package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.CategoryField;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 5:53 PM
 * To change this template use File | Settings | File Templates.
 */
public class AdminCategoriesHandler extends ContenticeHandler {
    private Logger logger = Logger.getLogger(AdminCategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading CategoriesHandler and writing contents to buffer");

        List<CategoryData> categories = getStorage().getCategories();

        logger.info("Got " + categories.size() + " categories");

        JsonArray categoryArray = new JsonArray();
        JsonArray subCategoryArray = new JsonArray();
        JsonArray categoryFields = new JsonArray();

        for (CategoryData category : categories) {
            for (SubCategoryData subcategoryData : getStorage().getSubCategories(category.getId())) {
                category.addSubCategory(subcategoryData);
                subCategoryArray.add(SubCategoryAssembler.buildJsonFromSubCategoryData(subcategoryData, category.getId()));
            }

            for (CategoryField field : category.getDefaultFields()) {
                categoryFields.add(new Gson().toJsonTree(field));
            }
            categoryArray.add(CategoryAssembler.buildCategoryJsonFromCategoryData(category));
        }

        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("categories", categoryArray);
        topLevelObject.add("subcategories", subCategoryArray);
        topLevelObject.add("categoryFields", categoryFields);

        writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json; charset=UTF-8");

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
