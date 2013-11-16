package no.haagensoftware.contentice.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpResponse;

import java.util.logging.Logger;

import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 19:45
 * To change this template use File | Settings | File Templates.
 */
public class NotFoundHandler extends ContenticeGenericHandler {
    private static final Logger logger = Logger.getLogger(NotFoundHandler.class.getName());

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        write404ToBuffer(channelHandlerContext);

        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }
}
