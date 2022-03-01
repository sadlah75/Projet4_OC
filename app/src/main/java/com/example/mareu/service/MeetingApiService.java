package com.example.mareu.service;

import com.example.mareu.model.Meeting;
import com.example.mareu.model.Room;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 *  Meeting API client
 */
public interface MeetingApiService {

    /**
     * Get all my Meetings
     * @return {@link List}
     */
    List<Meeting> getMeetings();

    /**
     * Create a meeting
     * @param meeting
     */
    void createMeeting(Meeting meeting);

    /**
     * Deletes a meeting
     * @param meeting
     */
    void deleteMeeting(Meeting meeting);

    /**
     * Get all my rooms
     * @return {@link List}
     */
    List<Room> getAllRooms();

    /**
     * Filter meetings by room
     * @return {@link ArrayList}
     * @param name
     */
    List<Meeting> getMeetingsFilteredByRoom(String name);

    /**
     * Filter meetings by time
     * @return {@link ArrayList}
     * @param date
     */
    List<Meeting> getMeetingsFilteredByDate(String date);

    /**
     * Get a room by name
     * @return {@link Room}
     *
     */
    Room getRoomByName(String name);

}
