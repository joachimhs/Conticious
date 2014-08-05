package no.haagensoftware.contentice.plugin;

import no.haagensoftware.contentice.data.SubCategoryData;
import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.data.auth.User;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import no.haagensoftware.contentice.spi.ConticiousPlugin;
import no.haagensoftware.contentice.spi.StoragePlugin;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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

    private StoragePlugin getStorage() {
        StoragePlugin storagePlugin = null;

        for (ConticiousPlugin plugin : dependantPluginsList) {
            if (plugin instanceof StoragePlugin) {
                storagePlugin = (StoragePlugin)plugin;
                break;
            }
        }

        return storagePlugin;
    }
    @Override
    public String authenticateUser(String username, String password) {
        SubCategoryData subCategoryData = getStorage().getSubCategory("admin", "users", username);

        String uuid = null;

        if (subCategoryData != null) {
            String realusername = subCategoryData.getName();
            String realpassword = subCategoryData.getKeyMap().get("password").getAsString();
            String realrole = subCategoryData.getKeyMap().get("role").getAsString();


            String md5Pass = null;
            try {
                byte[] bytesOfMessage = password.getBytes();

                MessageDigest md = MessageDigest.getInstance("MD5");
                byte[] thedigest = md.digest(bytesOfMessage);

                StringBuffer sb = new StringBuffer();
                for (int i = 0; i < thedigest.length; i++) {
                    sb.append(Integer.toString((thedigest[i] & 0xff) + 0x100, 16).substring(1));
                }

                md5Pass = sb.toString();
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }

            if (md5Pass != null && username.equals(realusername) && md5Pass.equals(realpassword)) {
                User user = getUser(username);
                if (user == null) {
                    user = new User();
                    user.setUsername(username);
                    user.setPassword(password);
                    user.setRole(realrole);

                    Session session = new Session();
                    session.setUser(user);
                    session.setUuid(UUID.randomUUID().toString());

                    sessionList.add(session);

                    uuid = session.getUuid();
                } else {
                    Session session = getSessionFromUsername(username);
                    uuid = session.getUuid();
                }
            }
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

    public Session getSession(String uuid) {
        Session foundSession = null;

        for (Session session : sessionList) {
            if (uuid != null && session.getUuid().equals(uuid)) {
                foundSession = session;
                break;
            }
        }

        return foundSession;
    }

    private Session getSessionFromUsername(String username) {
        Session foundSession = null;

        for (Session session : sessionList) {
            if (session.getUser() != null && session.getUser().getUsername().equals(username)) {
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
