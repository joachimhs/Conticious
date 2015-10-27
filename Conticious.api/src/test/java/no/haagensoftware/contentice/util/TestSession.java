package no.haagensoftware.contentice.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 05/04/15.
 */
public class TestSession {
    private String id;
    private TestUser user;
    private List<TestUser> users;
    private boolean authenticated;
    private Long lastAccessed;

    public TestSession() {
        authenticated = false;
        users = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TestUser getUser() {
        return user;
    }

    public void setUser(TestUser user) {
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

    public List<TestUser> getUsers() {
        return users;
    }

    public void setUsers(List<TestUser> users) {
        this.users = users;
    }
}
