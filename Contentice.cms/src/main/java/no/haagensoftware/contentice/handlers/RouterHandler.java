package no.haagensoftware.contentice.handlers;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;
import io.netty.handler.stream.ChunkedWriteHandler;
import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.data.Settings;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.handler.FileServerHandler;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import no.haagensoftware.contentice.spi.RouterPlugin;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.contentice.util.PluginResolver;
import org.apache.log4j.Logger;

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

    private PluginResolver pluginResolver;
    private Class<? extends ChannelHandler> defaultHandler;

    public RouterHandler(PluginResolver pluginResolver,Class<? extends ChannelHandler> defaultHandler, StoragePlugin storage) {
        this.pluginResolver = pluginResolver;
        this.defaultHandler = defaultHandler;
        this.storage = storage;
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        logger.info("channelRead: HttpRequest. File handlers before: " + FileServerHandler.getOpenFileDescriptorCount());

        String url = fullHttpRequest.getUri();
        logger.info("URL: " + url);

        Domain domain = Settings.getInstance().getConticiousOptions().getWebappForHost(getHost(fullHttpRequest));

        ChannelHandler channelHandler = null;
        ConticiousPlugin plugin = pluginResolver.getPluginForUrl(url);

        if (plugin != null) {
            if (plugin instanceof RouterPlugin) {
                RouterPlugin routerPlugin = (RouterPlugin)plugin;
                channelHandler = ((RouterPlugin) plugin).getHandlerForUrl(url);
            }
        }

        logger.info("channelHandler: " + channelHandler);

        if (channelHandler == null) {
            if (defaultHandler == null) {
                defaultHandler = NotFoundHandler.class;
            }

            logger.info("DEFAULT HANDLER");
            //Load default handler

            ChannelHandler handler = defaultHandler.newInstance();

            if (handler instanceof FileServerHandler) {
                //addFileHandlers(channelHandlerContext, fullHttpRequest);
                ((FileServerHandler)handler).setDomain(domain);
            }
            addOrReplaceHandler(channelHandlerContext, handler, "route-generated", fullHttpRequest);
        } else {

            if (channelHandler instanceof FileServerHandler) {
                //Initializer Handler correctly if the handler is a subclass of the FileServerHandler
                //((FileServerHandler)handler).setFromClasspath(false);
                //addFileHandlers(channelHandlerContext, fullHttpRequest);
                ((FileServerHandler)channelHandler).setDomain(domain);
            } else if (channelHandler instanceof ContenticeHandler) {
                //addDataHandlers(channelHandlerContext, fullHttpRequest);

                //Initializer Handler correctly if the handler is a subclass of the ContenticeHandler
                ((ContenticeHandler)channelHandler).setDomain(domain);
                ((ContenticeHandler)channelHandler).setPluginResolver(pluginResolver);
            }
            addOrReplaceHandler(channelHandlerContext, channelHandler, "route-generated", fullHttpRequest);
        }

        fullHttpRequest.retain();
        logger.info("//channelRead: HttpRequest. File handlers before: " + FileServerHandler.getOpenFileDescriptorCount());
        channelHandlerContext.fireChannelRead(fullHttpRequest);
    }

    private void addFileHandlers(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        removeHandler(channelHandlerContext.pipeline(), "codec");
        removeHandler(channelHandlerContext.pipeline(), "aggregator");

        addOrReplaceHandler(channelHandlerContext, new HttpRequestDecoder(), "decoder", fullHttpRequest);
        addOrReplaceHandler(channelHandlerContext, new HttpObjectAggregator(65536), "aggregator", fullHttpRequest);
        addOrReplaceHandler(channelHandlerContext, new HttpResponseEncoder(), "encoder", fullHttpRequest);
        addOrReplaceHandler(channelHandlerContext, new ChunkedWriteHandler(), "chunkedWriter", fullHttpRequest);
    }

    private void addDataHandlers(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) {
        removeHandler(channelHandlerContext.pipeline(), "decoder");
        removeHandler(channelHandlerContext.pipeline(), "aggregator");
        removeHandler(channelHandlerContext.pipeline(), "encoder");
        removeHandler(channelHandlerContext.pipeline(), "chunkedWriter");

        addOrReplaceHandler(channelHandlerContext, new HttpServerCodec(), "codec", fullHttpRequest);
        addOrReplaceHandler(channelHandlerContext, new HttpObjectAggregator(65536), "aggregator", fullHttpRequest);

    }

    private void addOrReplaceHandler(ChannelHandlerContext channelHandlerContext, ChannelHandler channelHandler, String handleName, FullHttpRequest fullHttpRequest) {
        if (channelHandlerContext.pipeline().get(handleName) == null) {
            logger.info("Adding handler last: " + handleName + " : " + channelHandler);
            channelHandlerContext.pipeline().addLast(handleName, channelHandler);
        } else {
            logger.info("replacing handler: " + handleName);
            channelHandlerContext.pipeline().replace(handleName, handleName, channelHandler);
        }


    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        super.exceptionCaught(ctx, cause);    //To change body of overridden methods use File | Settings | File Templates.
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
