package com.apps.elison.controledegastos.DAO;

public class Credito {
    private long ID;
    private String nome;
    private String data;
    private String valor;

    public Credito(long ID,  String nome, String data , String valor) {
        this.ID = ID;
        this.nome = nome;
        this.data = data;
        this.valor = valor;
        //this.imagem = imagem;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getValor() {
        return valor;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
