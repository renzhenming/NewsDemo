package com.ren.smartcity.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/5/13.
 */
public class DbDao {

    private final DbHelper helper;

    public DbDao(Context context){
        helper = new DbHelper(context);
    }

    public void insert(String newsId){
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("insert into read ("+DbHelper.NEWS_ID+") values (?)",new Object[]{newsId});
        db.close();
    }

    public void delete(String newsid){
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("delete from read where newsid = ?",new Object[]{newsid});
        db.close();
    }

    public void update(String oldNewsId , String newsid){
        SQLiteDatabase db = helper.getReadableDatabase();
        db.execSQL("update read set newsid = "+oldNewsId+" where newsid = ?",new Object[]{newsid});
        db.close();
    }

    public ArrayList<String> query(){
        SQLiteDatabase db = helper.getReadableDatabase();
        ArrayList<String> idLists = new ArrayList<>();
        Cursor cursor = db.query(true, "read", new String[]{"newsid"}, null, null, null, null, null, null);
        while(cursor != null && cursor.moveToNext()){
            String newsid = cursor.getString(cursor.getColumnIndex("newsid"));
            idLists.add(newsid);
        }
        cursor.close();
        db.close();
        return idLists;
    }
}
