package no.teknologihuset.handlers.data;

import java.util.List;

/**
 * Created by jhsmbp on 19/02/14.
 */
public class BookingInquiry {
    private String id;
    private String name;
    private String message;
    private String firmanavn;
    private String subject;
    private String epost;
    private String tlf;
    private String beskrivelse;
    private String communityBeskrivelse;
    private Boolean oenskerBevertning;
    private List<String> events;

    public BookingInquiry(BookingInquiry bookingInquiry) {
        this.id = bookingInquiry.getId();
        this.name = bookingInquiry.getName();
        this.message = bookingInquiry.getMessage();
        this.firmanavn = bookingInquiry.getFirmanavn();
        this.subject = bookingInquiry.getSubject();
        this.epost = bookingInquiry.getEpost();
        this.tlf = bookingInquiry.getTlf();
        this.beskrivelse = bookingInquiry.getBeskrivelse();
        this.communityBeskrivelse = bookingInquiry.getCommunityBeskrivelse();
        this.oenskerBevertning = bookingInquiry.getOenskerBevertning();
        this.events = bookingInquiry.getEvents();
    }

    public BookingInquiry() {


    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirmanavn() {
        return firmanavn;
    }

    public void setFirmanavn(String firmanavn) {
        this.firmanavn = firmanavn;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getEpost() {
        return epost;
    }

    public void setEpost(String epost) {
        this.epost = epost;
    }

    public String getTlf() {
        return tlf;
    }

    public void setTlf(String tlf) {
        this.tlf = tlf;
    }

    public String getBeskrivelse() {
        return beskrivelse;
    }

    public void setBeskrivelse(String beskrivelse) {
        this.beskrivelse = beskrivelse;
    }

    public Boolean getOenskerBevertning() {
        return oenskerBevertning;
    }

    public void setOenskerBevertning(Boolean oenskerBevertning) {
        this.oenskerBevertning = oenskerBevertning;
    }

    public List<String> getEvents() {
        return events;
    }

    public void setEvents(List<String> events) {
        this.events = events;
    }

    public String getCommunityBeskrivelse() {
        return communityBeskrivelse;
    }

    public void setCommunityBeskrivelse(String communityBeskrivelse) {
        this.communityBeskrivelse = communityBeskrivelse;
    }
}
