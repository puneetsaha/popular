package com.example.puneetkumar.myapplication.db;

/**
 * Created by puneetkumar on 9/20/16.
 */
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;



/**
 * Created by kevin-alien on 02/12/2014.
 */
public class PollContentProvider extends ContentProvider {

    private static final String TAG = "PollContentProvider";
    private static final int  USER_ALL_ROWS = 1000;
    private static final int  QUESTION_ALL_ROWS = 2000;
    private static final int  ANSWER_TO_SEND_ALL_ROWS = 3000;

    public static final long VERSION = 1;

    private DBHelper dbHelper;


    public static  final Uri userInserturi = new Uri.Builder()
            .scheme( ContentResolver.SCHEME_CONTENT )
            .authority( ContentProviderContract.AUTHORITY )
            .appendPath( ContentProviderContract.Users.TABLE )
            .appendPath( "insert" ).build();



    public static final Map<String, String> USER_PROJECTION = new HashMap<String, String>();
    public static final Map<String, String> QUESTION_PROJECTION = new HashMap<String, String>();
    public static final Map<String, String> ANSWER_TO_SEND_PROJECTION = new HashMap<String, String>();

    private static final UriMatcher MATCHER = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        // User URI MATCHER
        MATCHER.addURI(ContentProviderContract.AUTHORITY, ContentProviderContract.Users.TABLE , USER_ALL_ROWS);
        MATCHER.addURI(ContentProviderContract.AUTHORITY, ContentProviderContract.Questions.TABLE , QUESTION_ALL_ROWS);
        MATCHER.addURI(ContentProviderContract.AUTHORITY, ContentProviderContract.AnswerToSend.TABLE , ANSWER_TO_SEND_ALL_ROWS);

        //MATCHER.addURI(userInserturi, USER_INSERT);
        // User projection
        USER_PROJECTION.put(TableUser.BIRTHDAY, TableUser.BIRTHDAY);
        USER_PROJECTION.put(TableUser.ID, TableUser.ID);
        USER_PROJECTION.put(TableUser.NAME, TableUser.NAME);
        USER_PROJECTION.put(TableUser.GENDER, TableUser.GENDER);
        USER_PROJECTION.put(TableUser.ETHNICITY, TableUser.ETHNICITY);

        QUESTION_PROJECTION.put(TableQuestion.ID, TableQuestion.ID);
        QUESTION_PROJECTION.put(TableQuestion.QUESTION, TableQuestion.QUESTION);
        QUESTION_PROJECTION.put(TableQuestion.NUMBER_OF_TIME_ANSWER,
                TableQuestion.NUMBER_OF_TIME_ANSWER);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER1, TableQuestion.ANSWER1);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER1_COUNT, TableQuestion.ANSWER1_COUNT);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER2, TableQuestion.ANSWER2);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER2_COUNT, TableQuestion.ANSWER2_COUNT);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER3, TableQuestion.ANSWER3);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER3_COUNT, TableQuestion.ANSWER3_COUNT);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER4, TableQuestion.ANSWER4);
        QUESTION_PROJECTION.put(TableQuestion.ANSWER4_COUNT, TableQuestion.ANSWER4_COUNT);
        QUESTION_PROJECTION.put(TableQuestion.USER_ANSWER, TableQuestion.USER_ANSWER);
        QUESTION_PROJECTION.put(TableQuestion.LATEST_UPDATE, TableQuestion.LATEST_UPDATE);

        ANSWER_TO_SEND_PROJECTION.put(TableAnswerToSend.ID, TableAnswerToSend.ID);
        ANSWER_TO_SEND_PROJECTION.put(TableAnswerToSend.USER_ID, TableAnswerToSend.USER_ID);
        ANSWER_TO_SEND_PROJECTION.put(TableAnswerToSend.QUESTION_ID, TableAnswerToSend.QUESTION_ID);
        ANSWER_TO_SEND_PROJECTION.put(TableAnswerToSend.ANSWER_ID, TableAnswerToSend.ANSWER_ID);
    }




    public PollContentProvider() {
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DBHelper(getContext(), DBHelper.DATABASE_NAME, null,
                DBHelper.DATABASE_VERSION);
        return true;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        String target = "";
        switch (MATCHER.match(uri)) {
            case USER_ALL_ROWS:
                target = ContentProviderContract.Users.TABLE;
                break;
            case QUESTION_ALL_ROWS:
                target = ContentProviderContract.Questions.TABLE;
                break;
            case ANSWER_TO_SEND_ALL_ROWS:
                target = ContentProviderContract.AnswerToSend.TABLE;
                break;
            default:
                Log.e(TAG, "Bug not able to match uri given");
        }
        Log.d(TAG, "Delete called for table " + target );
        return dbHelper.getWritableDatabase().delete(target, selection, selectionArgs);
    }

    @Override
    public String getType(Uri uri) {
        Log.d(TAG, "URI to match " + uri.toString());
        switch (MATCHER.match(uri)) {
            case USER_ALL_ROWS:
                return ContentProviderContract.Users.DIR_TYPE;
        }
        throw new Resources.NotFoundException();
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Uri toReturn = null;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        // String Used to fill the db with an empty row
        String nullColumnHack = null;
        String target = "";

        switch (MATCHER.match(uri)) {
            case USER_ALL_ROWS:
                target = ContentProviderContract.Users.TABLE;
                break;
            case QUESTION_ALL_ROWS:
                target = ContentProviderContract.Questions.TABLE;
                break;
            case ANSWER_TO_SEND_ALL_ROWS:
                target = ContentProviderContract.AnswerToSend.TABLE;
                break;
            default:
                Log.e(TAG, "Bug not able to match uri given");
        }
        // Inserting value to the local Database

        long id = db.insert(target, nullColumnHack, values);
        if (id != - 1) {
            toReturn = ContentUris.withAppendedId(uri, id);
            Log.d(TAG, "to return " +  toReturn);
        } else {
            db.update(target, values,  TableQuestion.ID + "=?",
                    new String[] { values.getAsString(TableQuestion.ID)} );
        }
        return toReturn;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String target = "";
        switch (MATCHER.match(uri)) {
            case USER_ALL_ROWS:
                target = ContentProviderContract.Users.TABLE;
                break;
            case QUESTION_ALL_ROWS:
                target = ContentProviderContract.Questions.TABLE;
                break;
            case ANSWER_TO_SEND_ALL_ROWS:
                target = ContentProviderContract.AnswerToSend.TABLE;
                break;
            default:
                Log.e(TAG, "Bug not able to match uri given");
        }
        Cursor c = db.query(target,	projection,	selection, selectionArgs,
                null, null, sortOrder);
        return c;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        String target = "";
        switch (MATCHER.match(uri)) {
            case USER_ALL_ROWS:
                target = ContentProviderContract.Users.TABLE;
                break;
            case QUESTION_ALL_ROWS:
                target = ContentProviderContract.Questions.TABLE;
                break;
            default:
                Log.e(TAG, "Bug not able to match uri given");
        }
        int number = db.update(target, values, selection, selectionArgs);
        Log.d(TAG, " Update on " + target +  " for id =  " + values.getAsString("_id") );
        return number;
    }
}



