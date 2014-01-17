package no.teknologihuset.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.teknologihuset.calendar.CalendarRoom;
import no.teknologihuset.calendar.CalendarRooms;
import no.teknologihuset.calendar.GoogleCal;
import no.teknologihuset.calendar.RoomWeek;
import org.apache.log4j.Logger;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class RoomWeekHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(RoomWeekHandler.class.getName());

    private GoogleCal googleCal;

    public RoomWeekHandler() {
        this.googleCal = GoogleCal.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        CalendarRooms calendarRooms = googleCal.getTeknologihusetCalendarRooms();
        String jsonResponse = "";

        String roomNameFromClient = getParameter("roomName");
        if (roomNameFromClient != null) {
            roomNameFromClient = roomNameFromClient.replaceAll("%20", " ");
            logger.info(roomNameFromClient);

            String[] roomParts = roomNameFromClient.split(";");

            if (roomParts.length == 3) {
                String roomName = roomParts[0];
                String year = roomParts[1];
                String week = roomParts[2];

                CalendarRoom calendarRoom = null;

                for (CalendarRoom room : calendarRooms.getCalendarRooms()) {
                    if (room.getId().equalsIgnoreCase(roomName)) {
                        calendarRoom = room;
                        break;
                    }
                }

                if (calendarRoom != null) {
                    RoomWeek roomWeek = calendarRoom.getRoomWeek(Integer.parseInt(week));
                    if (roomWeek != null) {
                        jsonResponse = RoomWeekAssembler.assembleRoomWeek(roomWeek).toString();
                    }
                }
            }
        } else {
            jsonResponse = new Gson().toJson(calendarRooms);
        }

        if (jsonResponse.length() == 0) {
            jsonResponse = "{}";
        }

        writeContentsToBuffer(channelHandlerContext, jsonResponse, "application/json; charset=UTF-8");
    }
}
