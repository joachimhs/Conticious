package no.teknologihuset.plugin;

import io.netty.channel.ChannelHandler;
import no.haagensoftware.contentice.spi.RouterPlugin;
import no.teknologihuset.handlers.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jhsmbp on 2/4/14.
 */
public class TeknologihusetRoutesPlugin extends RouterPlugin {

    LinkedHashMap<String, Class<? extends ChannelHandler>> routeMap;
    Map<String, String> plurals;
    public TeknologihusetRoutesPlugin() {
        this.routeMap = new LinkedHashMap<>();
        this.plurals = new LinkedHashMap<>();

        this.plurals.put("community", "communities");

        routeMap.put("/json/rooms", RoomHandler.class);
        routeMap.put("/json/week/{week}", WeekHandler.class);
        routeMap.put("/json/weeks/{week}", WeekHandler.class);
        routeMap.put("/json/roomWeeks", RoomWeekHandler.class);
        routeMap.put("/json/roomWeeks/{roomName}", RoomWeekHandler.class);
        routeMap.put("/json/roomDays", RoomDaysHandler.class);
        routeMap.put("/json/communityEvents", CommunityEventsHandler.class);
        routeMap.put("/json/bookingInquiries", BookingInquiriesHandler.class);

        routeMap.put("/json/pages", PageHandler.class);

    }

    @Override
    public List<String> getPluginDependencies() {
        return new ArrayList<>();
    }

    @Override
    public String getPluginName() {
        return "TeknologihusetRouterPLugin";
    }

    @Override
    public LinkedHashMap<String,Class<? extends ChannelHandler>> getRoutes() {
        return routeMap;
    }

    @Override
    public Map<String, String> getPlurals() {
        return plurals;
    }
}
