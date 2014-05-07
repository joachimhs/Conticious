package no.haagensoftware.contentice.data;

/**
 * Created by jhsmbp on 18/04/14.
 */
public class Domain {
    private String id;
    private String domainName;
    private String webappName;
    private Boolean active;
    private Boolean minified;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDomainName() {
        return domainName;
    }

    public void setDomainName(String domainName) {
        this.domainName = domainName;
    }

    public String getWebappName() {
        return webappName;
    }

    public void setWebappName(String webappName) {
        this.webappName = webappName;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMinified() {
        return minified;
    }

    public void setMinified(Boolean minified) {
        this.minified = minified;
    }
}
