package no.haagensoftware.conticious.plugin;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import no.haagensoftware.contentice.spi.RouterPlugin;
import no.haagensoftware.conticious.plugin.handler.CachedScriptHandler;
import no.haagensoftware.conticious.plugin.handler.DataHandler;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhsmbp on 21/04/14.
 */
public class DataRoutesPlugin extends RouterPlugin {
    private static final Logger logger = Logger.getLogger(DataRoutesPlugin.class.getName());

    LinkedHashMap<String, Class<? extends ChannelHandler>> routeMap;

    public DataRoutesPlugin() {
        logger.info("Initializing AdminRoutesPlugin");
        routeMap = new LinkedHashMap<>();

        routeMap.put("/json/data/{category}", DataHandler.class);
        routeMap.put("/json/data/{category}/{subcategory}", DataHandler.class);

        routeMap.put("startsWith:/cachedScript", CachedScriptHandler.class);
    }

    @Override
    public List<String> getPluginDependencies() {
        List<String> dependencies = new ArrayList<>();
        return dependencies;
    }

    @Override
    public String getPluginName() {
        return "DataRoutesPlugin";
    }

    @Override
    public LinkedHashMap<String,Class<? extends ChannelHandler>> getRoutes() {
        return routeMap;
    }

    @Override
    public Map<String, String> getPlurals() {
        return null;
    }
}
