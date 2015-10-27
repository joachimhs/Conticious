package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 30/03/15.
 */
public class StormpathApplication {
    private String href;
    private String name;
    private String description;
    private String status;
    private StormpathHref tenant;
    private StormpathHref defaultAccountStoreMapping;
    private StormpathHref defaultGroupStoreMapping;
    private StormpathHref customData;
    private StormpathHref accounts;
    private StormpathHref groups;
    private StormpathHref accountStoreMappings;
    private StormpathHref loginAttempts;
    private StormpathHref passwordResetTokens;
    private StormpathHref apiKeys;
    private StormpathHref verificationEmails;

    public StormpathApplication() {
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

    public StormpathHref getDefaultAccountStoreMapping() {
        return defaultAccountStoreMapping;
    }

    public void setDefaultAccountStoreMapping(StormpathHref defaultAccountStoreMapping) {
        this.defaultAccountStoreMapping = defaultAccountStoreMapping;
    }

    public StormpathHref getDefaultGroupStoreMapping() {
        return defaultGroupStoreMapping;
    }

    public void setDefaultGroupStoreMapping(StormpathHref defaultGroupStoreMapping) {
        this.defaultGroupStoreMapping = defaultGroupStoreMapping;
    }

    public StormpathHref getCustomData() {
        return customData;
    }

    public void setCustomData(StormpathHref customData) {
        this.customData = customData;
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

    public StormpathHref getAccountStoreMappings() {
        return accountStoreMappings;
    }

    public void setAccountStoreMappings(StormpathHref accountStoreMappings) {
        this.accountStoreMappings = accountStoreMappings;
    }

    public StormpathHref getLoginAttempts() {
        return loginAttempts;
    }

    public void setLoginAttempts(StormpathHref loginAttempts) {
        this.loginAttempts = loginAttempts;
    }

    public StormpathHref getPasswordResetTokens() {
        return passwordResetTokens;
    }

    public void setPasswordResetTokens(StormpathHref passwordResetTokens) {
        this.passwordResetTokens = passwordResetTokens;
    }

    public StormpathHref getApiKeys() {
        return apiKeys;
    }

    public void setApiKeys(StormpathHref apiKeys) {
        this.apiKeys = apiKeys;
    }

    public StormpathHref getVerificationEmails() {
        return verificationEmails;
    }

    public void setVerificationEmails(StormpathHref verificationEmails) {
        this.verificationEmails = verificationEmails;
    }
}
