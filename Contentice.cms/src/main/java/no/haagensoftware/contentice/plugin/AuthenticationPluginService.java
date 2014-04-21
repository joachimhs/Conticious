package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.main.ClassPathUtil;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created by jhsmbp on 20/04/14.
 */
public class AuthenticationPluginService implements PluginService {
    private static final Logger logger = Logger.getLogger(AuthenticationPluginService.class.getName());

    private static AuthenticationPluginService pluginService = null;
    private ServiceLoader<AuthenticationPlugin> loader = null;

    public AuthenticationPluginService() {
        ClassPathUtil.addPluginDirectory();
        logger.info("Loading Authentication plugins");
        loader = ServiceLoader.load(AuthenticationPlugin.class);

        for (AuthenticationPlugin plugin : loader) {
            logger.info("Plugin loaded: " + plugin);
        }

        logger.info("Done loading authentication plugins");
    }

    public static synchronized AuthenticationPluginService getInstance() {
        if (pluginService == null) {
            pluginService = new AuthenticationPluginService();
        }

        return pluginService;
    }

    public AuthenticationPlugin getAuthenticationPlugin(String pluginName) {
        AuthenticationPlugin authenticationPlugin = null;

        for (AuthenticationPlugin plugin: loader) {
            if (plugin.getPluginName().equals(pluginName)) {
                authenticationPlugin = plugin;
                break;
            }
        }

        return authenticationPlugin;
    }

    @Override
    public List<ConticiousPlugin> getLoadedPlugins() {
        List<ConticiousPlugin> loadedPlugins = new ArrayList<>();

        for (AuthenticationPlugin plugin: loader) {
            loadedPlugins.add(plugin);
        }

        return loadedPlugins;
    }
}
