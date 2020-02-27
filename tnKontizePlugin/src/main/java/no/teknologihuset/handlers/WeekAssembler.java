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

        /*List<String> eventsAdded = new ArrayList<>();

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

                    newRoomDay = new RoomDay(roomWeek.getId() + ";" + (index + 1), (index + 1), dayOfMonth, weekNum, yearNum, monthNum, roomId);
                }

                String earlyGoogleCalId = null;
                String lateGoogleCalId = null;
                String fullGoogleCalId = null;

                for (int hourIndex = 8; hourIndex < 17; hourIndex++) {
                    String eventId = newRoomDay.getId() + ";" + hourIndex;

                    if (!eventsAdded.contains(eventId)) {
                        eventsAdded.add(eventId);
                        RoomEvent roomEvent = null;

                        if (roomDay != null) {
                            roomEvent = roomDay.getRoomEvent(eventId);
                        }

                        if (roomEvent == null) {
                            //String id, String googleCalId, String name, Date start, Date end, String description
                            roomEvent = new RoomEvent(eventId, null, hourIndex, (hourIndex + 1), newRoomDay.getDayOfMonth(), newRoomDay.getRoomMonth(), null, null, null, null);
                        } else {
                            if (hourIndex < 13) {
                                earlyGoogleCalId = roomEvent.getGoogleCalId();
                                fullGoogleCalId =  roomEvent.getGoogleCalId();
                            } else if (hourIndex < 18) {
                                lateGoogleCalId = roomEvent.getGoogleCalId();
                                fullGoogleCalId =  roomEvent.getGoogleCalId();
                            }
                        }

                        roomEvent.setHour(hourIndex);
                        roomEvent.setEndHour(hourIndex + 1);
                        roomEvent.setDayOfMonth(newRoomDay.getDayOfMonth());
                        roomEvent.setMonth(newRoomDay.getRoomMonth());
                        roomEvent.setRoom(roomId);

                        if (newRoomDay.getRoomEvent(eventId) == null) {
                            newRoomDay.getRoomEvents().add(roomEvent);
                        }

                        roomEvents.add(new Gson().toJsonTree(roomEvent));
                    }
                }

                RoomEvent earlyEvent = new RoomEvent(newRoomDay.getId() + ";early", earlyGoogleCalId, 8, 12, newRoomDay.getDayOfMonth(), newRoomDay.getRoomMonth(), null, null, null, null);
                earlyEvent.setRoom(roomId);
                RoomEvent lateEvent = new RoomEvent(newRoomDay.getId() + ";late", lateGoogleCalId, 13, 17, newRoomDay.getDayOfMonth(), newRoomDay.getRoomMonth(), null, null, null, null);
                lateEvent.setRoom(roomId);

                newRoomDay.getHalfdayEvents().add(earlyEvent);
                newRoomDay.getHalfdayEvents().add(lateEvent);
                roomEvents.add(new Gson().toJsonTree(earlyEvent));
                roomEvents.add(new Gson().toJsonTree(lateEvent));

                RoomEvent fulldayEvent = new RoomEvent(newRoomDay.getId() + ";full", fullGoogleCalId, 8, 17, newRoomDay.getDayOfMonth(), newRoomDay.getRoomMonth(), null, null, null, null);
                fulldayEvent.setRoom(roomId);
                newRoomDay.setFulldayEvent(fulldayEvent);
                roomEvents.add(new Gson().toJsonTree(fulldayEvent));

                roomDays.add(new Gson().toJsonTree(new RoomDayJson(newRoomDay)));
            }
        }
        topLevelObject.add("roomWeeks", roomWeeks);
        topLevelObject.add("roomDays", roomDays);
        topLevelObject.add("roomEvents", roomEvents);
*/

        return topLevelObject;
    }
}
