package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.spi.StoragePlugin;

import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.logging.Logger;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 1:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class StoragePluginService {
    private static final Logger logger = Logger.getLogger(StoragePluginService.class.getName());
    private static StoragePluginService pluginService = null;
    private ServiceLoader<StoragePlugin> loader;

    public StoragePluginService() {
        loader = ServiceLoader.load(StoragePlugin.class);
    }

    public static synchronized StoragePluginService getInstance() {
        if (pluginService == null) {
            pluginService = new StoragePluginService();
        }

        return pluginService;
    }

    public StoragePlugin getStoragePluginWithName(String storagePluginName) {
        StoragePlugin returnPlugin = null;

        for (StoragePlugin plugin : loader) {
            if (plugin.getStoragePluginName().equals(storagePluginName)) {
                returnPlugin = plugin;
                break;
            }
        }

        if (returnPlugin == null) {
            throw new IllegalArgumentException("There is no plugin named: " + storagePluginName + ". Storage is unavailable. Please load the plugin JAR file into the plugins directory.");
        }

        return returnPlugin;
    }
}
