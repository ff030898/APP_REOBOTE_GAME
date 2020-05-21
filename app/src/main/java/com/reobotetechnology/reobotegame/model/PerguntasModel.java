package com.reobotetechnology.reobotegame.model;

public class PerguntasModel {
    private int id;
    private String pergunta,questaoA,questaoB,questaoC,questaoD,questaoCorreta, questaoDica;

    public PerguntasModel(int id, String pergunta, String questaoA, String questaoB, String questaoC, String questaoD, String questaoCorreta, String questaoDica) {
        this.id = id;
        this.pergunta = pergunta;
        this.questaoA = questaoA;
        this.questaoB = questaoB;
        this.questaoC = questaoC;
        this.questaoD = questaoD;
        this.questaoCorreta = questaoCorreta;
        this.questaoDica = questaoDica;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPergunta() {
        return pergunta;
    }

    public void setPergunta(String pergunta) {
        this.pergunta = pergunta;
    }

    public String getQuestaoA() {
        return questaoA;
    }

    public void setQuestaoA(String questaoA) {
        this.questaoA = questaoA;
    }

    public String getQuestaoB() {
        return questaoB;
    }

    public void setQuestaoB(String questaoB) {
        this.questaoB = questaoB;
    }

    public String getQuestaoC() {
        return questaoC;
    }

    public void setQuestaoC(String questaoC) {
        this.questaoC = questaoC;
    }

    public String getQuestaoD() {
        return questaoD;
    }

    public void setQuestaoD(String questaoD) {
        this.questaoD = questaoD;
    }

    public String getQuestaoCorreta() {
        return questaoCorreta;
    }

    public void setQuestaoCorreta(String questaoCorreta) {
        this.questaoCorreta = questaoCorreta;
    }

    public String getQuestaoDica() {
        return questaoDica;
    }

    public void setQuestaoDica(String questaoDica) {
        this.questaoDica = questaoDica;
    }
}
