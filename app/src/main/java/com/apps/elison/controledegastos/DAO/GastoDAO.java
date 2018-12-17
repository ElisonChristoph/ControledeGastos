package com.apps.elison.controledegastos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.Date;
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
        cv.put("Nome", gasto.getNome());
        cv.put("Valor", gasto.getValor());
        cv.put("Data", gasto.getData());
        cv.put("Categoria", gasto.getCategoria());
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
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String valor = cursor.getString(cursor.getColumnIndex("Valor"));
            String data = cursor.getString(cursor.getColumnIndex("Data"));
            String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
            gastos.add(new Gasto(id, nome, valor, data, categoria));
        }
        cursor.close();
        return gastos;
    }

    public List<Gasto> retornaMes(String mes){
        List<Gasto> gastos = new ArrayList<>();
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Gastos", null);
        while(cursor.moveToNext()){
            String[] mesOb = cursor.getString(cursor.getColumnIndex("Data")).split("/");
            if( mesOb[2]== mes) {
                int id = cursor.getInt(cursor.getColumnIndex("ID"));
                String nome = cursor.getString(cursor.getColumnIndex("Nome"));
                String valor = cursor.getString(cursor.getColumnIndex("Valor"));
                String data = cursor.getString(cursor.getColumnIndex("Data"));
                String categoria = cursor.getString(cursor.getColumnIndex("Categoria"));
                gastos.add(new Gasto(id, nome, valor, data, categoria));
            }
        }
        cursor.close();
        return gastos;
    }

    public void recriarTabela(){
        gw.getDatabase().execSQL("DROP TABLE Gastos");
        gw.getDatabase().execSQL("CREATE TABLE Gastos (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Valor TEXT, Data TEXT, Categoria TEXT)");
    }



}