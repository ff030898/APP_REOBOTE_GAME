package com.reobotetechnology.reobotegame.model;

public class CommentModel {
    private String id_user;
    private String description;
    private String date;
    private String time;
    private int like;

    public CommentModel(String id_user, String description, String date, String time, int like) {
        this.id_user = id_user;
        this.description = description;
        this.date = date;
        this.time = time;
        this.like = like;
    }

    public String getId_user() {
        return id_user;
    }

    public void setId_user(String id_user) {
        this.id_user = id_user;
    }


    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }
}
