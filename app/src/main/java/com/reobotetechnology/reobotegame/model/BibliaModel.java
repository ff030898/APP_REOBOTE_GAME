package com.reobotetechnology.reobotegame.model;

public class BibliaModel {
    private int id,livro,capitulo,verso;
    private String text;
    private boolean selected;

    public BibliaModel(int id, int livro, int capitulo, int verso, String text, boolean selected) {
        this.id = id;
        this.livro = livro;
        this.capitulo = capitulo;
        this.verso = verso;
        this.text = text;
        this.selected = selected;
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

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
