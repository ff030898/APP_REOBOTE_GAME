package com.reobotetechnology.reobotegame.model;

public class BooksOfBibleModel {

    private int id, testamento, learning;
    private String nome;

    public BooksOfBibleModel(int id, int testamento, int learning, String nome) {
        this.id = id;
        this.testamento = testamento;
        this.learning = learning;
        this.nome = nome;
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
}
