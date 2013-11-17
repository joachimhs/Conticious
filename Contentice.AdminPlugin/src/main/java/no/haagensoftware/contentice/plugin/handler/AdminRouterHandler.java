package no.haagensoftware.contentice.plugin.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.ContenticeParameterMap;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 12:22
 * To change this template use File | Settings | File Templates.
 */
public class AdminRouterHandler extends ContenticeHandler  {
    private Map<String, String> parameterMap;

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
