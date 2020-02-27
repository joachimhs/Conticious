package no.teknologihuset.calendar;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class RoomDay {
    private String id;
    private Date date;
    private String room;

    private List<RoomEvent> roomEvents;
    private List<RoomEvent> halfdayEvents;
    private RoomEvent fulldayEvent;
    private RoomEvent communityEvent;

    RoomDay() {
        this.roomEvents = new ArrayList<RoomEvent>();
        this.halfdayEvents = new ArrayList<>();
    }

    public RoomDay(String id, Date date, String room) {
        this();
        this.id = id;
        this.date = date;
        this.room = room;
    }

    public RoomDay(String id, Integer dayOfWeek, Integer dayOfMonth, Integer roomWeek, Integer roomYear, Integer roomMonth, String room) {
        this();
        this.id = id;
        this.room = room;
    }

    public RoomDay(RoomDay roomDay) {
        this(roomDay.getId(), roomDay.getDate(), roomDay.getRoom());

        //Ensure that there is only ONE event for each event ID added. Sometimes the return from Google Calendar includes duplicates!
        List<String> addedEventIds = new ArrayList<>();
        for (RoomEvent event : roomDay.getRoomEvents()) {
            if (!addedEventIds.contains(event.getId())) {
                addedEventIds.add(event.getId());
                roomEvents.add(event);
            }
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<RoomEvent> getRoomEvents() {
        return roomEvents;
    }

    public void setRoomEvents(List<RoomEvent> roomEvents) {
        this.roomEvents = roomEvents;
    }

    public RoomEvent getRoomEvent(String eventId) {
        RoomEvent roomEvent = null;

        for (RoomEvent currEvent : roomEvents) {
            if (currEvent.getId().equals(eventId)) {
                roomEvent = currEvent;
                break;
            }
        }

        return roomEvent;
    }

    public List<RoomEvent> getHalfdayEvents() {
        return halfdayEvents;
    }

    public void setHalfdayEvents(List<RoomEvent> halfdayEvents) {
        this.halfdayEvents = halfdayEvents;
    }

    public RoomEvent getFulldayEvent() {
        return fulldayEvent;
    }

    public void setFulldayEvent(RoomEvent fulldayEvent) {
        this.fulldayEvent = fulldayEvent;
    }

    public RoomEvent getCommunityEvent() {
        return communityEvent;
    }

    public void setCommunityEvent(RoomEvent communityEvent) {
        this.communityEvent = communityEvent;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}