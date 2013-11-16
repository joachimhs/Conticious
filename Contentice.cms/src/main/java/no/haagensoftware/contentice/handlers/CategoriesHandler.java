package no.haagensoftware.contentice.handlers;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.CategoryData;

import java.util.List;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 1:28 PM
 * To change this template use File | Settings | File Templates.
 */
public class CategoriesHandler extends ContenticeGenericHandler {
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
            JsonObject categoryObject = new JsonObject();
            categoryObject.addProperty("id", category.getId());
            categoryArray.add(categoryObject);
        }

        logger.info("Writing contents to buffer");
        writeContentsToBuffer(channelHandlerContext, categoryArray.toString(), "application/json; charset=UTF-8");

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
