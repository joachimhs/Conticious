package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.main.ClassPathUtil;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import no.haagensoftware.contentice.spi.PostProcessorPlugin;
import no.haagensoftware.contentice.spi.RouterPlugin;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.ServiceLoader;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/19/13
 * Time: 6:02 PM
 * To change this template use File | Settings | File Templates.
 */
public class PostProcessorPluginService implements PluginService {
    private static final Logger logger = Logger.getLogger(PostProcessorPluginService.class.getName());

    private static PostProcessorPluginService pluginService = null;
    private ServiceLoader<PostProcessorPlugin> loader = null;

    public PostProcessorPluginService() {
        ClassPathUtil.addPluginDirectory();
        logger.info("Loading post processor plugins");
        loader = ServiceLoader.load(PostProcessorPlugin.class);
        for (PostProcessorPlugin plugin : loader) {
            logger.info("Post processor plugin loaded: " + plugin);
        }
        logger.info("Done loading post processor plugins");
    }

    public static synchronized PostProcessorPluginService getInstance() {
        if (pluginService == null) {
            pluginService = new PostProcessorPluginService();
        }

        return pluginService;
    }

    @Override
    public List<ConticiousPlugin> getLoadedPlugins() {
        List<ConticiousPlugin> loadedPlugins = new ArrayList<>();

        for (PostProcessorPlugin plugin: loader) {
            loadedPlugins.add(plugin);
        }

        return loadedPlugins;
    }
}
