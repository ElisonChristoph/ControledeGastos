package com.apps.elison.controledegastos.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

public class CreditoDAO {
    private final String TABLE_CREDITOS = "Creditos";
    private DB_Gateway gw;
    float valorTotalMensal;

    public CreditoDAO(Context ctx){

        gw = DB_Gateway.getInstance(ctx);
        //criaTabela();
    }

    // retorna o ID da inserção
    public long salvarItem(Credito credito){
        ContentValues cv = new ContentValues();
        cv.put("Nome", credito.getNome());
        cv.put("Valor", credito.getValor());
        cv.put("Data", credito.getData());
        System.out.println(credito.getData());
        return gw.getDatabase().insert(TABLE_CREDITOS, null, cv);
    }

    // retorna a quantidade de linhas afetadas
    public int excluirItem(long id){
        return gw.getDatabase().delete(TABLE_CREDITOS, "ID=?", new String[]{ id + "" });
    }

    //lista todos obejtos da tabela credito
    public List<Credito> retornarTodos(){
        List<Credito> credito = new ArrayList<>();
        valorTotalMensal =0.0f;
        Cursor cursor = gw.getDatabase().rawQuery("SELECT * FROM Creditos", null);
        while(cursor.moveToNext()){
            int id = cursor.getInt(cursor.getColumnIndex("ID"));
            String nome = cursor.getString(cursor.getColumnIndex("Nome"));
            String valor = cursor.getString(cursor.getColumnIndex("Valor"));
            valorTotalMensal = valorTotalMensal+Float.parseFloat(valor.replaceAll(",","."));
            String data = cursor.getString(cursor.getColumnIndex("Data"));
            credito.add(new Credito(id, nome, valor, data));
        }
        cursor.close();
        return credito;
    }

    public float getValorTotal(){
        return valorTotalMensal;
    }

    public void recriarTabela(){
        gw.getDatabase().execSQL("DROP TABLE Creditos");
        gw.getDatabase().execSQL("CREATE TABLE Creditos (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Valor TEXT, Data TEXT)");
    }


}
