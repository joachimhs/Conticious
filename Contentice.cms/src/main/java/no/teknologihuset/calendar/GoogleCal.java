package no.teknologihuset.calendar;

import com.google.api.client.auth.oauth2.Credential;
import
        com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import
        com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import
        com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.batch.json.JsonBatchCallback;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.client.http.HttpHeaders;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.DateTime;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.calendar.model.*;
import com.google.api.services.calendar.model.Calendar;
import com.google.gson.Gson;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;


import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;


/**
 * Created with IntelliJ IDEA.
 * User: joahaage
 * Date: 14.10.13
 * Time: 13:52
 * To change this template use File | Settings | File Templates.
 */
public class GoogleCal {
    private static final Logger logger = Logger.getLogger(GoogleCal.class.getName());

    private final String APPLICATION_NAME = "";
    private final java.io.File DATA_STORE_DIR = new java.io.File("/srv/contentice/calendar_sample");
    private FileDataStoreFactory dataStoreFactory;
    private HttpTransport httpTransport;
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private final List<Calendar> addedCalendarsUsingBatch = Lists.newArrayList();
    private static final List<String> SCOPES = Arrays.asList(
            "https://www.googleapis.com/auth/userinfo.profile",
            "https://www.googleapis.com/auth/userinfo.email",
            "https://www.googleapis.com/auth/calendar"
    );

    private Hashtable<String, CalendarRoom> rooms = new Hashtable<String, CalendarRoom>();


    private com.google.api.services.calendar.Calendar client;

    private Long calendarLastFetched = null;

    private static GoogleCal instance = null;

    private GoogleCal() {
    }

    public static GoogleCal getInstance() {
        if (instance == null) {
            instance = new GoogleCal();
        }

        return instance;
    }

    /**
     * Authorizes the installed application to access user's protected data.
     */
    private Credential authorize() throws Exception {
        // load client secrets
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY,
                new InputStreamReader(GoogleCal.class.getResourceAsStream("/client_secrets.json")));
        if (clientSecrets.getDetails().getClientId().startsWith("Enter")
                || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
            logger.info(
                    "Enter Client ID and Secret from https://code.google.com/apis/console/?api=calendar "
                            + "into calendar-cmdline-sample/src/main/resources/client_secrets.json");
            System.exit(1);
        }

