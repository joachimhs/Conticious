package no.haagensoftware.contentice.data;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.spi.ConticiousPlugin;

import java.util.Map;
import java.util.List;

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
    private ConticiousPlugin plugin;
    private Map<String, String> parameters;
    private List<String> queryStringIds;
    private String queryString;

    public URLData(String urlPattern, String realUrl, Map<String, String> parameters) {
        this.urlPattern = urlPattern;
        this.realUrl = realUrl;
        this.parameters = parameters;
    }

    public URLData(String urlPattern, String realUrl, Map<String, String> parameters, ConticiousPlugin plugin) {
        this.urlPattern = urlPattern;
        this.realUrl = realUrl;
        this.plugin = plugin;
        this.parameters = parameters;
    }

    public void addParameter(String key, String value) {
        if (parameters != null) {
            parameters.put(key, value);
        }
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

    public ConticiousPlugin getPlugin() {
        return plugin;
    }

    public List<String> getQueryStringIds() {
        return queryStringIds;
    }

    public void setQueryStringIds(List<String> queryStringIds) {
        this.queryStringIds = queryStringIds;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }
}
