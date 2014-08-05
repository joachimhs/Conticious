package no.haagensoftware.contentice.data;

import io.netty.channel.ChannelHandler;

/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 15.11.13
 * Time: 12:28
 * To change this template use File | Settings | File Templates.
 */
public class CmpiAppRoute {
    String route;
    ChannelHandler handler;

    public CmpiAppRoute() {
    }

    public CmpiAppRoute(String route, ChannelHandler handler) {
        this.route = route;
        this.handler = handler;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public ChannelHandler getHandler() {
        return handler;
    }

    public void setHandler(ChannelHandler handler) {
        this.handler = handler;
    }
}
