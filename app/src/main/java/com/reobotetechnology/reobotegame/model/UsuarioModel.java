package com.reobotetechnology.reobotegame.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.reobotetechnology.reobotegame.config.ConfiguracaoFireBase;

public class UsuarioModel {

    private String id, nome, email, senha, imagem, token;
    private int ranking, pontosG, pontosM, pontosS, pontosD, seguidores, seguindo, nivel, partidas, vitorias, derrotas, empates;
    private boolean online, jogando;

    public UsuarioModel() {
    }

    public UsuarioModel(String id, String nome, String email, String senha, String imagem, String token, int ranking, int pontosG, int pontosM, int pontosS, int pontosD, int seguidores, int seguindo, int nivel, int partidas, int vitorias, int derrotas, int empates, boolean online, boolean jogando) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.senha = senha;
        this.imagem = imagem;
        this.token = token;
        this.ranking = ranking;
        this.pontosG = pontosG;
        this.pontosM = pontosM;
        this.pontosS = pontosS;
        this.pontosD = pontosD;
        this.seguidores = seguidores;
        this.seguindo = seguindo;
        this.nivel = nivel;
        this.partidas = partidas;
        this.vitorias = vitorias;
        this.derrotas = derrotas;
        this.empates = empates;
        this.online = online;
        this.jogando = jogando;
    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagem() {
        return imagem;
    }

    public void setImagem(String imagem) {
        this.imagem = imagem;
    }

    public int getRanking() {
        return ranking;
    }

    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

    public int getPontosG() {
        return pontosG;
    }

    public void setPontosG(int pontosG) {
        this.pontosG = pontosG;
    }

    public int getPontosM() {
        return pontosM;
    }

    public void setPontosM(int pontosM) {
        this.pontosM = pontosM;
    }

    public int getPontosS() {
        return pontosS;
    }

    public void setPontosS(int pontosS) {
        this.pontosS = pontosS;
    }

    public int getPontosD() {
        return pontosD;
    }

    public void setPontosD(int pontosD) {
        this.pontosD = pontosD;
    }

    public int getSeguidores() {
        return seguidores;
    }

    public void setSeguidores(int seguidores) {
        this.seguidores = seguidores;
    }

    public int getSeguindo() {
        return seguindo;
    }

    public void setSeguindo(int seguindo) {
        this.seguindo = seguindo;
    }

    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {
        this.nivel = nivel;
    }

    public boolean isOnline() {
        return online;
    }

    public void setOnline(boolean online) {
        this.online = online;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPartidas() {
        return partidas;
    }

    public void setPartidas(int partidas) {
        this.partidas = partidas;
    }

    public boolean isJogando() {
        return jogando;
    }

    public void setJogando(boolean jogando) {
        this.jogando = jogando;
    }

    public int getVitorias() {
        return vitorias;
    }

    public void setVitorias(int vitorias) {
        this.vitorias = vitorias;
    }

    public int getDerrotas() {
        return derrotas;
    }

    public void setDerrotas(int derrotas) {
        this.derrotas = derrotas;
    }

    public int getEmpates() {
        return empates;
    }

    public void setEmpates(int empates) {
        this.empates = empates;
    }

    public void salvar(){
        DatabaseReference firebase = ConfiguracaoFireBase.getFirebaseDataBase();
        firebase.child("usuarios")
                .child( this.id )
                .setValue( this );
    }
}
