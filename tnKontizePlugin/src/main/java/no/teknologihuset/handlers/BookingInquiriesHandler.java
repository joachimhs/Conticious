package no.teknologihuset.handlers;

import com.google.api.client.util.DateTime;
import com.google.api.services.calendar.model.Event;
import com.google.api.services.calendar.model.EventAttendee;
import com.google.api.services.calendar.model.EventDateTime;
import com.google.gson.Gson;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.haagensoftware.contentice.util.FileUtil;
import no.haagensoftware.contentice.util.IntegerParser;
import no.teknologihuset.calendar.GoogleCal;
import no.teknologihuset.epost.EpostExecutor;
import no.teknologihuset.handlers.data.BookingInquiry;
import no.teknologihuset.handlers.data.BookingInquiryObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Calendar;
import java.util.TimeZone;

/**
 * Created by jhsmbp on 1/21/14.
 */
public class BookingInquiriesHandler extends ContenticeHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        String jsonReturn = "";

        String messageContent = getHttpMessageContent(fullHttpRequest);

        if (isPost(fullHttpRequest) && messageContent != null) {
            BookingInquiryObject bookingInquiry = new Gson().fromJson(messageContent, BookingInquiryObject.class);

            if (bookingInquiry != null && bookingInquiry.getBookingInquiry() != null &&
                    bookingInquiry.getBookingInquiry().getFirmanavn() != null &&
                    bookingInquiry.getBookingInquiry().getEvents().size() > 0) {

                bookingInquiry.getBookingInquiry().setId(bookingInquiry.getBookingInquiry().getFirmanavn().replace("/", "_") + "_" + System.currentTimeMillis());
                bookingInquiry.getBookingInquiry().setSubject("Ny Reservasjonsforespørsel fra: " + bookingInquiry.getBookingInquiry().getFirmanavn());

                StringBuilder message = new StringBuilder();

                if (bookingInquiry.getBookingInquiry().getCommunityBeskrivelse() != null) {
                    message.append(bookingInquiry.getBookingInquiry().getCommunityBeskrivelse()).append("\n;;;\n");
                }

                message.append("firmanavn: " + bookingInquiry.getBookingInquiry().getFirmanavn()).append("\n");
                message.append("epost: " + bookingInquiry.getBookingInquiry().getEpost()).append("\n");
                message.append("tlf: " + bookingInquiry.getBookingInquiry().getTlf()).append("\n");
                message.append("beskrivelse: " + bookingInquiry.getBookingInquiry().getBeskrivelse()).append("\n");
                message.append("bevertning: " + bookingInquiry.getBookingInquiry().getOenskerBevertning()).append("\n");
                message.append("events: \n");

                StringBuilder emailMessage = new StringBuilder();
                emailMessage.append("Ny booking forespoersel fra: ").append(bookingInquiry.getBookingInquiry().getFirmanavn()).append(" \r\n ");

                for (String event: bookingInquiry.getBookingInquiry().getEvents()) {
                    String[] eventParts = event.split("-|_|;");
                    if (eventParts.length == 5) {
                        Event googleEvent = createEvent(
                                eventParts[0],
                                eventParts[1],
                                eventParts[2],
                                eventParts[3],
                                eventParts[4],
                                bookingInquiry.getBookingInquiry().getFirmanavn(),
                                bookingInquiry.getBookingInquiry().getEpost(),
                                message.toString());
                        emailMessage.append(googleEvent.getHtmlLink()).append(" \r\n");
                    }
                    message.append(event).append("\n");
                }
                bookingInquiry.getBookingInquiry().setMessage(emailMessage.toString());

                //Email to Customer
                String filename = System.getProperty("no.haagensoftware.contentice.webappDir") + "/" + getDomain().getWebappName() +  "/uploads/booking_kvittering.html";
                File template = new File(filename);
                String templateHtml = null;

                if (template != null && template.isFile() && template.exists()) {
                    try {
                        templateHtml = FileUtil.getFilecontents(template);
                        BookingInquiry emailToCustomer = new BookingInquiry(bookingInquiry.getBookingInquiry());
                        emailToCustomer.setId(emailToCustomer.getId() + "_customer");
                        emailToCustomer.setSubject("Takk for din bookingforespørsel på Teknologihuset!");
                        emailToCustomer.setMessage(templateHtml);

                        getStorage().setSubCategory(getDomain().getWebappName(), "emailsNotSent", emailToCustomer.getId(), BookingInquiryAssembler.convertBookingInquiryToSubCategory(emailToCustomer));
                    } catch (IOException e) {
                        e.printStackTrace();
                        templateHtml = null;
                    }
                }

                //Email to vert!
                getStorage().setSubCategory(getDomain().getWebappName(), "emailsNotSent", bookingInquiry.getBookingInquiry().getId(), BookingInquiryAssembler.convertBookingInquiryToSubCategory(bookingInquiry.getBookingInquiry()));

                EpostExecutor.getInstance(getDomain().getWebappName()).sendRemainingEmails(getStorage());

                jsonReturn = "{\"bookingInquiry\": {}}";

                /*Event event = new Event();
        event.setSummary("New Event");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 3600000);
        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));
        return event;*/
            }
        }

        writeContentsToBuffer(channelHandlerContext, jsonReturn, "application/json");
    }

    private Event createEvent(String roomName, String yearNum, String monthNum, String dayNum, String eventTime, String firmanavn, String attendeeEmail, String besrkivelse) {
        Event event = null;

        Calendar startCal = null;
        Calendar endCal = null;

        try {
            int year = Integer.parseInt(yearNum);
            int month = Integer.parseInt(monthNum)-1;
            int day = Integer.parseInt(dayNum);

            startCal = Calendar.getInstance();
            startCal.set(Calendar.YEAR, year);
            startCal.set(Calendar.MONTH, month);
            startCal.set(Calendar.DATE, day);

            endCal = Calendar.getInstance();
            endCal.set(Calendar.YEAR, year);
            endCal.set(Calendar.MONTH, month);
            endCal.set(Calendar.DATE, day);

            if (eventTime.equals("early")) {
                startCal.set(Calendar.HOUR_OF_DAY, 8);
                endCal.set(Calendar.HOUR_OF_DAY, 12);
            } else if (eventTime.equals("late")) {
                startCal.set(Calendar.HOUR_OF_DAY, 13);
                endCal.set(Calendar.HOUR_OF_DAY, 17);
            } else if (eventTime.equals("full")) {
                startCal.set(Calendar.HOUR_OF_DAY, 8);
                endCal.set(Calendar.HOUR_OF_DAY, 17);
            } else if (eventTime.equals("community")) {
                startCal.set(Calendar.HOUR_OF_DAY, 17);
                startCal.set(Calendar.MINUTE, 1);
                endCal.set(Calendar.HOUR_OF_DAY, 22);
            } else {
                int startHour = Integer.parseInt(eventTime);
                startCal.set(Calendar.HOUR_OF_DAY, startHour);
                endCal.set(Calendar.HOUR_OF_DAY, (startHour + 1));
            }

            startCal.set(Calendar.MINUTE, 0);
            endCal.set(Calendar.MINUTE, 0);

            event = new Event();
            DateTime start = new DateTime(startCal.getTime(), TimeZone.getTimeZone("UTC"));
            event.setStart(new EventDateTime().setDateTime(start));
            DateTime end = new DateTime(endCal.getTime(), TimeZone.getTimeZone("UTC"));
            event.setEnd(new EventDateTime().setDateTime(end));

            event.setSummary(firmanavn);
            event.setLocation(roomName);
            event.setDescription(besrkivelse);

            EventAttendee room = new EventAttendee();
            room.setDisplayName(roomName);
            room.setEmail("vert@teknologihuset.no");

            List<EventAttendee> attendeeList = new ArrayList<>();
            attendeeList.add(room);

            EventAttendee attendee = new EventAttendee();
            attendee.setDisplayName(firmanavn);
            attendee.setEmail(attendeeEmail);
            attendeeList.add(attendee);

            event.setAttendees(attendeeList);

            GoogleCal googleCal = GoogleCal.getInstance();
            event = googleCal.addEvent(event);
        } catch (NumberFormatException nfe) {
            nfe.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return event;
    }
}
