package no.haagensoftware.contentice.data.auth;

/**
 * Created by jhsmbp on 20/04/14.
 */
public class Session {
    private String uuid;
    private User user;
    private boolean authenticated;

    public Session() {
        authenticated = false;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }
}
