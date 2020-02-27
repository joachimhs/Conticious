package no.teknologihuset.handlers;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.teknologihuset.calendar.CalendarRoom;
import no.teknologihuset.calendar.GoogleCal;
import no.teknologihuset.calendar.RoomDay;
import no.teknologihuset.calendar.RoomEvent;
import no.teknologihuset.epost.EpostExecutor;
import no.teknologihuset.handlers.data.RoomDayAssembler;
import org.apache.log4j.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by jhsmbp on 12/9/13.
 */
public class CommunityEventsHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(CommunityEventsHandler.class.getName());

    private GoogleCal googleCal;

    public CommunityEventsHandler() {
        googleCal = GoogleCal.getInstance();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //Ensure that the email sending is always started...
        EpostExecutor.getInstance(getDomain().getWebappName()).sendRemainingEmails(getStorage());

        String jsonResponse = "";

        String dateStr = getParameter("date");
        String monthStr = getParameter("month");
        String yearStr = getParameter("year");

        logger.info("input date: " + dateStr);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        Date inputDate = null;

        if (dateStr != null) {
            try {
                inputDate = sdf.parse(dateStr);
            } catch (ParseException pe) {
                logger.error("Unable to parse input date: " + dateStr);
                inputDate = null;
            }
        }

        JsonArray roomEventArray = new JsonArray();

        if (inputDate != null) {
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(inputDate);

            Integer month= dateCal.get(Calendar.MONTH);
            Integer year = dateCal.get(Calendar.YEAR);
            Integer dayOfMonth = dateCal.get(Calendar.DAY_OF_MONTH);

            for (RoomEvent re : googleCal.getCommunityEvents()) {
                Calendar eventCal = Calendar.getInstance();
                eventCal.setTime(re.getStart());

                if (year.equals(eventCal.get(Calendar.YEAR)) && month.equals(eventCal.get(Calendar.MONTH)) && dayOfMonth.equals(eventCal.get(Calendar.DAY_OF_MONTH))) {
                    roomEventArray.add(new Gson().toJsonTree(re));
                }

            }

        } else if (monthStr != null && yearStr != null) {
            Integer month = null;
            Integer year = null;

            try {
                month = Integer.parseInt(monthStr);
                month--; //months here are 0-based, but 1-based in the API
                year = Integer.parseInt(yearStr);
            } catch (NumberFormatException nfe) {
                month = null;
                year = null;
            }

            if (month >= 0 && year > 2014) {
                for (RoomEvent re : googleCal.getCommunityEvents()) {
                    Calendar eventCal = Calendar.getInstance();
                    eventCal.setTime(re.getStart());

                    if (year.equals(eventCal.get(Calendar.YEAR)) && month.equals(eventCal.get(Calendar.MONTH))) {
                        roomEventArray.add(new Gson().toJsonTree(re));
                    }

                }
            }
        }

        JsonObject topLevelObject = new JsonObject();

        topLevelObject.add("communityEvents", roomEventArray);

        jsonResponse = topLevelObject.toString();

        if (jsonResponse.length() == 0) {
            jsonResponse = "{}";
        }

        writeContentsToBuffer(channelHandlerContext, jsonResponse, "application/json");
    }
}