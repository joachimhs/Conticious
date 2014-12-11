package no.haagensoftware.contentice.data.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import no.haagensoftware.conticious.gson.SerializedClassName;

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

    @Expose
    private List<String> arrProp;

    @Expose
    private List<Integer> intProp;

    @Expose
    private List<User> users;

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

    public List<String> getArrProp() {
        return arrProp;
    }

    public void setArrProp(List<String> arrProp) {
        this.arrProp = arrProp;
    }

    public List<Integer> getIntProp() {
        return intProp;
    }

    public void setIntProp(List<Integer> intProp) {
        this.intProp = intProp;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}
