package no.haagensoftware.contentice.handler;

import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;

import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 17.11.13
 * Time: 12:24
 * To change this template use File | Settings | File Templates.
 */
public abstract class ContenticeHandler extends SimpleChannelInboundHandler<FullHttpRequest> implements ContenticeParameterMap {
    private Map<String, String> parameterMap;

    @Override
    public String getParameter(String key) {
        String value = null;

        if (parameterMap != null) {
            value = parameterMap.get(key);
        }

        return value;
    }

    @Override
    public void setParameterMap(Map<String, String> parameterMap) {
        this.parameterMap = parameterMap;
    }
}
