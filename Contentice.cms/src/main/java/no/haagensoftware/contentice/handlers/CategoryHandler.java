package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

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

        String category = null;

        logger.info("parameterMap: " + getParameterMap());
        if (getParameterMap() != null) {
            category = getParameterMap().get("category");
        }



        writeContentsToBuffer(channelHandlerContext, "Channel CategoryHandler Response: " + category, "text/plain; charset=UTF-8");

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
