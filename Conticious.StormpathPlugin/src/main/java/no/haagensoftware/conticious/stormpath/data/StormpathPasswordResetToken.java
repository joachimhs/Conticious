package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 04/04/15.
 */
public class StormpathPasswordResetToken {
    private String href;
    private String email;
    private StormpathHref account;
    private StormpathHref accountStore;

    public StormpathPasswordResetToken() {
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

    public StormpathHref getAccount() {
        return account;
    }

    public void setAccount(StormpathHref account) {
        this.account = account;
    }

    public StormpathHref getAccountStore() {
        return accountStore;
    }

    public void setAccountStore(StormpathHref accountStore) {
        this.accountStore = accountStore;
    }
}
