package no.teknologihuset.calendar;

import java.util.List;
/**
 * Created by jhsmbp on 12/9/13.
 */
public class Week {
    private String id;
    private Integer weeknum;
    private Integer year;
    private List<String> roomWeeks;

    public Week() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getWeeknum() {
        return weeknum;
    }

    public void setWeeknum(Integer weeknum) {
        this.weeknum = weeknum;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public List<String> getRoomWeeks() {
        return roomWeeks;
    }

    public void setRoomWeeks(List<String> roomWeeks) {
        this.roomWeeks = roomWeeks;
    }
}
