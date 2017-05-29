package com.example.puneetkumar.myapplication.db;

import android.app.ActionBar;

/**
 * Created by kevin-alien on 19/12/2014.
 */
public class TableQuestion {

    public static final String TABLE_NAME = "questions";
    public static final String ID = "_id";
    public static final String QUESTION = "question";

    public static final String ANSWER1 = "answer1";
    public static final String ANSWER1_COUNT = "answer1_count";
    public static final String ANSWER_1_ID = "answer_1_id";

    public static final String ANSWER2 = "answer2";
    public static final String ANSWER2_COUNT = "answer2_count";
    public static final String ANSWER_2_ID = "answer2_id";

    public static final String ANSWER3 = "answer3";
    public static final String ANSWER3_COUNT = "answer3_count";
    public static final String ANSWER_3_ID = "answer3_id";

    public static final String ANSWER4 = "answer4";
    public static final String ANSWER_4_ID = "answer4_id";
    public static final String ANSWER4_COUNT = "answer4_count";

    public static final String USER_ANSWER = "user_answer";
    public static final String NUMBER_OF_TIME_ANSWER = "number_of_time_answer";
    public static final String LATEST_UPDATE = "updated";

    public static final String NO_TEXT = "no_text";


    public static final String CREATE_REQUEST = " create table " +
            TableQuestion.TABLE_NAME + " ( " +
            TableQuestion.ID + " text primary key, " +
            TableQuestion.QUESTION + " text  default "+  NO_TEXT + " , " +
            TableQuestion.ANSWER1 + " text, " +
            TableQuestion.ANSWER_1_ID + " text, " +
            TableQuestion.ANSWER1_COUNT + " number  not null default 0, " +

            TableQuestion.ANSWER_2_ID + " text, " +
            TableQuestion.ANSWER2 + " text, " +
            TableQuestion.ANSWER2_COUNT + " number  not null default 0, " +

            TableQuestion.ANSWER_3_ID + " text ," +
            TableQuestion.ANSWER3 + " text, " +
            TableQuestion.ANSWER3_COUNT + " number  not null default 0, " +

            TableQuestion.ANSWER_4_ID + " text, " +
            TableQuestion.ANSWER4 + " text, " +
            TableQuestion.ANSWER4_COUNT + " number  not null default 0, " +

            TableQuestion.USER_ANSWER + " text  default -1, " +
            TableQuestion.LATEST_UPDATE + " text, " +
            TableQuestion.NUMBER_OF_TIME_ANSWER + " number not null default 0 " +
            " );";
}

