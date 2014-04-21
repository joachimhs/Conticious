package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.data.auth.User;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by jhsmbp on 20/04/14.
 */
public class AdminAuthenticationPlugin extends AuthenticationPlugin {
    private List<Session> sessionList;

    public AdminAuthenticationPlugin() {
        this.sessionList = new ArrayList<>();
    }

    @Override
    public String getPluginName() {
        return "AdminAuthenticationPlugin";
    }

    @Override
    public List<String> getPluginDependencies() {
        return new ArrayList<>();
    }

    @Override
    public String authenticateUser(String username, String password) {
        String uuid = null;

        if (username.equals("joachimhs") && password.equals("pass")) {
            User user = getUser(username);
            if (user == null) {
                user = new User();
                user.setUsername(username);
                user.setPassword(password);

                Session session = new Session();
                session.setUser(user);
                session.setUuid(UUID.randomUUID().toString());

                sessionList.add(session);

                uuid = session.getUuid();
            }
        } else {

        }
        return uuid;
    }

    @Override
    public boolean isUuidAuthenticated(String uuid) {
        boolean authenticated = false;

        Session session = getSession(uuid);
        if (session != null) {
            authenticated = session.isAuthenticated();
        }

        return authenticated;
    }

    @Override
    public boolean logOutUserWithUuid(String uuid) {
        Session session = getSession(uuid);
        if (session != null) {
            sessionList.remove(session);
        }

        return true;
    }

    private Session getSession(String uuid) {
        Session foundSession = null;

        for (Session session : sessionList) {
            if (session.getUuid().equals(uuid)) {
                foundSession = session;
                break;
            }
        }

        return foundSession;
    }

    private User getUser(String username) {
        User user = null;

        for (Session session : sessionList) {
            if (session.getUser().getUsername().equals(username)) {
                user = session.getUser();
                break;
            }
        }

        return user;
    }
}
