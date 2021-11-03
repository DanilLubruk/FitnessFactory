package com.example.fitnessfactory.data.models;

import com.example.fitnessfactory.utils.TimeUtils;
import com.google.firebase.firestore.Exclude;
import java.util.Date;
import java.util.List;

public class Session {

    public static final String ID_FIELD = "id";
    public static final String DATE_FIELD = "date";
    public static final String START_TIME_FIELD = "startTime";
    public static final String END_TIME_FIELD = "endTime";
    public static final String GYM_NAME_FIELD = "gymName";
    public static final String SESSION_TYPE_NAME_FIELD = "sessionTypeName";
    public static final String COACHES_IDS_FIELD = "coachesIds";
    public static final String CLIENTS_IDS_FIELD = "clientsIds";

    private String id;
    private Date date;
    private Date startTime;
    private Date endTime;
    private String gymName;
    private String sessionTypeName;
    private List<String> coachesIds;
    private List<String> clientsIds;

    public Session() {

    }

    public static boolean isNotNull(Session session) {
        return session != null
                && session.getId() != null
                && session.getDate() != null
                && session.getGymName() != null
                && session.getSessionTypeName() != null
                && session.getCoachesIds() != null
                && session.getClientsIds() != null;
    }

    public void copy(Session session) {
        this.setId(session.getId());
        this.setDate(session.getDate());
        this.setStartTime(session.getStartTime());
        this.setEndTime(session.getEndTime());
        this.setGymName(session.getGymName());
        this.setSessionTypeName(session.getSessionTypeName());
        this.setCoachesIds(session.getCoachesIds());
        this.setClientsIds(session.getClientsIds());
    }

    public boolean equals(Session session) {
        return
                this.getId().equals(session.getId())
                        && this.getDate().equals(session.getDate())
                        && this.getStartTime().equals(session.getStartTime())
                        && this.getEndTime() == session.getEndTime()
                        && this.getGymName().equals(session.getGymName())
                        && this.getSessionTypeName().equals(session.getSessionTypeName())
                        && this.getClientsIds().equals(session.getClientsIds())
                        && this.getCoachesIds().equals(session.getCoachesIds());
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    @Exclude
    public String getDateString() {
        return TimeUtils.dateToLocaleStr(date);
    }

    public void setDate(Date date) {
        this.date = date;
        correctStartEndTimeDay();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
        correctStartEndTimeDay();
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
        correctStartEndTimeDay();
    }

    @Exclude
    public String getEndTimeString() {
        return TimeUtils.dateTo24HoursTime(endTime);
    }

    public String getGymName() {
        return gymName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getSessionTypeName() {
        return sessionTypeName;
    }

    public void setSessionTypeName(String sessionTypeName) {
        this.sessionTypeName = sessionTypeName;
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

    private void correctStartEndTimeDay() {
        if (!TimeUtils.isTheSameDay(getDate(), getStartTime())) {
            setStartTime(TimeUtils.setDatesDay(getDate(), getStartTime()));
        }
        if (!TimeUtils.isTheSameDay(getDate(), getEndTime())) {
            setEndTime(TimeUtils.setDatesDay(getDate(), getEndTime()));
        }
    }
}
