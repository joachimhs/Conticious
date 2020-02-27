package no.teknologihuset.calendar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class CalendarRoom {
    private String id;
    private List<RoomDay> roomDays;



    public CalendarRoom() {
        roomDays = new ArrayList<RoomDay>();
    }

    public RoomDay getRoomDay(Date day) {
        RoomDay roomDay = null;

        for (RoomDay rd : roomDays) {
            if (isSameDay(rd.getDate(), day)) {
                roomDay = rd;
                break;
            }
        }

        return roomDay;
    }

    public List<RoomDay> getRoomDays() {
        return roomDays;
    }

    private boolean isSameDay(Date d1, Date d2) {
        boolean isSameDay = false;

        if (d1 != null && d2 != null) {
            Calendar c1 = Calendar.getInstance();
            c1.setTime(d1);

            Calendar c2 = Calendar.getInstance();
            c2.setTime(d2);

            isSameDay = (c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR) &&
                    c1.get(Calendar.MONTH) == c2.get(Calendar.MONTH) &&
                    c1.get(Calendar.DAY_OF_MONTH) == c2.get(Calendar.DAY_OF_MONTH));

        }
        return isSameDay;
    }

    public void addRoomDay(RoomDay rd) {
        roomDays.add(rd);
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