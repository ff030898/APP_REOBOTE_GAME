package com.reobotetechnology.reobotegame.model;

public class PostModel {

    private int id, amei, tipo;
    private UsuarioModel user;
    private String imagem, titulo, desc;
    private String data, hora;

    public PostModel(int id, int amei, int tipo, UsuarioModel user, String imagem, String titulo, String desc, String data, String hora) {
        this.id = id;
        this.amei = amei;
        this.tipo = tipo;
        this.user = user;
        this.imagem = imagem;
        this.titulo = titulo;
        this.desc = desc;
        this.data = data;
        this.hora = hora;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAmei() {
        return amei;
    }

    public void setAmei(int amei) {
        this.amei = amei;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public UsuarioModel getUser() {
        return user;
    }

    public void setUser(UsuarioModel user) {
        this.user = user;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }
}
