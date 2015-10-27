package no.haagensoftware.contentice.handler;

import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpMethod;
import no.haagensoftware.contentice.data.Domain;
import no.haagensoftware.contentice.spi.AuthenticationPlugin;
import no.haagensoftware.contentice.spi.PostProcessorPlugin;
import no.haagensoftware.contentice.spi.StoragePlugin;
import no.haagensoftware.contentice.util.PluginResolver;

import java.util.List;
import java.util.Map;

/**
 * Created by jhsmbp on 12/09/15.
 */
public abstract class CommonConticiousOutboundHandler extends ChannelOutboundHandlerAdapter implements ContenticeParameterMap {
    private Domain domain;
    private AuthenticationPlugin authenticationPlugin;
    private PluginResolver pluginResolver;
    private PostProcessorPlugin postProcessorPlugin;
    private List<PostProcessorPlugin> postProcessorPlugins;
    private StoragePlugin storage;
    private Map<String, String> parameterMap;
    private List<String> queryStringIds;

    @Override
    public String getParameter(String key) {
        String value = null;

        if (parameterMap != null) {
            value = parameterMap.get(key);
        }

        if (value != null) {
            value = value.replaceAll("%20", " ");
        }

        return value;
    }

    @Override
    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }

    @Override
    public List<String> getQueryStringIds() {
        return queryStringIds;
    }

    @Override
    public void setQueryStringIds(List<String> queryStringids) {
        this.queryStringIds = queryStringids;
    }

    @Override
    public PluginResolver getPluginResolver() {
        return pluginResolver;
    }

    @Override
    public void setPluginResolver(PluginResolver pluginResolver) {
        this.pluginResolver = pluginResolver;
    }

    @Override
    public PostProcessorPlugin getPostProcessorPlugin() {
        return postProcessorPlugin;
    }

    public void setPostProcessorPlugin(PostProcessorPlugin postProcessorPlugin) {
        this.postProcessorPlugin = postProcessorPlugin;
    }

    @Override
    public List<PostProcessorPlugin> getPostProcessorPlugins() {
        return postProcessorPlugins;
    }

    @Override
    public void setPostProcessorPlugins(List<PostProcessorPlugin> postProcessorPlugins) {
        this.postProcessorPlugins = postProcessorPlugins;
    }

    public Map<String, String> getParameterMap() {
        return parameterMap;
    }

    public AuthenticationPlugin getAuthenticationPlugin() {
        return authenticationPlugin;
    }

    public void setAuthenticationPlugin(AuthenticationPlugin authenticationPlugin) {
        this.authenticationPlugin = authenticationPlugin;
    }

    public Domain getDomain() {
        return domain;
    }

    public void setDomain(Domain domain) {
        this.domain = domain;
    }

    public void setStorage(StoragePlugin storage) {
        this.storage = storage;
    }

    public StoragePlugin getStorage() {
        return storage;
    }

    public boolean isPut(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.PUT;
    }

    public boolean isPost(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.POST;
    }

    public boolean isGet(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.GET;
    }

    public boolean isDelete(FullHttpRequest fullHttpRequest) {
        HttpMethod method = fullHttpRequest.getMethod();
        return method == HttpMethod.DELETE;
    }
}
