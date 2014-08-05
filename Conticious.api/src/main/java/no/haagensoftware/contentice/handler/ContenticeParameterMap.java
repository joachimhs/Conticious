package no.haagensoftware.contentice.handler;

import no.haagensoftware.contentice.util.PluginResolver;

import java.util.Map;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 12:23
 * To change this template use File | Settings | File Templates.
 */
public interface ContenticeParameterMap {

    public void setParameterMap(Map<String, String> parameterMap);

    public String getParameter(String key);

    public void setQueryStringIds(List<String> queryStringids);

    public List<String> getQueryStringIds();

    public void setPluginResolver(PluginResolver pluginResolver);

    public PluginResolver getPluginResolver();
}
