package no.haagensoftware.ontentice.sessioncache;

import no.haagensoftware.contentice.data.auth.Session;
import no.haagensoftware.contentice.data.auth.User;

import java.util.Hashtable;
import java.util.UUID;

/**
 * Created by jhsmbp on 07/12/14.
 */
public class SessionCache<T extends User> {
    private Hashtable<String, Session> cache = null;

    public SessionCache() {
        cache = new Hashtable<>();
    }

    public boolean isUuidAuthenticated(String uuid) {
        boolean authenticated = false;

        Session session = getSession(uuid);
        if (session != null) {
            authenticated = session.isAuthenticated();
        }

        return authenticated;
    }

    public boolean logOutUserWithUuid(String uuid) {
        Session session = getSession(uuid);
        if (session != null) {
            cache.remove(uuid);
        }

        return true;
    }

    public Session getSession(String uuid) {
        Session session = cache.get(uuid);
        if (session != null) {
            session.setLastAccessed(System.currentTimeMillis());
        }

        return session;
    }

    public void setAuthenticated(String uuid, boolean authenticated) {
        Session session = getSession(uuid);
        if (session != null) {
            session.setAuthenticated(authenticated);
        }
    }

    public Session createSession(T user) {
        Session session = new Session();
        session.setUser(user);

        session.setUuid(UUID.randomUUID().toString());

        cache.put(session.getUuid(), session);

        return session;
    }
}