        // set up authorization code flow
        GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                httpTransport, JSON_FACTORY, clientSecrets,
                SCOPES).setDataStoreFactory(dataStoreFactory)
                .build();
        // authorize
        return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
    }

    private void fetchCalendar() {
        logger.info("Fetching Calendar from Google");
        try {
            // initialize the transport
            httpTransport = GoogleNetHttpTransport.newTrustedTransport();


            // initialize the data store factory
            dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);


            // authorization
            Credential credential = authorize();


            // set up global Calendar instance
            client = new com.google.api.services.calendar.Calendar.Builder(
                    httpTransport, JSON_FACTORY, credential).setApplicationName(APPLICATION_NAME).build();


            // run commands
            CalendarListEntry calendar = getCalendar("Teknologihuset");
            logger.info("Found Calendar: " + calendar.getSummary() + " with id: " + calendar.getId());
            Events events = showEvents(calendar.getId());
            for (Event event : events.getItems()) {
                if (event.getStart() != null && event.getStart().getDateTime() != null && event.getEnd().getDateTime() != null && event.getAttendees() != null && event.getAttendees().size() > 0 &&
                        roomIsAtTeknologihuset(event.getAttendees())) {

                    CalendarRoom calendarRoom = rooms.get(event.getLocation());
                    if (calendarRoom == null) {
                        calendarRoom = new CalendarRoom(event.getLocation());
                        rooms.put(event.getLocation(), calendarRoom);
                    }

                    DateTime dateTime = event.getStart().getDateTime();
                    java.util.Calendar startCal = GregorianCalendar.getInstance();
                    startCal.setTimeInMillis(dateTime.getValue());

                    dateTime = event.getEnd().getDateTime();
                    java.util.Calendar endCal = GregorianCalendar.getInstance();
                    endCal.setTimeInMillis(dateTime.getValue());

                    Integer weekNum = startCal.get(java.util.Calendar.WEEK_OF_YEAR);
                    Integer monthNum = startCal.get(java.util.Calendar.MONTH) +1;
                    Integer dayOfWeek = startCal.get(java.util.Calendar.DAY_OF_WEEK) -1;
                    Integer dayOfMonth = startCal.get(java.util.Calendar.DAY_OF_MONTH);
                    Integer yearNum = startCal.get(java.util.Calendar.YEAR);

                    RoomWeek roomWeek = calendarRoom.getRoomWeek(weekNum);
                    if (roomWeek == null) {
                        roomWeek = new RoomWeek(calendarRoom.getId() + ";" + yearNum + ";" + weekNum, weekNum, monthNum, yearNum);
                        calendarRoom.addRoomWeek(roomWeek);
                    }
                    RoomDay roomDay = roomWeek.getRoomDay(dayOfWeek);
                    if (roomDay == null) {
                        //RoomDay(String id, Integer dayOfWeek, Integer dayOfMonth, Integer roomWeek, Integer roomYear, Integer roomMonth) {
                        roomDay = new RoomDay(calendarRoom.getId() + ";" + yearNum + ";" + weekNum + ";" + dayOfWeek, dayOfWeek, dayOfMonth, weekNum, yearNum, monthNum);
                        roomWeek.addRoomDay(roomDay);
                    }

                    Integer startHourOfDay = startCal.get(java.util.Calendar.HOUR_OF_DAY);
                    Integer endHourOfDay = endCal.get(java.util.Calendar.HOUR_OF_DAY);

                    for (int hour = startHourOfDay; hour < endHourOfDay; hour++) {
                        String eventId = roomWeek.getId() + ";" + dayOfWeek + ";" + hour;
                        RoomEvent roomEvent = new RoomEvent(eventId, event.getId(), hour, dayOfMonth, monthNum, event.getSummary(),  startCal.getTime(), endCal.getTime(), event.getDescription());
                        roomDay.getRoomEvents().add(roomEvent);
                    }

                    if (event.getRecurrence() != null) {
                        for (String recurrence : event.getRecurrence()) {
                            logger.info("\t\t" + recurrence);
                        }
                    }
                }
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public CalendarRooms getTeknologihusetCalendarRooms() {
        CalendarRooms calendarRooms = null;

        if (calendarLastFetched == null || (System.currentTimeMillis() - calendarLastFetched) > (1000 * 60 * 10)) {
            fetchCalendar();

            if (rooms.size() > 0) {
                calendarLastFetched = System.currentTimeMillis();
            }
        }

        List<CalendarRoom> calendarRoomList = new ArrayList<CalendarRoom>();
        for (CalendarRoom r : rooms.values()) {
            calendarRoomList.add(r);
        }


        logger.info("------<>-------");
        calendarRooms = new CalendarRooms();
        calendarRooms.setCalendarRooms(calendarRoomList);
        logger.info(new Gson().toJson(calendarRooms));

        return calendarRooms;
    }

    public CalendarRoom getCalendarRoom(String roomName) {
        CalendarRoom calendarRoom = null;

        for (CalendarRoom calRoom : getTeknologihusetCalendarRooms().getCalendarRooms()) {
            if (calRoom.getId().equals(roomName)) {
                calendarRoom = calRoom;
                break;
            }
        }

        return calendarRoom;
    }

    private boolean roomIsAtTeknologihuset(List<EventAttendee> eventAttendees) {
        boolean isAtTeknolgihuset = false;

        for (EventAttendee attendee : eventAttendees) {
            if (isAttendeIsAtTeknologihuset(attendee.getDisplayName())) {
                isAtTeknolgihuset = true;
                break;
            }
        }

        return isAtTeknolgihuset;
    }

    private boolean isAttendeIsAtTeknologihuset(String attendee) {
        return attendee != null && (attendee.contains("Big Conference Room")
                || attendee.contains("BoardRoom")
                || attendee.contains("GameRoom")
                || attendee.contains("GreenRoom")
                || attendee.contains("RegularRoom")
                || attendee.contains("Small Conference Room"));
    }


    private CalendarListEntry getCalendar(String calName) throws IOException {
        CalendarListEntry foundCalendar = null;


        CalendarList feed = client.calendarList().list().execute();
        for (CalendarListEntry entry : feed.getItems()) {
            if (entry.getSummary().equals(calName)) {
                foundCalendar = entry;
                break;
            }


        }


        return foundCalendar;
    }


    private void addCalendarsUsingBatch() throws IOException {
        BatchRequest batch = client.batch();


        // Create the callback.
        JsonBatchCallback<Calendar> callback = new JsonBatchCallback<Calendar>() {


            @Override
            public void onSuccess(Calendar calendar, HttpHeaders responseHeaders) {
                addedCalendarsUsingBatch.add(calendar);
            }


            @Override
            public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                logger.info("Error Message: " + e.getMessage());
            }
        };


        // Create 2 Calendar Entries to insert.
        Calendar entry1 = new Calendar().setSummary("Calendar for Testing 1");
        client.calendars().insert(entry1).queue(batch, callback);


        Calendar entry2 = new Calendar().setSummary("Calendar for Testing 2");
        client.calendars().insert(entry2).queue(batch, callback);


        batch.execute();
    }


    private Calendar addCalendar() throws IOException {
        Calendar entry = new Calendar();
        entry.setSummary("Calendar for Testing 3");
        Calendar result = client.calendars().insert(entry).execute();
        return result;
    }


    private Calendar updateCalendar(Calendar calendar) throws IOException {
        Calendar entry = new Calendar();
        entry.setSummary("Updated Calendar for Testing");
        Calendar result = client.calendars().patch(calendar.getId(), entry).execute();
        return result;
    }


    private void addEvent(Calendar calendar) throws IOException {
        Event event = newEvent();
        Event result = client.events().insert(calendar.getId(), event).execute();
    }


    private Event newEvent() {
        Event event = new Event();
        event.setSummary("New Event");
        Date startDate = new Date();
        Date endDate = new Date(startDate.getTime() + 3600000);
        DateTime start = new DateTime(startDate, TimeZone.getTimeZone("UTC"));
        event.setStart(new EventDateTime().setDateTime(start));
        DateTime end = new DateTime(endDate, TimeZone.getTimeZone("UTC"));
        event.setEnd(new EventDateTime().setDateTime(end));
        return event;
    }


    private Events showEvents(String calendarId) throws IOException {
        return client.events().list(calendarId).execute();
    }


    private void deleteCalendarsUsingBatch() throws IOException {
        BatchRequest batch = client.batch();
        for (Calendar calendar : addedCalendarsUsingBatch) {
            client.calendars().delete(calendar.getId()).queue(batch, new JsonBatchCallback<Void>() {


                @Override
                public void onSuccess(Void content, HttpHeaders responseHeaders) {
                    logger.info("Delete is successful!");
                }


                @Override
                public void onFailure(GoogleJsonError e, HttpHeaders responseHeaders) {
                    logger.info("Error Message: " + e.getMessage());
                }
            });
        }


        batch.execute();
    }


    private void deleteCalendar(Calendar calendar) throws IOException {
        client.calendars().delete(calendar.getId()).execute();
    }











}
