package com.reobotetechnology.reobotegame.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.reobotetechnology.reobotegame.config.ConfigurationFireBase;

public class MatchModel {

    private String id;
    private boolean desconectado, aceito, recusado, internet;
    private String resultado;
    private String datetime;
    private UserModel user1, user2;

    public MatchModel(String id, boolean desconectado, boolean aceito, boolean recusado, boolean internet, String resultado, String datetime) {
        this.id = id;
        this.desconectado = desconectado;
        this.aceito = aceito;
        this.recusado = recusado;
        this.internet = internet;
        this.resultado = resultado;
        this.datetime = datetime;
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

    public void salvar(){
        DatabaseReference firebase = ConfigurationFireBase.getFirebaseDataBase();
        firebase.child("partidas")
                .child( this.id )
                .setValue( this );
    }
}
