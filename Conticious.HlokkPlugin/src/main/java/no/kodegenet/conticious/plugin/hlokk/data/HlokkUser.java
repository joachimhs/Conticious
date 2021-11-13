package no.kodegenet.conticious.plugin.hlokk.data;

import java.util.Date;

/**
 * Created by joachimhaagenskeie on 16/08/2017.
 */
public class HlokkUser {
    private String token;
    private String username;
    private Long generatedTimestamp;

    public HlokkUser() {
    }

    public HlokkUser(String token, String username) {
        this.token = token;
        this.username = username;
        generatedTimestamp = new Date().getTime();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Long getGeneratedTimestamp() {
        return generatedTimestamp;
    }

    public void setGeneratedTimestamp(Long generatedTimestamp) {
        this.generatedTimestamp = generatedTimestamp;
    }
}
