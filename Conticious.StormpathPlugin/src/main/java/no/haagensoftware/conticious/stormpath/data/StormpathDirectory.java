package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 01/04/15.
 */
public class StormpathDirectory {
    private String href;
    private String name;
    private String description;
    private String status;
    private StormpathHref tenant;
    private StormpathHref provider;
    private StormpathHref accounts;
    private StormpathHref groups;

    public StormpathDirectory() {
    }


    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StormpathHref getTenant() {
        return tenant;
    }

    public void setTenant(StormpathHref tenant) {
        this.tenant = tenant;
    }

    public StormpathHref getProvider() {
        return provider;
    }

    public void setProvider(StormpathHref provider) {
        this.provider = provider;
    }

    public StormpathHref getAccounts() {
        return accounts;
    }

    public void setAccounts(StormpathHref accounts) {
        this.accounts = accounts;
    }

    public StormpathHref getGroups() {
        return groups;
    }

    public void setGroups(StormpathHref groups) {
        this.groups = groups;
    }
}
