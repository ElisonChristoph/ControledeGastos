package com.apps.elison.controledegastos.DAO;

import java.util.Date;

public class Gasto {


    private long ID;
    private String categoria, nome, data;
    //private int valor;
    //private int imagem;

    public Gasto(long ID, String categoria, String nome, String data) {
        this.ID = ID;
        this.categoria = categoria;
        this.nome = nome;
        this.data = data;
        //this.valor = valor;
        //this.imagem = imagem;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
