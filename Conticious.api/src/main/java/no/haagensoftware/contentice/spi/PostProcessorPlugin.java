package no.haagensoftware.contentice.spi;

import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.data.auth.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 20/04/14.
 */
public abstract class PostProcessorPlugin implements ConticiousPlugin {
    private List<ConticiousPlugin> dependantPluginsList = new ArrayList<>();
    private StoragePlugin storage;
    private Domain domain;

    public void setStorage(StoragePlugin storage) {
        this.storage = storage;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    protected StoragePlugin getStorage() {
        return storage;
    }

    protected Domain getDomain() {
        return domain;
    }

    @Override
    public List<String> getPluginDependencies() {
        return new ArrayList<>();
    }

    @Override
    public void addPlugin(ConticiousPlugin plugin) {
        dependantPluginsList.add(plugin);
    }

    public String postProcess(String input, String originalUrl, String filePath, String queryString, String contentType) {
        return input;
    }

}
