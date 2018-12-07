package com.apps.elison.controledegastos;

import java.text.SimpleDateFormat;

public class DataHoraAtual {

    long date = System.currentTimeMillis();
    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    String dateString = sdf.format(date);

    @Override
    public String toString(){
        return dateString;
    }

}
