package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.main.ClassPathUtil;
import no.haagensoftware.contentice.spi.RouterPlugin;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class RouterPluginService {
    private static final Logger logger = Logger.getLogger(RouterPluginService.class.getName());

    private static RouterPluginService pluginService = null;
    private ServiceLoader<RouterPlugin> loader = null;

    public RouterPluginService() {
        ClassPathUtil.addPluginDirectory();
        logger.info("Loading router plugins");
        loader = ServiceLoader.load(RouterPlugin.class);
        for (RouterPlugin plugin : loader) {
            logger.info("Plugin loaded: " + plugin);
        }
        logger.info("Done loading router plugins");
    }

    public static synchronized RouterPluginService getInstance() {
        if (pluginService == null) {
            pluginService = new RouterPluginService();
        }

        return pluginService;
    }

    public List<RouterPlugin> getRouterPlugins() {
        List<RouterPlugin> plugins = new ArrayList<>();

        for (RouterPlugin plugin : loader) {
            plugins.add(plugin);
        }

        return plugins;
    }
}
