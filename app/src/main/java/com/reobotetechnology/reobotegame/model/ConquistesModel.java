package com.reobotetechnology.reobotegame.model;

public class ConquistesModel {
    private String description;
    private int count;
    private boolean enabled;

    public ConquistesModel(String description, int count, boolean enabled) {
        this.description = description;
        this.count = count;
        this.enabled = enabled;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
