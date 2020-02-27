package no.teknologihuset.calendar;

import java.util.Date;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class RoomEvent {
    private String id;
    private String googleCalId;
    private Integer hour;
    private Integer endHour;
    private String name;
    private Date start;
    private Date end;
    private String description;
    private String room;


    RoomEvent() {
    }

    public RoomEvent(String id, String googleCalId, Integer hour, Integer endHour, String name, Date start, Date end, String description) {
        this.id = id;
        this.googleCalId = googleCalId;
        this.hour = hour;
        this.endHour = endHour;
        this.name = name;
        this.start = start;
        this.end = end;
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGoogleCalId() {
        return googleCalId;
    }

    public void setGoogleCalId(String googleCalId) {
        this.googleCalId = googleCalId;
    }

    public Integer getHour() {
        return hour;
    }

    public void setHour(Integer hour) {
        this.hour = hour;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getStart() {
        return start;
    }

    public void setStart(Date start) {
        this.start = start;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }
}