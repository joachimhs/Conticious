package no.teknologihuset.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import no.teknologihuset.calendar.CalendarRooms;
import no.teknologihuset.calendar.RoomDay;
import no.teknologihuset.calendar.RoomWeek;
import no.teknologihuset.handlers.data.RoomWeekJson;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by jhsmbp on 12/6/13.
 */
public class RoomWeekAssembler {

    public static JsonElement assembleRoomWeek(RoomWeek roomWeek) {
        String[] idParts = roomWeek.getId().split(";");

        RoomWeekJson newRoomWeek = new RoomWeekJson(roomWeek, "rooms_" + idParts[0]);
        List<String> roomDays = new ArrayList<>();

        for (int index = 0; index < 7; index++) {
            RoomDay roomDay = roomWeek.getRoomDay(index+1);
            if (roomDay != null) {
                roomDays.add(roomDay.getId());
            } else {
                Calendar dayCal = Calendar.getInstance();
                dayCal.set(Calendar.WEEK_OF_YEAR, roomWeek.getRoomWeek());
                dayCal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                dayCal.add(Calendar.DATE, index);

                Integer weekNum = dayCal.get(java.util.Calendar.WEEK_OF_YEAR);
                Integer monthNum = dayCal.get(java.util.Calendar.MONTH) +1;
                Integer dayOfMonth = dayCal.get(java.util.Calendar.DAY_OF_MONTH);
                Integer yearNum = dayCal.get(java.util.Calendar.YEAR);

                //RoomDay(String id, Integer dayOfWeek, Integer dayOfMonth, Integer roomWeek, Integer roomYear, Integer roomMonth) {
                //roomDays.add(new RoomDay(roomWeek.getId() + ";" + (index + 1), (index + 1), dayOfMonth, weekNum, yearNum, monthNum));
                roomDays.add(roomWeek.getId() + ";" + (index + 1));
            }
        }

        newRoomWeek.setRoomDays(roomDays);

        return new Gson().toJsonTree(newRoomWeek);
    }

    public static JsonObject assembleRoomWeeks(CalendarRooms calendarRooms){
        return null;
    }
}
