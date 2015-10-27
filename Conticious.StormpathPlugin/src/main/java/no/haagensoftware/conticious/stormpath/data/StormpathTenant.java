package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 30/03/15.
 */
public class StormpathTenant {
    private String href;
    private String name;

    private StormpathHref customData;
    private StormpathHref providerData;
    private StormpathHref tenant;
    private StormpathHref groups;
    private StormpathHref applications;
    private StormpathHref accounts;
    private StormpathHref groupMemberships;
    private StormpathHref apiKeys;

    public StormpathTenant() {
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

    public StormpathHref getCustomData() {
        return customData;
    }

    public void setCustomData(StormpathHref customData) {
        this.customData = customData;
    }

    public StormpathHref getProviderData() {
        return providerData;
    }

    public void setProviderData(StormpathHref providerData) {
        this.providerData = providerData;
    }

    public StormpathHref getTenant() {
        return tenant;
    }

    public void setTenant(StormpathHref tenant) {
        this.tenant = tenant;
    }

    public StormpathHref getGroups() {
        return groups;
    }

    public void setGroups(StormpathHref groups) {
        this.groups = groups;
    }

    public StormpathHref getApplications() {
        return applications;
    }

    public void setApplications(StormpathHref applications) {
        this.applications = applications;
    }

    public StormpathHref getAccounts() {
        return accounts;
    }

    public void setAccounts(StormpathHref accounts) {
        this.accounts = accounts;
    }

    public StormpathHref getGroupMemberships() {
        return groupMemberships;
    }

    public void setGroupMemberships(StormpathHref groupMemberships) {
        this.groupMemberships = groupMemberships;
    }

    public StormpathHref getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(StormpathHref apiKeys) {
        this.apiKeys = apiKeys;
    }
}
