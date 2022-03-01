package com.example.mareu.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class Meeting implements Serializable {

    private Room room;
    private String subject;
    private Date date;
    private List<String> mails;

    public Meeting(Room room, Date date, String subject, List<String> mails) {
        this.room = room;
        this.subject = subject;
        this.date = date;
        this.mails = mails;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public List<String> getMails() {
        return mails;
    }

    public void setMails(List<String> mails) {
        this.mails = mails;
    }

    public String getDateFormatted() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
        return simpleDateFormat.format(date);
    }

    public StringBuilder getTimeFormatted() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        String strTime = dateFormat.format(this.getDate());
        StringBuilder time = new StringBuilder(strTime);
        time.setCharAt(2,'h');
        return time;
    }

    public String getMailsFormatted() {
        StringBuilder result = new StringBuilder();
        for(String str : this.mails) {
            result.append(str).append(", ");
        }
        return result.toString();

    }
}
