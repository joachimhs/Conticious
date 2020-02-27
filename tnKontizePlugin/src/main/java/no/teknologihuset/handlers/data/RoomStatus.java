package no.teknologihuset.handlers.data;

/**
 * Created by jhsmbp on 27/02/15.
 */
public class RoomStatus {
    private String id;
    private Boolean bookable;

    public RoomStatus() {
    }

    public RoomStatus(String id, Boolean bookable) {
        this.id = id;
        this.bookable = bookable;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Boolean getBookable() {
        return bookable;
    }

    public void setBookable(Boolean bookable) {
        this.bookable = bookable;
    }
}
