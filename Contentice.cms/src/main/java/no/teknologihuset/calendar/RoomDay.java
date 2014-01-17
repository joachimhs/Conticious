package no.teknologihuset.calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class RoomDay {
    private String id;
    private Integer dayOfWeek;
    private Integer roomWeek;
    private Integer roomYear;
    private Integer roomMonth;
    private Integer dayOfMonth;
    private List<RoomEvent> roomEvents;

    RoomDay() {
        roomEvents = new ArrayList<RoomEvent>();
    }

    public RoomDay(String id, Integer dayOfWeek, Integer dayOfMonth, Integer roomWeek, Integer roomYear, Integer roomMonth) {
        this();
        this.id = id;
        this.dayOfWeek = dayOfWeek;
        this.roomWeek = roomWeek;
        this.dayOfMonth = dayOfMonth;
        this.roomYear = roomYear;
        this.roomMonth = roomMonth;
    }

    public RoomDay(RoomDay roomDay) {
        this(roomDay.getId(), roomDay.getDayOfWeek(), roomDay.getDayOfMonth(), roomDay.getRoomWeek(), roomDay.getRoomYear(), roomDay.getRoomMonth());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getDayOfWeek() {
        return dayOfWeek;
    }

    public void setDayOfWeek(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    public Integer getRoomWeek() {
        return roomWeek;
    }

    public void setRoomWeek(Integer roomWeek) {
        this.roomWeek = roomWeek;
    }

    public Integer getRoomYear() {
        return roomYear;
    }

    public void setRoomYear(Integer roomYear) {
        this.roomYear = roomYear;
    }

    public Integer getRoomMonth() {
        return roomMonth;
    }

    public void setRoomMonth(Integer roomMonth) {
        this.roomMonth = roomMonth;
    }

    public Integer getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(Integer dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }
}