package no.teknologihuset.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.teknologihuset.calendar.CalendarRoom;
import no.teknologihuset.calendar.GoogleCal;
import no.teknologihuset.calendar.RoomWeek;
import no.teknologihuset.calendar.Week;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jhsmbp on 12/9/13.
 */
public class WeekHandler extends ContenticeHandler {
    private GoogleCal googleCal;
    private List<String> validRooms;

    public WeekHandler() {
        googleCal = GoogleCal.getInstance();
        validRooms = new ArrayList<>();
        validRooms.add("Big Conference Room");
        validRooms.add("BoardRoom");
        validRooms.add("GameRoom");
        validRooms.add("GreenRoom");
        validRooms.add("RegularRoom");
        validRooms.add("Small Conference Room");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonResponse = "";

        String week = getParameter("week");

        if (week != null) {
            week = week.replaceAll("%3B", ";");
        }

        if (week != null && week.contains(";")) {
            String[] weekParts = week.split(";");
            if (weekParts.length == 2) {
                String year = weekParts[0];
                String weeknum = weekParts[1];

                Week weekObject = new Week();
                weekObject.setId(week);
                weekObject.setWeeknum(Integer.parseInt(weeknum));
                weekObject.setYear((Integer.parseInt(year)));

                List<RoomWeek> roomWeekArray= new ArrayList<>();

                List<String> roomWeeks = new ArrayList<>();
                for (String roomName : validRooms) {
                    roomWeeks.add(roomName + ";" + week);

                    CalendarRoom calendarRoom = googleCal.getCalendarRoom(roomName);
                    if (calendarRoom == null) {
                        calendarRoom = new CalendarRoom();
                        calendarRoom.setId(roomName);
                    }

                    RoomWeek roomWeek = null;//calendarRoom.getRoomWeek(Integer.parseInt(weeknum), Integer.parseInt(year));
                    if (roomWeek == null) {
                        Calendar dayCal = Calendar.getInstance();
                        dayCal.set(Calendar.WEEK_OF_YEAR, Integer.parseInt(weeknum));
                        dayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

                        Integer weekNum = dayCal.get(java.util.Calendar.WEEK_OF_YEAR);
                        Integer monthNum = dayCal.get(java.util.Calendar.MONTH) +1;
                        Integer yearNum = dayCal.get(java.util.Calendar.YEAR);

                        roomWeek = new RoomWeek(calendarRoom.getId() + ";" + week, weekNum, monthNum, yearNum, roomName);
                    }

                    roomWeekArray.add(roomWeek);
                }

                weekObject.setRoomWeeks(roomWeeks);

                jsonResponse = WeekAssembler.assembleWeekDaytime(weekObject, roomWeekArray).toString();
            }
        }

        if (jsonResponse.length() == 0) {
            jsonResponse = "{}";
        }

        writeContentsToBuffer(channelHandlerContext, jsonResponse, "application/json");
    }
}