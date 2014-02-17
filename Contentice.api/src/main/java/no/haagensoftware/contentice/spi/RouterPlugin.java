package no.haagensoftware.contentice.spi;

import io.netty.channel.ChannelHandler;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class RouterPlugin {
    public abstract LinkedHashMap<String, Class<? extends ChannelHandler>> getRoutes();

    public abstract Class<? extends ChannelHandler> getHandlerForRoute(String route);

    public abstract Map<String, String> getPlurals();
}
