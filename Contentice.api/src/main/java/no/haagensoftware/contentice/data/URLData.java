package no.haagensoftware.contentice.data;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 16.11.13
 * Time: 16:58
 * To change this template use File | Settings | File Templates.
 */
public class URLData {
    private String urlPattern;
    private String realUrl;
    private Map<String, String> parameters;

    public URLData(String urlPattern, String realUrl, Map<String, String> parameters) {
        this.urlPattern = urlPattern;
        this.realUrl = realUrl;
        this.parameters = parameters;
    }

    public String getUrlPattern() {
        return urlPattern;
    }

    public String getRealUrl() {
        return realUrl;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }
}
