package no.haagensoftware.contentice.spi;

import io.netty.channel.ChannelHandler;

import java.util.LinkedHashMap;

/**
 * Created with IntelliJ IDEA.
 * User: jhsmbp
 * Date: 11/16/13
 * Time: 10:42 AM
 * To change this template use File | Settings | File Templates.
 */
public abstract class ContenticeRouterPlugin {
    public abstract LinkedHashMap<String, ChannelHandler> getRoutes();

    public abstract ChannelHandler getHandlerForRoute(String route);
}
