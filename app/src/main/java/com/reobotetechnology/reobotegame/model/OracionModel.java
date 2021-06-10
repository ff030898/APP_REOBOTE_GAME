package com.reobotetechnology.reobotegame.model;

public class OracionModel {
    private String id;
    private String title;
    private String description;
    private String date_after;
    private String date_before;
    private String time;
    private String status;
    private int appointments;

    public OracionModel(String id, String title, String description, String date_after, String date_before, String time, String status, int appointments) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.date_after = date_after;
        this.date_before = date_before;
        this.time = time;
        this.status = status;
        this.appointments = appointments;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate_after() {
        return date_after;
    }

    public void setDate_after(String date_after) {
        this.date_after = date_after;
    }

    public String getDate_before() {
        return date_before;
    }

    public void setDate_before(String date_before) {
        this.date_before = date_before;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAppointments() {
        return appointments;
    }

    public void setAppointments(int appointments) {
        this.appointments = appointments;
    }
}
