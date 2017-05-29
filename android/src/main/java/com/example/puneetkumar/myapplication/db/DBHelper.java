package com.example.puneetkumar.myapplication.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by kevin-alien on 02/12/2014.
 */
public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";
    public static final String DATABASE_NAME = "poll.db";
    public static final int DATABASE_VERSION = 5;


    public DBHelper(Context context, String name,
                    SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(TableUser.CREATE_REQUEST);
        db.execSQL(TableQuestion.CREATE_REQUEST);
        db.execSQL(TableAnswerToSend.CREATE_REQUEST);
        Log.w(TAG, "TableUser created using " + TableUser.CREATE_REQUEST);
        Log.w(TAG, "TableQuestion created using " + TableQuestion.CREATE_REQUEST);
        Log.w(TAG, "TableAnswerToSend table created using " + TableAnswerToSend.CREATE_REQUEST);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Log the version upgrade.
        Log.w(TAG, "Upgrading from version " +
                oldVersion + " to " +
                newVersion + ", which will destroy all old data");
        // The simplest case is to drop the old table and create a new one.
        db.execSQL("DROP TABLE IF EXISTS " + TableUser.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableQuestion.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TableAnswerToSend.TABLE_NAME);
        // Create a new one.
        onCreate(db);
    }

}
