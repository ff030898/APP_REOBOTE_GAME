package com.reobotetechnology.reobotegame.model;

import java.util.Date;


public class MensagensModel {

    private int id;
    private UsuarioModel envia, recebe;
    private Date data;
    private String hora;
    private String texto;
    private int imagem;
    private String desc;
    private int tipo;
    private boolean visualizado;

    public MensagensModel(int id, UsuarioModel envia, UsuarioModel recebe, Date data, String hora, String texto, int imagem, String desc, int tipo, boolean visualizado) {
        this.id = id;
        this.envia = envia;
        this.recebe = recebe;
        this.data = data;
        this.hora = hora;
        this.texto = texto;
        this.imagem = imagem;
        this.desc = desc;
        this.tipo = tipo;
        this.visualizado = visualizado;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UsuarioModel getEnvia() {
        return envia;
    }

    public void setEnvia(UsuarioModel envia) {
        this.envia = envia;
    }

    public UsuarioModel getRecebe() {
        return recebe;
    }

    public void setRecebe(UsuarioModel recebe) {
        this.recebe = recebe;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getTipo() {
        return tipo;
    }

    public void setTipo(int tipo) {
        this.tipo = tipo;
    }

    public boolean isVisualizado() {
        return visualizado;
    }

    public void setVisualizado(boolean visualizado) {
        this.visualizado = visualizado;
    }
}
