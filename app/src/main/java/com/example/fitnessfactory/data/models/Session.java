package com.example.fitnessfactory.data.models;

import com.example.fitnessfactory.utils.TimeUtils;
import com.google.firebase.firestore.Exclude;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Session {

    public static final String ID_FIELD = "id";
    public static final String START_TIME_FIELD = "startTime";
    public static final String END_TIME_FIELD = "endTime";
    public static final String GYM_ID_FIELD = "gymId";
    public static final String SESSION_TYPE_ID_FIELD = "sessionTypeId";
    public static final String COACHES_EMAILS_FIELD = "coachesEmails";
    public static final String CLIENTS_EMAILS_FIELD = "clientsEmails";

    private String id;
    private Date startTime = new Date();
    private Date endTime = new Date();
    private String gymId;
    private String sessionTypeId;
    private List<String> coachesEmails;
    private List<String> clientsEmails;

    public Session() {

    }

    public static boolean isNotNull(Session session) {
        return session != null
                && session.getId() != null
                && session.getGymId() != null
                && session.getSessionTypeId() != null;
    }

    public void copy(Session session) {
        this.setId(session.getId());
        this.setStartTime(session.getStartTime());
        this.setEndTime(session.getEndTime());
        this.setGymId(session.getGymId());
        this.setSessionTypeId(session.getSessionTypeId());
        this.setCoachesEmails(session.getCoachesEmails());
        this.setClientsEmails(session.getClientsEmails());
    }

    public boolean equals(Session session) {
        return
                this.getId().equals(session.getId())
                        && this.getStartTime().equals(session.getStartTime())
                        && this.getEndTime() == session.getEndTime()
                        && this.getGymId().equals(session.getGymId())
                        && this.getSessionTypeId().equals(session.getSessionTypeId());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Exclude
    public String getDateString() {
        return TimeUtils.dateToLocaleStr(getStartTime());
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public void setDate(Date date) {
        setStartTime(TimeUtils.setDatesDay(date, getStartTime()));
        setEndTime(TimeUtils.setDatesDay(date, getEndTime()));
    }

    @Exclude
    public String getStartTimeString() {
        return TimeUtils.dateTo24HoursTime(startTime);
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    @Exclude
    public String getEndTimeString() {
        return TimeUtils.dateTo24HoursTime(endTime);
    }

    public String getGymId() {
        return gymId;
    }

    public void setGymId(String gymId) {
        this.gymId = gymId;
    }

    public String getSessionTypeId() {
        return sessionTypeId;
    }

    public void setSessionTypeId(String sessionTypeId) {
        this.sessionTypeId = sessionTypeId;
    }

    public List<String> getCoachesEmails() {
        return coachesEmails;
    }

    public void setCoachesEmails(List<String> coachesEmails) {
        this.coachesEmails = coachesEmails;
    }

    public List<String> getClientsEmails() {
        return clientsEmails;
    }

    public void setClientsEmails(List<String> clientsEmails) {
        this.clientsEmails = clientsEmails;
    }
}
