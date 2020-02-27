package no.teknologihuset.handlers.data;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import no.teknologihuset.calendar.RoomDay;
import no.teknologihuset.calendar.RoomEvent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 07/03/15.
 */
public class RoomDayAssembler {

    public static JsonObject assembleRoomDay(List<RoomDay> roomDays) {
        Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").create();

        JsonObject topLevelObject = new JsonObject();

        List<String> eventsAdded = new ArrayList<>();

        JsonArray roomDaysArray = new JsonArray();
        JsonArray roomEvents = new JsonArray();

        for (RoomDay roomDay : roomDays) {
            String earlyGoogleCalId = null;
            String lateGoogleCalId = null;
            String fullGoogleCalId = null;
            String communityGoogleCalId = null;

            for (int hourIndex = 8; hourIndex < 17; hourIndex++) {
                String eventId = roomDay.getId() + "-" + hourIndex;

                if (!eventsAdded.contains(eventId)) {
                    eventsAdded.add(eventId);
                    RoomEvent roomEvent = null;

                    roomEvent = roomDay.getRoomEvent(eventId);

                    if (roomEvent == null) {
                        //String id, String googleCalId, String name, Date start, Date end, String description
                        roomEvent = new RoomEvent(eventId, null, hourIndex, (hourIndex + 1), null, null, null, null);
                    } else {
                        if (roomEvent.getGoogleCalId() != null && hourIndex < 13) {
                            earlyGoogleCalId = roomEvent.getGoogleCalId();
                            fullGoogleCalId = roomEvent.getGoogleCalId();
                        } else if (roomEvent.getGoogleCalId() != null && hourIndex < 18) {
                            lateGoogleCalId = roomEvent.getGoogleCalId();
                            fullGoogleCalId = roomEvent.getGoogleCalId();
                        }
                    }

                    roomEvent.setHour(hourIndex);
                    roomEvent.setEndHour(hourIndex + 1);
                    roomEvent.setRoom(roomDay.getRoom());

                    if (roomDay.getRoomEvent(eventId) == null) {
                        roomDay.getRoomEvents().add(roomEvent);
                    }

                    roomEvents.add(gson.toJsonTree(roomEvent));
                }
            }

            if (roomDay.getCommunityEvent() != null) {
                communityGoogleCalId = roomDay.getCommunityEvent().getGoogleCalId();
            }


            RoomEvent earlyEvent = new RoomEvent(roomDay.getId() + ";early", earlyGoogleCalId, 8, 12, null, null, null, null);
            earlyEvent.setRoom(roomDay.getRoom());
            RoomEvent lateEvent = new RoomEvent(roomDay.getId() + ";late", lateGoogleCalId, 13, 17, null, null, null, null);
            lateEvent.setRoom(roomDay.getRoom());

            roomDay.getHalfdayEvents().add(earlyEvent);
            roomDay.getHalfdayEvents().add(lateEvent);
            roomEvents.add(new Gson().toJsonTree(earlyEvent));
            roomEvents.add(new Gson().toJsonTree(lateEvent));

            RoomEvent fulldayEvent = new RoomEvent(roomDay.getId() + ";full", fullGoogleCalId, 8, 17, null, null, null, null);
            fulldayEvent.setRoom(roomDay.getRoom());
            roomDay.setFulldayEvent(fulldayEvent);
            roomEvents.add(new Gson().toJsonTree(fulldayEvent));

            RoomEvent communityEvent = new RoomEvent(roomDay.getId() + ";community", communityGoogleCalId, 17, 22, null, null, null, null);
            communityEvent.setRoom(roomDay.getRoom());
            roomDay.setCommunityEvent(communityEvent);
            roomEvents.add(new Gson().toJsonTree(communityEvent));

            roomDaysArray.add(new Gson().toJsonTree(new RoomDayJson(roomDay)));
        }

        topLevelObject.add("roomDays", roomDaysArray);
        topLevelObject.add("roomEvents", roomEvents);

        return topLevelObject;
    }
}
