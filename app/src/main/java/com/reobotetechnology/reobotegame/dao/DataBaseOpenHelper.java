package com.reobotetechnology.reobotegame.dao;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DataBaseOpenHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME="biblia.db";
    private static int DATABASE_VERSION=1;


    public DataBaseOpenHelper(Context context) {
        super(context, DATABASE_NAME, null,  DATABASE_VERSION);
    }


}
