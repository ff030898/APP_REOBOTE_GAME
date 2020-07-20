package com.reobotetechnology.reobotegame.model;

public class Notification extends Message {

    private String fromName;
    private String fromImage;
    private String tipo;
    private String idPartida;

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public String getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(String idPartida) {
        this.idPartida = idPartida;
    }

    public String getFromImage() {
        return fromImage;
    }

    public void setFromImage(String fromImage) {
        this.fromImage = fromImage;
    }
}