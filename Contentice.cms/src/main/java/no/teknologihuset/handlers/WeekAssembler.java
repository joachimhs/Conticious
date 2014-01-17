package no.teknologihuset.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.teknologihuset.calendar.RoomDay;
import no.teknologihuset.calendar.RoomEvent;
import no.teknologihuset.calendar.RoomWeek;
import no.teknologihuset.calendar.Week;
import no.teknologihuset.handlers.data.RoomDayJson;
import no.teknologihuset.handlers.data.RoomWeekJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jhsmbp on 12/9/13.
 */
public class WeekAssembler {

    public static JsonObject assembleWeekDaytime(Week week, List<RoomWeek> roomWeekList) {
        JsonObject topLevelObject = new JsonObject();
        topLevelObject.add("week", new Gson().toJsonTree(week));

        List<String> eventsAdded = new ArrayList<>();

        JsonArray roomWeeks = new JsonArray();
        JsonArray roomDays = new JsonArray();
        JsonArray roomEvents = new JsonArray();
        for (RoomWeek roomWeek : roomWeekList) {
            String[] idParts = roomWeek.getId().split(";");
            String roomId = "rooms_" + idParts[0];

            roomWeeks.add(RoomWeekAssembler.assembleRoomWeek(roomWeek));

            for (int index = 0; index < 7; index++) {
                RoomDay roomDay = roomWeek.getRoomDay(index+1);
                RoomDay newRoomDay = null;

                if (roomDay != null) {
                    newRoomDay = new RoomDay(roomDay);
                } else {
                    Calendar dayCal = Calendar.getInstance();
                    dayCal.set(Calendar.WEEK_OF_YEAR, roomWeek.getRoomWeek());
                    dayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                    dayCal.add(Calendar.DATE, index);

                    Integer weekNum = dayCal.get(java.util.Calendar.WEEK_OF_YEAR);
                    Integer monthNum = dayCal.get(java.util.Calendar.MONTH) +1;
                    Integer dayOfMonth = dayCal.get(java.util.Calendar.DAY_OF_MONTH);
                    Integer yearNum = dayCal.get(java.util.Calendar.YEAR);

                    newRoomDay = new RoomDay(roomWeek.getId() + ";" + (index + 1), (index + 1), dayOfMonth, weekNum, yearNum, monthNum);
                }

                for (int hourIndex = 9; hourIndex < 17; hourIndex++) {
                    String eventId = newRoomDay.getId() + ";" + hourIndex;

                    RoomEvent roomEvent = null;

                    if (roomDay != null) {
                        roomEvent = roomDay.getRoomEvent(eventId);
                    }

                    if (roomEvent == null) {
                        //String id, String googleCalId, String name, Date start, Date end, String description
                        roomEvent = new RoomEvent(eventId, null, hourIndex, newRoomDay.getDayOfMonth(), newRoomDay.getRoomMonth(), null, null, null, null);
                    }

                    roomEvent.setHour(hourIndex);
                    roomEvent.setDayOfMonth(newRoomDay.getDayOfMonth());
                    roomEvent.setMonth(newRoomDay.getRoomMonth());
                    roomEvent.setRoom(roomId);
                    newRoomDay.getRoomEvents().add(roomEvent);
                    roomEvents.add(new Gson().toJsonTree(roomEvent));
                }

                roomDays.add(new Gson().toJsonTree(new RoomDayJson(newRoomDay)));
            }
        }
        topLevelObject.add("roomWeeks", roomWeeks);
        topLevelObject.add("roomDays", roomDays);
        topLevelObject.add("roomEvents", roomEvents);


        return topLevelObject;
    }
}
