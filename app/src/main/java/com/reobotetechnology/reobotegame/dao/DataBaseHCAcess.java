package com.reobotetechnology.reobotegame.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.reobotetechnology.reobotegame.model.BibliaModel;
import com.reobotetechnology.reobotegame.model.HCModel;
import com.reobotetechnology.reobotegame.model.LivrosBibliaModel;
import com.reobotetechnology.reobotegame.model.PerguntasModel;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHCAcess {

    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase db;
    private static DataBaseHCAcess instance;

    private DataBaseHCAcess(Context context) {

        this.openHelper = new DataBaseHCOpenHelper(context);
    }

    public static DataBaseHCAcess getInstance(Context context){
        if(instance==null){
            instance = new DataBaseHCAcess(context);
        }

        return instance;
    }

    //abrirDatabase
    private void open(){
        this.db=openHelper.getWritableDatabase();
    }

    //fecharDatabase

    private void close(){

        if (db!=null){
            this.db.close();
        }
    }

    public void onCreate(){
        open();
        close();
    }

    public List<HCModel> listHC(){

        open();

        List<HCModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM songs";
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{

                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String lyrics = cursor.getString(3);
                    HCModel b = new HCModel(id, title, lyrics);
                    lista.add(b);

                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }

    public List<HCModel> listHCID(int num){

        open();

        List<HCModel> lista = new ArrayList<>();

        String sql = "SELECT * FROM songs where id="+num;
        Cursor cursor = db.rawQuery(sql, null);
        if (cursor.getCount()>0){
            if (cursor.moveToFirst()){
                do{

                    int id = cursor.getInt(0);
                    String title = cursor.getString(1);
                    String lyrics = cursor.getString(3);
                    HCModel b = new HCModel(id, title, lyrics);
                    lista.add(b);

                }while(cursor.moveToNext());
            }
        }
        cursor.close();
        close();
        return lista;

    }



}
