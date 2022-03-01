package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.List;

public class DummyMeetingApiService implements MeetingApiService{

    private List<Meeting> meetings = DummyMeetingGenerator.generateMeetings();
    private List<Room> rooms = DummyMeetingGenerator.generateRooms();


    @Override
    public List<Meeting> getMeetings() {
        return meetings;
    }

    @Override
    public void createMeeting(Meeting meeting) {
        meetings.add(meeting);
    }

    @Override
    public void deleteMeeting(Meeting meeting) {
        meetings.remove(meeting);
    }

    @Override
    public List<Room> getAllRooms() {
        return rooms;
    }

    @Override
    public List<Meeting> getMeetingsFilteredByRoom(String name) {
        List<Meeting> meetingsListByRoom = new ArrayList<>();
        for (Meeting m : meetings) {
            if(m.getRoom().getName().equals(name)) {
                meetingsListByRoom.add(m);
            }
        }
        return meetingsListByRoom;
    }

    @Override
    public List<Meeting> getMeetingsFilteredByDate(String date) {
        List<Meeting> meetingsListByDate = new ArrayList<>();
        for (Meeting m: meetings) {
            if(m.getDateFormatted().equals(date)) {
                meetingsListByDate.add(m);
            }
        }
        return meetingsListByDate;
    }

    @Override
    public Room getRoomByName(String name) {
        for(Room room : rooms) {
            if(room.getName().equals(name)) {
                return room;
            }
        }
        return null;
    }
}
