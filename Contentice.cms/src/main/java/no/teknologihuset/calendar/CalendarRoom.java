package no.teknologihuset.calendar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class CalendarRoom {
    private String id;
    private List<RoomWeek> roomWeeks;


    public CalendarRoom() {
        roomWeeks = new ArrayList<RoomWeek>();
    }

    public RoomWeek getRoomWeek(Integer weekNumber) {
        RoomWeek roomWeek = null;


        for (RoomWeek rw : roomWeeks) {
            if (rw.getRoomWeek().intValue() == weekNumber.intValue()) {
                roomWeek = rw;
            }
        }


        return roomWeek;
    }

    public void addRoomWeek(RoomWeek rw) {
        roomWeeks.add(rw);
    }

    CalendarRoom(String id) {
        this();
        this.id = id;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


}