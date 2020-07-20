package com.reobotetechnology.reobotegame.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;

public class PartidaModel {

    private String id;
    private boolean desconectado, aceito, recusado, internet;
    private String resultado;

    public PartidaModel(String id, boolean desconectado, boolean aceito, boolean recusado, boolean internet, String resultado) {
        this.id = id;
        this.desconectado = desconectado;
        this.aceito = aceito;
        this.recusado = recusado;
        this.internet = internet;
        this.resultado = resultado;
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

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFireBase.getFirebaseDataBase();
        firebase.child("partidas")
                .child( this.id )
                .setValue( this );
    }
}
