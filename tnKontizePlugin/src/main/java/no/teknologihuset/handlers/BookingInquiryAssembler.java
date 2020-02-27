package no.teknologihuset.handlers;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import no.haagensoftware.contentice.data.SubCategoryData;
import no.teknologihuset.handlers.data.BookingInquiry;

/**
 * Created by jhsmbp on 19/02/14.
 */
public class BookingInquiryAssembler {

    public static SubCategoryData convertBookingInquiryToSubCategory(BookingInquiry bookingInquiry) {
        SubCategoryData subCategoryData = new SubCategoryData();
        subCategoryData.setId(bookingInquiry.getId());
        subCategoryData.setName(bookingInquiry.getId());
        subCategoryData.getKeyMap().put("subject", new JsonPrimitive(bookingInquiry.getSubject()));
        subCategoryData.getKeyMap().put("message", new JsonPrimitive(bookingInquiry.getMessage()));
        subCategoryData.getKeyMap().put("firmanavn", new JsonPrimitive(bookingInquiry.getFirmanavn()));
        subCategoryData.getKeyMap().put("epost", new JsonPrimitive(bookingInquiry.getEpost()));
        subCategoryData.getKeyMap().put("tlf", new JsonPrimitive(bookingInquiry.getTlf()));

        if (bookingInquiry.getBeskrivelse() != null) {
            subCategoryData.getKeyMap().put("beskrivelse", new JsonPrimitive(bookingInquiry.getBeskrivelse()));
        }

        subCategoryData.getKeyMap().put("oenskerBevertning", new JsonPrimitive(bookingInquiry.getOenskerBevertning()));

        JsonArray eventsArray = new JsonArray();
        for (String event : bookingInquiry.getEvents()) {
            eventsArray.add(new JsonPrimitive(event));
        }
        subCategoryData.getKeyMap().put("events", eventsArray);


        return subCategoryData;
    }

    public static BookingInquiry convertSubCategoryToBookingInquiry(SubCategoryData subCategoryData) {
        BookingInquiry bookingInquiry = new BookingInquiry();
        bookingInquiry.setId(subCategoryData.getId());

        if (bookingInquiry.getId().startsWith("emailsNotSent_")) {
            bookingInquiry.setId(bookingInquiry.getId().substring(14));
        }

        bookingInquiry.setName(subCategoryData.getName());
        bookingInquiry.setMessage(subCategoryData.getKeyMap().get("message").toString());
        bookingInquiry.setSubject(subCategoryData.getKeyMap().get("subject").toString());
        if (subCategoryData.getKeyMap().get("beskrivelse") != null) {
            bookingInquiry.setBeskrivelse(subCategoryData.getKeyMap().get("beskrivelse").toString());
        }
        bookingInquiry.setEpost(subCategoryData.getKeyMap().get("epost").toString());
        bookingInquiry.setFirmanavn(subCategoryData.getKeyMap().get("firmanavn").toString());
        bookingInquiry.setOenskerBevertning(subCategoryData.getKeyMap().get("oenskerBevertning").getAsBoolean());
        bookingInquiry.setTlf(subCategoryData.getKeyMap().get("tlf").toString());

        List<String> events = new ArrayList<>();
        for (JsonElement element : subCategoryData.getKeyMap().get("events").getAsJsonArray()) {
            events.add(element.getAsString());
        }
        bookingInquiry.setEvents(events);


        return bookingInquiry;
    }
}
