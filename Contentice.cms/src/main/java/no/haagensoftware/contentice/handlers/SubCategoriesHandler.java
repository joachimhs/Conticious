package no.haagensoftware.contentice.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 20:21
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoriesHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(CategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading SubCategoriesHandler and writing contents to buffer");

        String category = getParameter("category");

        List<SubCategoryData> subCategories = getStorage().getSubCategories(category);

        if (subCategories == null) {
            write404ToBuffer(channelHandlerContext);
        } else {
            JsonArray subCategoryArray = new JsonArray();
            for (SubCategoryData subCategory : subCategories) {
                subCategoryArray.add(SubCategoryAssembler.buildJsonFromSubCategoryData(subCategory, category));
            }

            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("subcategories", subCategoryArray);

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        }
    }
}
