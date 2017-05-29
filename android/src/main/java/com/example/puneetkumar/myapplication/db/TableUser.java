package com.example.puneetkumar.myapplication.db;


public class TableUser {

    public static final String TABLE_NAME = "users";
    public static final String ID = "_id";
    public static final String NAME = "name";
    public static final String BIRTHDAY = "birthday";
    public static final String ETHNICITY = "ethnicity";
    public static final String GENDER = "gender";
    public static final String ACTIVE = "active";

    public static final String CREATE_REQUEST = " create table " +
            TableUser.TABLE_NAME + " ( " +
            TableUser.ID + " text primary key, " +
            TableUser.NAME + " text unique, " +
            TableUser.BIRTHDAY + " text, " +
            TableUser.GENDER + " text, " +
            TableUser.ETHNICITY + " text, " +
            TableUser.ACTIVE + " number not null default 0 " +
            " );";

}