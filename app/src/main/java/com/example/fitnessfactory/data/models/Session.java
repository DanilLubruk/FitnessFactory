package com.example.fitnessfactory.data.models;

import com.google.firebase.Timestamp;

import java.util.Date;
import java.util.List;

public class Session {

    public static final String ID_FIELD = "id";
    public static final String DATE_FIELD = "date";
    public static final String START_TIME_FIELD = "startTime";
    public static final String END_TIME_FIELD = "endTime";
    public static final String GYM_ID_FIELD = "gymId";
    public static final String SESSION_TYPE_ID = "sessionTypeId";
    public static final String COACHES_IDS_FIELD = "coachesIds";
    public static final String CLIENTS_IDS_FIELD = "clientsIds";

    private String id;
    private Timestamp date;
    private int startTime;
    private int endTime;
    private String gymId;
    private String sessionTypeId;
    private List<String> coachesIds;
    private List<String> clientsIds;

    public Session() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date.toDate();
    }

    public void setDate(Timestamp date) {
        this.date = date;
    }

    public int getStartTime() {
        return startTime;
    }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
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

    public List<String> getCoachesIds() {
        return coachesIds;
    }

    public void setCoachesIds(List<String> coachesIds) {
        this.coachesIds = coachesIds;
    }

    public List<String> getClientsIds() {
        return clientsIds;
    }

    public void setClientsIds(List<String> clientsIds) {
        this.clientsIds = clientsIds;
    }
}
