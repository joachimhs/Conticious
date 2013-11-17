package no.haagensoftware.contentice.handlers;

import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 20:19
 * To change this template use File | Settings | File Templates.
 */
public class CategoryHandler extends ContenticeGenericHandler {
    private static final Logger logger = Logger.getLogger(CategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading CategoryHandler and writing contents to buffer");

        String category = getParameter("category");

        CategoryData categoryData = getStorage().getCategory(category);
        if (categoryData == null) {
            write404ToBuffer(channelHandlerContext);
        } else {
            JsonObject topLevelObject = new JsonObject();
            topLevelObject.add("category", CategoryAssembler.buildCategoryJsonFromCategoryData(categoryData));

            writeContentsToBuffer(channelHandlerContext, topLevelObject.toString(), "application/json; charset=UTF-8");
        }

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
