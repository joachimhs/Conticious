package no.teknologihuset.handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import no.haagensoftware.contentice.handler.ContenticeHandler;
import no.teknologihuset.calendar.*;
import no.teknologihuset.epost.EpostExecutor;
import no.teknologihuset.handlers.data.RoomDayAssembler;
import no.teknologihuset.handlers.data.RoomDayJson;
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
public class RoomDaysHandler extends ContenticeHandler {
    private static final Logger logger = Logger.getLogger(RoomDaysHandler.class.getName());

    private GoogleCal googleCal;
    private List<String> validRooms;

    public RoomDaysHandler() {
        googleCal = GoogleCal.getInstance();
        validRooms = new ArrayList<>();
        validRooms.add("Big Conference Room");
        validRooms.add("BoardRoom");
        validRooms.add("GameRoom");
        validRooms.add("GreenRoom");
        validRooms.add("RegularRoom");
        validRooms.add("Small Conference Room");
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, FullHttpRequest fullHttpRequest) throws Exception {
        //Ensure that the email sending is always started...
        EpostExecutor.getInstance(getDomain().getWebappName()).sendRemainingEmails(getStorage());

        String jsonResponse = "";

        String dateStr = getParameter("date");

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

        List<RoomDay> roomDays = new ArrayList<>();

        if (inputDate != null) {
            Calendar dateCal = Calendar.getInstance();
            dateCal.setTime(inputDate);

            for (String roomName : validRooms) {
                CalendarRoom calRoom = googleCal.getCalendarRoom(roomName);
                if (calRoom != null) {
                    RoomDay roomDay = calRoom.getRoomDay(dateCal.getTime());

                    if (roomDay != null) {
                        logger.info("\t\tFound Room Day: " + roomDay.getId());
                    }

                    if (roomDay == null) {
                        roomDay = new RoomDay(roomName + "_" + sdf.format(dateCal.getTime()), dateCal.getTime(), roomName);
                    }


                    roomDays.add(roomDay);
                }
            }

        }

        JsonObject topLevelObject = new JsonObject();

        topLevelObject = RoomDayAssembler.assembleRoomDay(roomDays);

        jsonResponse = topLevelObject.toString();

        if (jsonResponse.length() == 0) {
            jsonResponse = "{}";
        }

        writeContentsToBuffer(channelHandlerContext, jsonResponse, "application/json");
    }
}