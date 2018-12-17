package com.apps.elison.controledegastos.DAO;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB_Conexao extends SQLiteOpenHelper {

    // Nome do banco de dados
    private static final String DATABASE_NAME = "ListadeGastos.db";
    private static final String TABLE_CREDITO = "Creditos";
    // Versão do banco - incrementar a cada atualização do banco
    private static final int DATABASE_VERSION = 1;
    private final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS Gastos (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Valor TEXT, Data TEXT, Categoria TEXT)";
    //private final String CREATE_TABLE_CREDITO = "CREATE TABLE IF NOT EXISTS TABLE_CREDITO (ID INTEGER PRIMARY KEY AUTOINCREMENT, Nome TEXT NOT NULL, Valor TEXT, Data TEXT);";

    private Context context;

    public DB_Conexao(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        //db.execSQL(CREATE_TABLE_CREDITO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
