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
        cv.put("Imagem", gasto.getImagem());

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
            float valor = cursor.getFloat(cursor.getColumnIndex("Valor"));
            int imagem = cursor.getInt(cursor.getColumnIndex("Imagem"));
            gastos.add(new Gasto(id, categoria, nome, data, valor, imagem));
        }
        cursor.close();
        return gastos;
    }

    public void recriarTabela(){
        gw.getDatabase().execSQL("DROP TABLE Compras");
        gw.getDatabase().execSQL("CREATE TABLE Compras (ID INTEGER PRIMARY KEY AUTOINCREMENT, Item TEXT NOT NULL, Quantidade TEXT)");
    }

}