package com.ren.smartcity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Administrator on 2017/5/13.
 */
public class DbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "db_read";
    public static String NEWS_ID = "newsid";

    public DbHelper(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table read (_id integer primary key autoincrement,"+NEWS_ID+" text)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
