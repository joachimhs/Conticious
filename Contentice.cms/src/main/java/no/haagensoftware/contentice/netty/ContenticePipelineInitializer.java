package no.haagensoftware.contentice.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.http.HttpContentCompressor;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

import io.netty.channel.socket.SocketChannel;
import io.netty.handler.stream.ChunkedWriteHandler;
import no.haagensoftware.contentice.handler.DataHandler;
import no.haagensoftware.contentice.handler.FileServerHandler;
import no.haagensoftware.contentice.handlers.*;
import no.haagensoftware.contentice.plugin.RouterPluginService;
import no.haagensoftware.contentice.plugin.StoragePluginService;
import no.haagensoftware.contentice.spi.RouterPlugin;
import no.haagensoftware.contentice.util.URLResolver;
import no.kodegenet.handlers.ChaptersHandler;
import no.kodegenet.handlers.CoursesHandler;
import no.kodegenet.handlers.OppgaverHandler;
import no.kodegenet.handlers.RevealHandler;
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

    private URLResolver urlResolver;

    public ContenticePipelineInitializer(URLResolver urlResolver) {
        this.urlResolver = urlResolver;

        this.urlResolver.addUrlPattern("/json/categories", CategoriesHandler.class);
        this.urlResolver.addUrlPattern("/json/categories/{category}", CategoryHandler.class);
        this.urlResolver.addUrlPattern("/json/categories/{category}/subcategories", SubCategoriesHandler.class);
        this.urlResolver.addUrlPattern("/json/categories/{category}/subcategories/{subcategory}", SubCategoryHandler.class);

        this.urlResolver.addUrlPattern("/json/data/{category}", DataHandler.class);
        this.urlResolver.addUrlPattern("/json/data/{category}/{subcategory}", DataHandler.class);

        this.urlResolver.addUrlPattern("/json/courses", CoursesHandler.class);
        this.urlResolver.addUrlPattern("/json/chapters", ChaptersHandler.class);
        this.urlResolver.addUrlPattern("/json/chapters/{chapter}", ChaptersHandler.class);
        this.urlResolver.addUrlPattern("/json/oppgaver", OppgaverHandler.class);
        this.urlResolver.addUrlPattern("/json/oppgaver/{oppgave}", OppgaverHandler.class);


        this.urlResolver.addUrlPattern("/reveal.html", RevealHandler.class);


        this.urlResolver.addPlural("oppgave", "oppgaver");
        //Load plugins and add URLs to urlResolver
    }

    @Override
    public void initChannel(SocketChannel ch) throws Exception {
        logger.info("initChannelHandler");
        ChannelPipeline pipeline = ch.pipeline();

        // Uncomment the following line if you want HTTPS
        //SSLEngine engine = SecureChatSslContextFactory.getServerContext().createSSLEngine();
        //engine.setUseClientMode(false);
        //p.addLast("ssl", new SslHandler(engine));

        pipeline.addLast("decoder", new HttpRequestDecoder());
        pipeline.addLast("aggregator", new HttpObjectAggregator(2097152));
        pipeline.addLast("encoder", new HttpResponseEncoder());
        //pipeline.addLast("deflater", new HttpContentCompressor(1));
        pipeline.addLast("chunkedWriter", new ChunkedWriteHandler());

        for (RouterPlugin routerPlugin : RouterPluginService.getInstance().getRouterPlugins()) {
            for (String key : routerPlugin.getRoutes().keySet()) {
                urlResolver.addUrlPattern(key, routerPlugin.getRoutes().get(key));
            }
        }

        //Router Handler
        pipeline.addLast("router_handler", new RouterHandler(urlResolver, FileServerHandler.class, StoragePluginService.getInstance().getStoragePluginWithName(System.getProperty("no.haagensoftware.contentice.storage.plugin"))));


        //pipeline.addLast("handler", new HttpStaticFileServerHandler(true));
    }
}
