package com.reobotetechnology.reobotegame.model;

import java.util.Date;

public class PostModel {

    private int id, imagem, amei, comentarios, compartilhamentos;
    private UsuarioModel user;
    private String desc, link;
    private Date data;

    public PostModel(int id, int imagem, int amei, int comentarios, int compartilhamentos, UsuarioModel user, String desc, String link, Date data) {
        this.id = id;
        this.imagem = imagem;
        this.amei = amei;
        this.comentarios = comentarios;
        this.compartilhamentos = compartilhamentos;
        this.user = user;
        this.desc = desc;
        this.link = link;
        this.data = data;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public int getAmei() {
        return amei;
    }

    public void setAmei(int amei) {
        this.amei = amei;
    }

    public int getComentarios() {
        return comentarios;
    }

    public void setComentarios(int comentarios) {
        this.comentarios = comentarios;
    }

    public int getCompartilhamentos() {
        return compartilhamentos;
    }

    public void setCompartilhamentos(int compartilhamentos) {
        this.compartilhamentos = compartilhamentos;
    }

    public UsuarioModel getUser() {
        return user;
    }

    public void setUser(UsuarioModel user) {
        this.user = user;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
}
