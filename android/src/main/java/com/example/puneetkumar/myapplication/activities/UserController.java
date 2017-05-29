package com.example.puneetkumar.myapplication.activities;

/**
 * Created by puneetkumar on 7/14/16.
 */
public class UserController {

    private String userId = "";

    private static UserController ourInstance = new UserController();

    public static UserController getInstance() {
        return ourInstance;
    }

    private UserController() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
