package com.apps.elison.controledegastos.DAO;

import java.util.ArrayList;

public class Mes {

    private int ID;
    private String nome;
    private float renda;
    private float quergastar;
    private float gastototal;
    private int imagem;

    public Mes(int ID,String nome, float renda, float quergastar, float gastototal, int imagem ) {
        this.ID = ID;
        this.nome = nome;
        this.renda = renda;
        this.quergastar = quergastar;
        this.gastototal = gastototal;
        this.imagem = imagem;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public float getGastototal() {
        return gastototal;
    }

    public void setGastototal(float gastototal) {
        this.gastototal = gastototal;
    }

    public float getQuergastar() {
        return quergastar;
    }

    public void setQuergastar(float quergastar) {
        this.quergastar = quergastar;
    }

    public float getRenda() {
        return renda;
    }

    public void setRenda(float renda) {
        this.renda = renda;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }
}


