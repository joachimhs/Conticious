package no.haagensoftware.contentice.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.SubCategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 20:22
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoryHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(CategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading SubCategoryHandler and writing contents to buffer");

        String category = getParameter("category");
        String subcategory = getParameter("subcategory");

        SubCategoryData subCategoryData = getStorage().getSubCategory(getDomain().getWebappName(), category, subcategory);

        if (subCategoryData == null) {
            write404ToBuffer(channelHandlerContext);

        } else {
            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("subcategory", SubCategoryAssembler.buildJsonFromSubCategoryData(subCategoryData, category));

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json");
        }
    }
}
