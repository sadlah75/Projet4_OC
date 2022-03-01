package com.example.mareu;

import static com.example.mareu.service.DummyMeetingGenerator.mails1;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

import android.util.Log;

import com.example.mareu.di.DI;
import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;
import com.example.mareu.service.DummyMeetingGenerator;
import com.example.mareu.service.MeetingApiService;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {

    private MeetingApiService service;
    private List<Meeting> meetings;
    private List<Room> rooms;
    private Calendar calendar = Calendar.getInstance();
    private Date currentDate = calendar.getTime();

    @Before
    public void setup() {
        service = DI.getNewInstanceApiService();
        meetings = service.getMeetings();
        rooms = service.getAllRooms();
    }

    /**
     * We create and add a meeting and check that the list contains it
     */
    @Test
    public void addMeetingWithSuccess() {
        Meeting meetingToAdd = new Meeting(rooms.get(0),
                currentDate,"Room T",mails1);
        service.createMeeting(meetingToAdd);
        assertTrue(meetings.contains(meetingToAdd));
    }

    /**
     * We delete a meeting and we check that te list not contains it
     */
    @Test
    public void deleteMeetingWithsuccess() {
        Meeting meetingToRemove = meetings.get(0);
        service.deleteMeeting(meetingToRemove);
        assertFalse(meetings.contains(meetingToRemove));
    }

    /**
     * 1. we create and add a new meeting with nam's room equals "Room F" [index 5]
     * 2. We filter the meetings list by room
     * 3. and check that the meetings filtered have expected
     */
    @Test
    public void meetingFilteredByRoom_WithSuccess() {
        String nameRoom = "Room F";
        Meeting meeting = new Meeting(rooms.get(5),currentDate,"Filter by room",mails1);
        service.createMeeting(meeting);
        List<Meeting> meetingsFilteredByRoom = service.getMeetingsFilteredByRoom(nameRoom);
        assertEquals(2,meetingsFilteredByRoom.size());
        for(Meeting current : meetingsFilteredByRoom) {
            assertEquals(nameRoom,current.getRoom().getName());
        }
    }


    /**
     * 1. we create and add a new meeting with a date equals "18/03/2022"
     * 2. We filter the meetings list by date
     * 3. and check that the meetings filtered have expected
     */
    @Test
    public void meetingFilteredByDate_WithSuccess() {
        String dateToCompare = "19/03/2022";
        calendar.set(2022,2,19);
        Date dateToAdd = calendar.getTime();

        Meeting meetingToAdd = new Meeting(rooms.get(0),dateToAdd,"Filter by date",mails1);
        service.createMeeting(meetingToAdd);

        // meetingsFilteredByDate.size() = 2 + 1
        List<Meeting> meetingsFilteredByDate = service.getMeetingsFilteredByDate(dateToCompare);
        assertEquals(3,meetingsFilteredByDate.size());
        for(Meeting current : meetingsFilteredByDate) {
            assertEquals(dateToCompare,current.getDateFormatted());
        }

    }
}