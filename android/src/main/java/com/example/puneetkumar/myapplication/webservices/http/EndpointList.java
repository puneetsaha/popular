package com.example.puneetkumar.myapplication.webservices.http;

public abstract  class EndpointList {

    //public static final String BASE_SERVER_URL = "http://192.168.0.102:8080/";
    // public static final String BASE_SERVER_URL = "http://54.148.195.252:8080/";
    public static final String BASE_SERVER_URL = "http://10.0.2.2:8080/";
    public static final String ACCOUNT_CREATION_ENDPOINT = BASE_SERVER_URL + "signup";
    public static final String LOGIN_ENDPOINT = BASE_SERVER_URL + "login";
    public static final String QUESTION_ENDPOINT = BASE_SERVER_URL + "question";
    public static final String ANSWER_ENDPOINT = BASE_SERVER_URL + "question/answer";
    public static final String ANSWER_BATCH_ENDPOINT = BASE_SERVER_URL + "question/answer_batch";
    public static final String USER_ENDPOINT = BASE_SERVER_URL + "user";
}