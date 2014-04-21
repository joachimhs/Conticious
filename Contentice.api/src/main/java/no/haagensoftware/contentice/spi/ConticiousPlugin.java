package no.haagensoftware.contentice.spi;

import java.util.List;

/**
 * Created by jhsmbp on 20/04/14.
 */
public interface ConticiousPlugin {
    public List<String> getPluginDependencies();

    public void addPlugin(ConticiousPlugin plugin);

    public String getPluginName();
}
