package no.haagensoftware.conticious.stormpath.data;

/**
 * Created by jhsmbp on 01/04/15.
 */
public class ProviderData {
    private String providerId;
    private String accessToken;

    public ProviderData() {
    }

    public String getProviderId() {
        return providerId;
    }

    public void setProviderId(String providerId) {
        this.providerId = providerId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
