package com.reobotetechnology.reobotegame.model;

public class BooksOfBibleModel {

    private int id, testamento;
    private String nome;

    public BooksOfBibleModel(int id, int testamento, String nome) {
        this.id = id;
        this.testamento = testamento;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    @Override
    public String toString() {
        return "BibliaModel{" +
                "nome='" + nome + '\'' +
                '}';
    }
}
