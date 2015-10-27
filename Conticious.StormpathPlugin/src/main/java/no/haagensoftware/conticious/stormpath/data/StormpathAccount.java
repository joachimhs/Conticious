package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 30/03/15.
 */
public class StormpathAccount {
    private String href;
    private String email;
    private String surname;
    private String givenName;
    private String username;
    private String password;
    private String status;
    private StormpathHref emailVerificationToken;
    private StormpathHref customData;
    private StormpathHref providerData;
    private StormpathHref directory;
    private StormpathHref tenant;
    private StormpathHref groups;
    private StormpathHref applications;
    private StormpathHref groupMemberships;
    private StormpathHref apiKeys;

    public StormpathAccount() {
    }

    public StormpathAccount(String email, String surname, String givenName, String password) {
        this.email = email;
        this.surname = surname;
        this.givenName = givenName;
        this.password = password;
        this.status = "UNVERIFIED";
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getGivenName() {
        return givenName;
    }

    public void setGivenName(String givenName) {
        this.givenName = givenName;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public StormpathHref getEmailVerificationToken() {
        return emailVerificationToken;
    }

    public void setEmailVerificationToken(StormpathHref emailVerificationToken) {
        this.emailVerificationToken = emailVerificationToken;
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

    public StormpathHref getDirectory() {
        return directory;
    }

    public void setDirectory(StormpathHref directory) {
        this.directory = directory;
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
