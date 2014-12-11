package no.haagensoftware.contentice.data.auth;

import com.google.gson.annotations.Expose;

/**
 * Created by jhsmbp on 20/04/14.
 */
public class User {
    @Expose
    private String username;

    @Expose(serialize = false, deserialize = false)
    private String password;

    @Expose
    private String role;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
