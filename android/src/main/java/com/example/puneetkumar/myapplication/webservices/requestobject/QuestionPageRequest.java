package com.example.puneetkumar.myapplication.webservices.requestobject;

public class QuestionPageRequest {

    public String page;
    public String userId;
    public String answered = "false";
    public String tag = "";

    public String getPage() {
        return page;
    }

    public QuestionPageRequest setPage(String page) {
        this.page = page;
        return this;
    }

    public String getAnswered() {
        return answered;
    }

    public QuestionPageRequest setAnswered(String answered) {
        this.answered = answered;
        return this;
    }

    public String getUserId() {
        return userId;
    }

    public String getTag() {
        return tag;
    }

    public QuestionPageRequest setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public QuestionPageRequest setUserId(String userId) {
        this.userId = userId;
        return this;
    }

}
