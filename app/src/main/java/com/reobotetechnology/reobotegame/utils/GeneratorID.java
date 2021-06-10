package com.reobotetechnology.reobotegame.utils;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class GeneratorID {
    public static String id(){
        String idChat;
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH:mm:ss");
        Calendar cal = Calendar.getInstance();
        Date data = new Date();
        cal.setTime(data);
        Date dataId = cal.getTime();
        idChat = dateFormat.format(dataId);

        return idChat;
    }
}
