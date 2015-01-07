package no.haagensoftware.contentice.data.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import no.haagensoftware.hyrrokkin.annotations.SerializedClassName;

import java.util.List;

/**
 * Created by jhsmbp on 20/04/14.
 */
@SerializedClassName("session")
public class Session {
    @Expose
    @SerializedName("id")
    private String uuid;

    @Expose
    private User user;

    @Expose
    private boolean authenticated;

    @Expose(deserialize = false, serialize = false)
    private Long lastAccessed;

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

    public Long getLastAccessed() {
        return lastAccessed;
    }

    public void setLastAccessed(Long lastAccessed) {
        this.lastAccessed = lastAccessed;
    }
}
