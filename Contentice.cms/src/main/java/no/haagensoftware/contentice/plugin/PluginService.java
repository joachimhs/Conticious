package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.spi.ConticiousPlugin;

import java.util.List;
/**
 * Created by jhsmbp on 20/04/14.
 */
public interface PluginService {
    public List<ConticiousPlugin> getLoadedPlugins();
}
