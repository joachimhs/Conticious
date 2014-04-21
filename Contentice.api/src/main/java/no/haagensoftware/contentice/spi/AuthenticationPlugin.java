package no.haagensoftware.contentice.spi;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 20/04/14.
 */
public abstract class AuthenticationPlugin implements ConticiousPlugin {
    public List<ConticiousPlugin> dependantPluginsList = new ArrayList<>();

    @Override
    public void addPlugin(ConticiousPlugin plugin) {
        dependantPluginsList.add(plugin);
    }

    public abstract String authenticateUser(String username, String password);

    public abstract boolean isUuidAuthenticated(String uuid);

    public abstract boolean logOutUserWithUuid(String uuid);
}
