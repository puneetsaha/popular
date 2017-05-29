package com.example.puneetkumar.myapplication.db;

import android.app.ActionBar;
import android.content.ContentResolver;
import com.example.puneetkumar.myapplication.db.PollContentProvider;
import android.net.Uri;

/**
 * Created by kevin-alien on 02/12/2014.
 */
public class ContentProviderContract {

    public static final long VERSION = 1;

    public static final String AUTHORITY = "10.0.2.2:8080";

    public static final Uri BASE_URI = new Uri.Builder()
            .scheme(ContentResolver.SCHEME_CONTENT) // content://
            .encodedAuthority(AUTHORITY) //  //
            .build();


    // Contract Element for the table Appreciation Received
    public static final class Users {
        private Users() { }
        public static final String TABLE = TableUser.TABLE_NAME;
        private static final String MINOR_TYPE = "/vnd." + AUTHORITY ;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;
        public static final String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;
        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();
    }


    public static final class Questions {
        private Questions() { }
        public static final String TABLE = TableQuestion.TABLE_NAME;
        private static final String MINOR_TYPE = "/vnd." + AUTHORITY ;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;
        public static final String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;
        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();


        public static String[] FULL_PROJECTION = {
                TableQuestion.QUESTION,
                TableQuestion.ID,
                TableQuestion.NUMBER_OF_TIME_ANSWER,
                TableQuestion.LATEST_UPDATE,

                // User Answer
                TableQuestion.USER_ANSWER,

                // Answer
                TableQuestion.ANSWER1,
                TableQuestion.ANSWER2,
                TableQuestion.ANSWER3,
                TableQuestion.ANSWER4,
                // Answer ids
                TableQuestion.ANSWER_1_ID,
                TableQuestion.ANSWER_2_ID,
                TableQuestion.ANSWER_3_ID,
                TableQuestion.ANSWER_4_ID,
                // Answer Counts
                TableQuestion.ANSWER1_COUNT,
                TableQuestion.ANSWER2_COUNT,
                TableQuestion.ANSWER3_COUNT,
                TableQuestion.ANSWER4_COUNT
        };
    }

    public static final class AnswerToSend {
        private AnswerToSend() { }
        public static final String TABLE = TableAnswerToSend.TABLE_NAME;
        private static final String MINOR_TYPE = "/vnd." + AUTHORITY ;
        public static final String ITEM_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE + MINOR_TYPE;
        public static final String DIR_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE + MINOR_TYPE;
        public static final Uri URI = BASE_URI.buildUpon().appendPath(TABLE).build();

        public static String[] FULL_PROJECTION = {
                TableAnswerToSend.ID,
                TableAnswerToSend.QUESTION_ID,
                TableAnswerToSend.ANSWER_ID,
                TableAnswerToSend.USER_ID
        };




    }
}