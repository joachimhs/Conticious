package no.haagensoftware.contentice.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoriesHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(CategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        handleIncomingRequest(channelHandlerContext, fullHttpRequest);
    }

    public void handleIncomingRequest(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading CategoriesHandler and writing contents to buffer");

        List<CategoryData> categories = getStorage().getCategories();

        logger.info("Got " + categories.size() + " categories");

        JsonArray categoryArray = new JsonArray();
        for (CategoryData category : categories) {
            List<SubCategoryData> subcategories = getStorage().getSubCategories(category.getId());
            category.getSubcategories().addAll(subcategories);
            categoryArray.add(CategoryAssembler.buildCategoryJsonFromCategoryData(category));
        }

        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("categories", categoryArray);

        writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
    }
}
