package com.reobotetechnology.reobotegame.model;

public class BooksOfBibleModel {

    private int id, testamento, learning;
    private String nome;
    private boolean favorited;

    public BooksOfBibleModel(int id, int testamento, int learning, String nome, boolean favorited) {
        this.id = id;
        this.testamento = testamento;
        this.learning = learning;
        this.nome = nome;
        this.favorited = favorited;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTestamento() {
        return testamento;
    }

    public void setTestamento(int testamento) {
        this.testamento = testamento;
    }

    public int getLearning() {
        return learning;
    }

    public void setLearning(int learning) {
        this.learning = learning;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean isFavorited() {
        return favorited;
    }

    public void setFavorited(boolean favorited) {
        this.favorited = favorited;
    }
}
