package com.reobotetechnology.reobotegame.model;

public class CommentBlogPostModel {

    private UserModel user;
    private String text;
    private String date;
    private String time;
    private FollowBlogPostModel follow;

    public CommentBlogPostModel() {
    }

    public CommentBlogPostModel(UserModel user, String text, String date, String time, FollowBlogPostModel follow) {
        this.user = user;
        this.text = text;
        this.date = date;
        this.time = time;
        this.follow = follow;
    }

    public UserModel getUser() {
        return user;
    }

    public void setUser(UserModel user) {
        this.user = user;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
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

    public FollowBlogPostModel getFollow() {
        return follow;
    }

    public void setFollow(FollowBlogPostModel follow) {
        this.follow = follow;
    }
}
