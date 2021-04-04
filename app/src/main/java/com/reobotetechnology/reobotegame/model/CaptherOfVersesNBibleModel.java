package com.reobotetechnology.reobotegame.model;

public class CaptherOfVersesNBibleModel {

    private int id, capVerso;


    public CaptherOfVersesNBibleModel(int id, int capVerso) {
        this.id = id;
        this.capVerso = capVerso;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCapVerso() {
        return capVerso;
    }

    public void setCapVerso(int capVerso) {
        this.capVerso = capVerso;
    }
}
