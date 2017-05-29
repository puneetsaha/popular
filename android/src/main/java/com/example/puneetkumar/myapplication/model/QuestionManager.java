package com.example.puneetkumar.myapplication.model;

import java.util.List;

/**
 * Created by kevin-alien on 03/12/2014.
 */
public interface QuestionManager {

    public List<Question> getQuestions();

    public Question getQuestionAt(int position);

    public void addQuestions(List<Question> questions);

    public void updateQuestionWithAnswer(Question question, String answerId);
}