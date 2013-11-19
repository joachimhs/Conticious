package no.haagensoftware.contentice.plugin.handler;

import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.assembler.CategoryAssembler;
import no.haagensoftware.contentice.data.CategoryData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.ContenticeParameterMap;
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
