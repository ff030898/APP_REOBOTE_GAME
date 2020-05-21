package com.reobotetechnology.reobotegame.model;

public class VersiculosModel {
    private int id,livro,capitulo,verso;
    private String text;

    public VersiculosModel(int id, int livro, int capitulo, int verso, String text) {
        this.id = id;
        this.livro = livro;
        this.capitulo = capitulo;
        this.verso = verso;
        this.text = text;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLivro() {
        return livro;
    }

    public void setLivro(int livro) {
        this.livro = livro;
    }

    public int getCapitulo() {
        return capitulo;
    }

    public void setCapitulo(int capitulo) {
        this.capitulo = capitulo;
    }

    public int getVerso() {
        return verso;
    }

    public void setVerso(int verso) {
        this.verso = verso;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return "VersiculosModel{" +
                "capitulo=" + capitulo +
                ", verso=" + verso +
                ", text='" + text + '\'' +
                '}';
    }
}
