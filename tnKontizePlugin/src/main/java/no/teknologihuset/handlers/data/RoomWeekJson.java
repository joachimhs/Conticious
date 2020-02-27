package no.teknologihuset.handlers.data;

import no.teknologihuset.calendar.RoomDay;
import no.teknologihuset.calendar.RoomWeek;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 12/9/13.
 */
public class RoomWeekJson {
    private String id;
    private Integer roomWeek;
    private Integer roomYear;
    private Integer roomMonth;
    private List<String> roomDays;
    private String room;

    RoomWeekJson() {
        roomDays = new ArrayList<String>();
    }

    public RoomWeekJson(String id, Integer roomWeek, Integer roomMonth, Integer roomYear, String room) {
        this();
        this.id = id;
        this.roomWeek = roomWeek;
        this.roomMonth = roomMonth;
        this.roomYear = roomYear;
        this.room = room;
    }

    public RoomWeekJson(RoomWeek oldRoomWeek, String room) {
        this(oldRoomWeek.getId(),
                oldRoomWeek.getRoomWeek(),
                oldRoomWeek.getRoomMonth(),
                oldRoomWeek.getRoomYear(),
                room);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public List<String> getRoomDays() {
        return roomDays;
    }

    public void setRoomDays(List<String> roomDays) {
        this.roomDays = roomDays;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
