package com.reobotetechnology.reobotegame.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;

public class MatchModel {

    private String id;
    private boolean desconectado, aceito, recusado, internet;
    private String resultado;
    private String datetime;
    private String emailUser;
    private String scoreUser;
    private String scoreUser2;
    private String emailUser2;


    public MatchModel() {
    }

    public MatchModel(String id, boolean desconectado, boolean aceito, boolean recusado, boolean internet, String resultado, String datetime, String emailUser, String scoreUser, String scoreUser2, String emailUser2) {
        this.id = id;
        this.desconectado = desconectado;
        this.aceito = aceito;
        this.recusado = recusado;
        this.internet = internet;
        this.resultado = resultado;
        this.datetime = datetime;
        this.emailUser = emailUser;
        this.scoreUser = scoreUser;
        this.scoreUser2 = scoreUser2;
        this.emailUser2 = emailUser2;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isDesconectado() {
        return desconectado;
    }

    public void setDesconectado(boolean desconectado) {
        this.desconectado = desconectado;
    }

    public boolean isAceito() {
        return aceito;
    }

    public void setAceito(boolean aceito) {
        this.aceito = aceito;
    }

    public boolean isRecusado() {
        return recusado;
    }

    public void setRecusado(boolean recusado) {
        this.recusado = recusado;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public boolean isInternet() {
        return internet;
    }

    public void setInternet(boolean internet) {
        this.internet = internet;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    @Exclude
    public String getEmailUser() {
        return emailUser;
    }

    public void setEmailUser(String emailUser) {
        this.emailUser = emailUser;
    }

    @Exclude
    public String getScoreUser2() {
        return scoreUser2;
    }

    public void setScoreUser2(String scoreUser2) {
        this.scoreUser2 = scoreUser2;
    }

    @Exclude
    public String getEmailUser2() {
        return emailUser2;
    }

    public void setEmailUser2(String emailUser2) {
        this.emailUser2 = emailUser2;
    }


    @Exclude
    public String getScoreUser() {
        return scoreUser;
    }

    public void setScoreUser(String scoreUser) {
        this.scoreUser = scoreUser;
    }

    public void salvar(){
        DatabaseReference firebase = ConfigurationFireBase.getFirebaseDataBase();
        firebase.child("partidas")
                .child( this.id )
                .setValue( this );
    }
}
