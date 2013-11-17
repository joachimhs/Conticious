package no.haagensoftware.contentice.handler;

import java.util.Map;

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
}
