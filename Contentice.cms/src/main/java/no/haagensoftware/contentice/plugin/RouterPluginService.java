package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.spi.RouterPlugin;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceLoader;
import java.util.logging.Logger;

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
    private ServiceLoader<RouterPlugin> loader;

    public RouterPluginService() {
        loader = ServiceLoader.load(RouterPlugin.class);
    }

    public static synchronized RouterPluginService getInstance() {
        if (pluginService == null) {
            pluginService = new RouterPluginService();
        }

        return pluginService;
    }

    public List<RouterPlugin> getRouterPlugins() {
        List<RouterPlugin> plugins = new ArrayList<>();

        Iterator<RouterPlugin> iterator = loader.iterator();

        while (iterator.hasNext()) {
            RouterPlugin plugin = iterator.next();
            plugins.add(plugin);
        }

        return plugins;
    }
}
