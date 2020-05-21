package com.reobotetechnology.reobotegame.model;


public class UsuarioModel {

    private int id;
    private String nome;
    private int img;
    private int pontos;
    private int ranking;


    public UsuarioModel(int id, String nome, int img, int pontos, int ranking) {
        this.id = id;
        this.nome = nome;
        this.img = img;
        this.pontos = pontos;
        this.ranking = ranking;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getPontos() {
        return pontos;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }
}