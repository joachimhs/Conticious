package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeGenericHandler;

import java.util.Map;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 20:22
 * To change this template use File | Settings | File Templates.
 */
public class SubCategoryHandler extends ContenticeGenericHandler {
    private static final Logger logger = Logger.getLogger(CategoriesHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading SubCategoryHandler and writing contents to buffer");
        writeContentsToBuffer(channelHandlerContext, "Channel SubCategoryHandler Response", "text/plain; charset=UTF-8");

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
