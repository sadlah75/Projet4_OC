package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public abstract class DummyMeetingGenerator {

    public static List<Room> DUMMY_ROOMS  = Arrays.asList(
            new Room("Room A", "#40a8db"),
            new Room("Room B", "#97ebdb"),
            new Room("Room C", "#FA8072"),
            new Room("Room D", "#B22222"),
            new Room("Room E", "#00FF7F"),
            new Room("Room F", "#32CD32"),
            new Room("Room G", "#808000"),
            new Room("Room H", "#DEB887"),
            new Room("Room I", "#DAA520"),
            new Room("Room J", "#F4A460")
    );

    public static List<Meeting> generateMeetings() {
        return new ArrayList<>(DUMMY_MEETINGS);
    }

    public static List<Room> generateRooms() {
        return new ArrayList<>(DUMMY_ROOMS);
    }

    public final static List<String> mails1 = Arrays.asList("sadek@lamzone.com","jean@lamzone.com");
    public final static List<String> mails2 = Arrays.asList("josh@lamzone.com","thierry@lamzone.com");
    public final static List<String> mails3 = Arrays.asList("sam@lamzone.com",
            "greg@lamzone.com","said@lamzone.com");
    public final static List<String> mails4 = Arrays.asList("sendhil@lamzone.com","alain@lamzone.com");
    public final static List<String> mails5 = Arrays.asList("simon@lamzone.com","sebastien@lamzone.com");



    public static Date mDate1 = generateDate(2022,03,22,15,12);
    public static Date mDate2 = generateDate(2022,03,18,15,12);
    public static Date mDate3 = generateDate(2022,03,19,15,12);
    public static Date mDate4 = generateDate(2022,03,19,17,10);
    public static Date mDate5 = generateDate(2022,03,20,17,20);
    public static Date mDate6 = generateDate(2022,03,21,18,15);
    public static Date mDate7 = generateDate(2022,03,02,16,25);
    public static Date mDate8 = generateDate(2022,03,10,15,12);
    public static Date mDate9 = generateDate(2022,03,17,05,53);
    public static Date mDate10 = generateDate(2022,04,20,9,45);
    public static Date mDate11= generateDate(2022,06,07,10,12);
    public static Date mDate12= generateDate(2022,06,11,9,30);


    public static Date generateDate(int year,int month, int day, int hour, int minute) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month-1, day, hour, minute);
        return mCalendar.getTime();
    }

    public static List<Meeting> DUMMY_MEETINGS = Arrays.asList(
            new Meeting(DUMMY_ROOMS.get(6), mDate1,"Subject 16",mails4),
            new Meeting(DUMMY_ROOMS.get(0), mDate2,"Subject 1",mails1),
            new Meeting(DUMMY_ROOMS.get(1),mDate3,"Subject 2",mails2),
            new Meeting(DUMMY_ROOMS.get(2), mDate4,"Subject 3",mails2),
            new Meeting(DUMMY_ROOMS.get(3), mDate5,"Subject 4",mails4),
            new Meeting(DUMMY_ROOMS.get(4), mDate6,"Subject 5",mails1),
            new Meeting(DUMMY_ROOMS.get(5), mDate7,"Subject 6",mails2),
            new Meeting(DUMMY_ROOMS.get(6), mDate8,"Subject 7",mails3)
            /*
            new Meeting(DUMMY_ROOMS.get(5), mDate9,"Subject 8",mails4),
            new Meeting(DUMMY_ROOMS.get(4), mDate10,"Subject 9",mails1),
            new Meeting(DUMMY_ROOMS.get(8), mDate11,"Subject 10",mails2),
            new Meeting(DUMMY_ROOMS.get(5), mDate12,"Subject 15",mails1)
            */
    );
}