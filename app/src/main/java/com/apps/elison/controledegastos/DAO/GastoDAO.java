package com.apps.elison.controledegastos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class GastoDAO {
    private final String TABLE_GASTOS = "Gastos";
    private DB_Gateway gw;

    public GastoDAO(Context ctx){
        gw = DB_Gateway.getInstance(ctx);
    }


    // retorna o ID da inserção
    public long salvarItem(Gasto gasto){
        ContentValues cv = new ContentValues();
        cv.put("Categoria", gasto.getCategoria());
        cv.put("Nome", gasto.getNome());
        cv.put("Valor", gasto.getValor());
        cv.put("Data", gasto.getData());
        //cv.put("Imagem", gasto.getImagem());

        return gw.getDatabase().insert(TABLE_GASTOS, null, cv);
    }

    // retorna a quantidade de linhas afetadas
    public int excluirItem(long id){
        return gw.getDatabase().delete(TABLE_GASTOS, "ID=?", new String[]{ id + "" });
    }

    public List<Gasto> retornarTodos(){
        List<Gasto> gastos = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Gastos", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String data = cursor.getString(cursor.getColumnIndex("Data"));
            int valor = cursor.getInt(cursor.getColumnIndex("Valor"));
            //int imagem = cursor.getInt(cursor.getColumnIndex("Imagem"));
            gastos.add(new Gasto(id, categoria, nome, data, valor));
        }
        cursor.close();
        return gastos;
    }

    public void recriarTabela(){
        gw.getDatabase().execSQL("DROP TABLE Gastos");
        gw.getDatabase().execSQL("CREATE TABLE Gastos (ID INTEGER PRIMARY KEY AUTOINCREMENT, Categoria TEXT NOT NULL, Nome TEXT NOT NULL, Data TEXT NOT NULL, Valor INTEGER NOT NULL)");
    }

}