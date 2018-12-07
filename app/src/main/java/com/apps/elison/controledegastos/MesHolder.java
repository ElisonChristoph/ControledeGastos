package com.apps.elison.controledegastos;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class MesHolder extends RecyclerView.ViewHolder {

    public TextView nome, renda, gasto;
    public ImageView imagem;

    public MesHolder(@NonNull View itemView) {
        super(itemView);

        nome = itemView.findViewById(R.id.tvnome);
        renda = itemView.findViewById(R.id.tvrenda);
        gasto = itemView.findViewById(R.id.tvgastos);
        imagem = itemView.findViewById(R.id.ivlogo);
    }

}
