package com.example.fitnessfactory.data.views;

import com.example.fitnessfactory.data.models.Session;

import java.util.Date;

public class SessionView {

    private Session session;
    private String sessionTypeName;
    private String gymName;

    public SessionView(Session session) {
        this.session = session;
    }

    public void setSessionTypeName(String sessionTypeName) {
        this.sessionTypeName = sessionTypeName;
    }

    public String getSessionTypeName() {
        return sessionTypeName;
    }

    public void setGymName(String gymName) {
        this.gymName = gymName;
    }

    public String getGymName() {
        return gymName;
    }

    public String getId() {
        return session.getId();
    }

    public void setId(String id) {
        session.setId(id);
    }

    public Date getDate() {
        return session.getDate();
    }

    public String getDateString() {
        return session.getDateString();
    }

    public void setDate(Date date) {
        session.setDate(date);
    }

    public Date getStartTime() {
        return session.getStartTime();
    }

    public void setStartTime(Date startTime) {
        session.setStartTime(startTime);
    }

    public String getStartTimeString() {
        return session.getStartTimeString();
    }

    public Date getEndTime() {
        return session.getEndTime();
    }

    public void setEndTime(Date endTime) {
        session.setEndTime(endTime);
    }

    public String getEndTimeString() {
        return session.getEndTimeString();
    }

    public String getGymId() {
        return session.getGymId();
    }

    public void setGymId(String gymId) {
        session.setGymId(gymId);
    }

    public String getSessionTypeId() {
        return session.getSessionTypeId();
    }

    public void setSessionTypeId(String sessionTypeId) {
        session.setSessionTypeId(sessionTypeId);
    }

    public Session getSession() {
        return session;
    }
}
