package com.reobotetechnology.reobotegame.dao;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseHCOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME="hc.db";
    private static int DATABASE_VERSION=1;


    public DataBaseHCOpenHelper(Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }


}
