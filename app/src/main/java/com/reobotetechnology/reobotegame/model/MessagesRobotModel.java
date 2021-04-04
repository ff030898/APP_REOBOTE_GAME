package com.reobotetechnology.reobotegame.model;

public class MessagesRobotModel {

    private int id;
    private String mensagem;

    public MessagesRobotModel(int id, String mensagem) {
        this.id = id;
        this.mensagem = mensagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }
}
