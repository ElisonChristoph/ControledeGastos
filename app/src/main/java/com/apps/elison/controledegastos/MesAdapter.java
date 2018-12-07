package com.apps.elison.controledegastos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.elison.controledegastos.DAO.Mes;

import java.util.List;

public class MesAdapter extends RecyclerView.Adapter {

    private List<Mes> meses;
    private Context context;

    public MesAdapter(List<Mes> meses, Context context) {
        this.meses = meses;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.linha_meses, viewGroup, false);

        MesHolder mesholder = new MesHolder(view);

        return mesholder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

        MesHolder mesholder = (MesHolder) viewHolder;
        Mes mes = meses.get(i);
        mesholder.nome.setText(mes.getNome());
        mesholder.renda.setText(String.valueOf(mes.getRenda()));
        mesholder.gasto.setText(String.valueOf(mes.getGastototal()));
        mesholder.imagem.setImageResource(mes.getImagem());

    }

    @Override
    public int getItemCount() {
        return meses.size();
    }

}
