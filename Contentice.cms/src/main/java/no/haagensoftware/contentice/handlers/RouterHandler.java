package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.data.URLData;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.contentice.util.URLResolver;

import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 2:14 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouterHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(RouterHandler.class.getName());
    private StoragePlugin storage;

    private URLResolver urlResolver;
    private Class<? extends ChannelHandler> defaultHandler;

    public RouterHandler(URLResolver urlResolver,Class<ChannelHandler> defaultHandler, StoragePlugin storage) {
        this.urlResolver = urlResolver;
        this.defaultHandler = defaultHandler;
        this.storage = storage;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("channelRead: HttpRequest");
        String url = fullHttpRequest.getUri();
        logger.info("URL: " + url);
        URLData urlData = urlResolver.getValueForUrl(url);

        logger.info("urlData: " + urlData);

        if (urlData == null) {
            if (defaultHandler == null) {
                defaultHandler = NotFoundHandler.class;
            }

            logger.info("DEFAULT HANDLER");
            //Load default handler
            addOrReplaceHandler(channelHandlerContext, defaultHandler.newInstance(), "route-generated");
        } else {
            logger.info("Handler: " + urlData.getChannelHandler());


            ChannelHandler handler = urlData.getChannelHandler().newInstance();
            if (handler instanceof ContenticeHandler) {
                //Initializer Handler correctly if the handler is a subclass of the ContenticeHandler
                ((ContenticeHandler)handler).setParameterMap(urlData.getParameters());
                ((ContenticeHandler)handler).setStorage(storage);
            }
            addOrReplaceHandler(channelHandlerContext, handler, "route-generated");
        }
        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }

    private void addOrReplaceHandler(ChannelHandlerContext channelHandlerContext, ChannelHandler channelHandler, String handleName) {
        if (channelHandlerContext.pipeline().get(handleName) == null) {
            logger.info("Adding handler last: " + handleName + " : " + channelHandler);
            channelHandlerContext.pipeline().addLast(handleName, channelHandler);
        } else {
            logger.info("replacing handler: " + handleName);
            channelHandlerContext.pipeline().remove(handleName);
            channelHandlerContext.pipeline().addLast(handleName, channelHandler);
        }

        for (String name : channelHandlerContext.pipeline().names()) {
            logger.info("Name: " + name);
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
