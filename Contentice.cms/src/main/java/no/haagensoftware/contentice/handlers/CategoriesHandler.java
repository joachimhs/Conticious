package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;

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
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        logger.info("reading channel and writing contents to buffer");
        writeContentsToBuffer(ctx, "Channel Handler Response", "text/plain; charset=UTF-8");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("reading channel and writing contents to buffer");
        writeContentsToBuffer(channelHandlerContext, "Channel Handler Response", "text/plain; charset=UTF-8");
    }
}
