package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class NotFoundHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(NotFoundHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        write404ToBuffer(channelHandlerContext);

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
