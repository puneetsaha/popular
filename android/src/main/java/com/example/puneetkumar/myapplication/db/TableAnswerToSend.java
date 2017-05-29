package com.example.puneetkumar.myapplication.db;

public class TableAnswerToSend {

    public static final String TABLE_NAME = "answer_to_send";
    public static final String ID = "_id";
    public static final String USER_ID ="user_id";
    public static final String QUESTION_ID = "question";
    public static final String ANSWER_ID = "answer";


    public static final String CREATE_REQUEST = " create table " +
            TableAnswerToSend.TABLE_NAME + " ( " +
            TableAnswerToSend.ID + " integer primary key autoincrement, " +
            TableAnswerToSend.USER_ID + " text not null, " +
            TableAnswerToSend.QUESTION_ID + " text not null, " +
            TableAnswerToSend.ANSWER_ID + " text not null " +
            " );";


}