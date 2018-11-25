package com.apps.elison.controledegastos;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class GastosHolder extends RecyclerView.ViewHolder {


    public TextView categoria;
    public TextView nome;
    public TextView data;
    public TextView valor;
    public ImageView imagem;
    public ImageView btnExcluir;

    public GastosHolder(View itemView) {
        super(itemView);
        categoria = itemView.findViewById(R.id.tvCategoria);
        nome = itemView.findViewById(R.id.tvNome);
        data = itemView.findViewById(R.id.tvData);
        valor = itemView.findViewById(R.id.tvValor);
        //imagem = itemView.findViewById(R.id.ivLogo);
        btnExcluir = itemView.findViewById(R.id.iv_linha_deleteID);
    }
}