package com.shishuheng.reader.process;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by shishuheng on 2018/1/9.
 */

public class BookInformationDatabaseOpenHelper extends SQLiteOpenHelper {
    private static String CREAT_BOOKSINFO = "create table Books("
            + "path text primary key,"
            + "author text,"
            + "title text,"
            + "category text,"
            + "image text,"
            + "id integer,"
            + "readPointer integer,"
            +"firstLineLastExit text)";
    private Context mContext;
    public BookInformationDatabaseOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        mContext = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREAT_BOOKSINFO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
