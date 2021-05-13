package com.reobotetechnology.reobotegame.model;

public class SettingsModel {
    private int id;
    private String text;

    public SettingsModel(int id, String text) {
        this.id = id;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
