package no.teknologihuset.handlers.data;

import no.teknologihuset.calendar.RoomDay;
import no.teknologihuset.calendar.RoomEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

/**
 * Created by jhsmbp on 12/9/13.
 */
public class RoomDayJson {
    private String id;
    private Date date;
    private String room;
    private String h8 = null;
    private String h9 = null;
    private String h10 = null;
    private String h11 = null;
    private String h12 = null;
    private String h13 = null;
    private String h14 = null;
    private String h15 = null;
    private String h16 = null;
    private String h17 = null;
    private String c1 = null;
    private List<String> roomEvents;
    private List<String> halfdayEvents;
    private String fulldayEvent;
    private String communityEvent;

    public RoomDayJson() {

    }

    public RoomDayJson(RoomDay roomDay) {
        this();
        this.id = roomDay.getId();
        this.room = roomDay.getRoom();
        this.date = roomDay.getDate();
        this.roomEvents = new ArrayList<>();
        this.halfdayEvents = new ArrayList<>();
        for (RoomEvent roomEvent : roomDay.getRoomEvents()) {
            this.roomEvents.add(roomEvent.getId());
        }
        for (RoomEvent roomEvent : roomDay.getHalfdayEvents()) {
            this.halfdayEvents.add(roomEvent.getId());
        }
        this.fulldayEvent = roomDay.getFulldayEvent().getId();
        this.communityEvent = roomDay.getCommunityEvent().getId();
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

    public List<String> getRoomEvents() {
        return roomEvents;
    }

    public void setRoomEvents(List<String> roomEvents) {
        this.roomEvents = roomEvents;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getH8() {
        return h8;
    }

    public void setH8(String h8) {
        this.h8 = h8;
    }

    public String getH9() {
        return h9;
    }

    public void setH9(String h9) {
        this.h9 = h9;
    }

    public String getH10() {
        return h10;
    }

    public void setH10(String h10) {
        this.h10 = h10;
    }

    public String getH11() {
        return h11;
    }

    public void setH11(String h11) {
        this.h11 = h11;
    }

    public String getH12() {
        return h12;
    }

    public void setH12(String h12) {
        this.h12 = h12;
    }

    public String getH13() {
        return h13;
    }

    public void setH13(String h13) {
        this.h13 = h13;
    }

    public String getH14() {
        return h14;
    }

    public void setH14(String h14) {
        this.h14 = h14;
    }

    public String getH15() {
        return h15;
    }

    public void setH15(String h15) {
        this.h15 = h15;
    }

    public String getH16() {
        return h16;
    }

    public void setH16(String h16) {
        this.h16 = h16;
    }

    public String getH17() {
        return h17;
    }

    public void setH17(String h17) {
        this.h17 = h17;
    }

    public String getC1() {
        return c1;
    }

    public void setC1(String c1) {
        this.c1 = c1;
    }
}
