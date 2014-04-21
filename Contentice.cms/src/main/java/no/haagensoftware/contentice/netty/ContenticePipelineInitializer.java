package no.haagensoftware.contentice.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.*;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import no.haagensoftware.contentice.handler.FileServerHandler;
import no.haagensoftware.contentice.handlers.*;
import no.haagensoftware.contentice.plugin.RouterPluginService;
import no.haagensoftware.contentice.plugin.StoragePluginService;
import no.haagensoftware.contentice.spi.RouterPlugin;
import no.haagensoftware.contentice.util.PluginResolver;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/15/13
 * Time: 9:26 AM
 * To change this template use File | Settings | File Templates.
 */
public class ContenticePipelineInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger logger = Logger.getLogger(ContenticePipelineInitializer.class.getName());

    private PluginResolver pluginResolver;

    public ContenticePipelineInitializer(PluginResolver pluginResolver) {
        this.pluginResolver = pluginResolver;

        //this.urlResolver.addUrlPattern("/json/categories", CategoriesHandler.class);
        //this.urlResolver.addUrlPattern("/json/categories/{category}", CategoryHandler.class);
        //this.urlResolver.addUrlPattern("/json/categories/{category}/subcategories", SubCategoriesHandler.class);
        //this.urlResolver.addUrlPattern("/json/categories/{category}/subcategories/{subcategory}", SubCategoryHandler.class);

        //Load plugins and add URLs to urlResolver
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        logger.info("initChannelHandler");
        ChannelPipeline pipeline = ch.pipeline();

        pipeline.addLast("codec", new HttpServerCodec());
        pipeline.addLast("aggregator", new HttpObjectAggregator(5 * 1024 * 1024));
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

        for (RouterPlugin routerPlugin : RouterPluginService.getInstance().getRouterPlugins()) {
            for (String key : routerPlugin.getRoutes().keySet()) {
                pluginResolver.addUrlPattern(key, routerPlugin);
            }
            if (routerPlugin.getPlurals() != null) {
                for (String singular : routerPlugin.getPlurals().keySet()) {
                    pluginResolver.addPlural(singular, routerPlugin.getPlurals().get(singular));
                }
            }
        }

        //Router Handler
        pipeline.addLast("router_handler", new RouterHandler(pluginResolver, FileServerHandler.class, StoragePluginService.getInstance().getStoragePluginWithName(System.getProperty("no.haagensoftware.contentice.storage.plugin"))));


        //pipeline.addLast("handler", new HttpStaticFileServerHandler(true));
    }
}
