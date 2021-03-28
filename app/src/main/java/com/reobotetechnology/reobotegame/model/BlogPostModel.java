package com.reobotetechnology.reobotegame.model;

public class BlogPostModel {

    private int id;
    private String title;
    private String description;
    private String time;
    private int visualizations;

    public BlogPostModel(int id, String title, String description, String time, int visualizations) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.visualizations = visualizations;

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getVisualizations() {
        return visualizations;
    }

    public void setVisualizations(int visualizations) {
        this.visualizations = visualizations;
    }
}
