package no.haagensoftware.contentice.plugin.admindata;

/**
 * Created by jhsmbp on 04/05/14.
 */
public class AdminSpgData {
    private String domain;
    private String hostname;
    private String port;

    public AdminSpgData() {
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }
}
