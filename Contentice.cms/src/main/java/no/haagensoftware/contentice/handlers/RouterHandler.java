package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.URLData;
import no.haagensoftware.contentice.util.URLResolver;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouterHandler extends ContenticeGenericHandler {
    private static final Logger logger = Logger.getLogger(ContenticeGenericHandler.class.getName());

    private URLResolver urlResolver;

    public RouterHandler(URLResolver urlResolver) {
        this.urlResolver = urlResolver;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String url = fullHttpRequest.getUri();
        logger.info("URL: " + url);
        URLData urlData = urlResolver.getValueForUrl(url);

        logger.info("urlData:" + urlData);

        if (urlData == null) {
            //Load default handler
        } else {
            logger.info("Handler: " + urlData.getChannelHandler());
            addOrReplaceHandler(channelHandlerContext, urlData.getChannelHandler(), "route-generated");
            //removeHandler(channelHandlerContext.pipeline(), "default-handler");
        }


    }

    private void addOrReplaceHandler(ChannelHandlerContext channelHandlerContext, ChannelHandler channelHandler, String handleName) {
        if (channelHandlerContext.pipeline().get(handleName) == null) {
            logger.info("Adding handler last: " + handleName + " : " + channelHandler);
            channelHandlerContext.pipeline().addLast(handleName, channelHandler);
        } else {
            logger.info("replacing handler: " + handleName);
            channelHandlerContext.pipeline().replace(handleName, handleName, channelHandler);
        }
    }

    private void removeHandler(ChannelPipeline pipeline, String handleName) {
        synchronized (pipeline) {
            if (pipeline.get(handleName) != null) {
                System.out.println("removing handle: " + handleName);
                pipeline.remove(handleName);
            }
        }
    }
}
