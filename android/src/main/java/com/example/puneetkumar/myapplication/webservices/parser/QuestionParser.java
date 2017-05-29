package com.example.puneetkumar.myapplication.webservices.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import com.example.puneetkumar.myapplication.model.Question;




/**
 * Created by kevin-alien on 03/12/2014.
 */
public class QuestionParser {

    public List<Question> parseFromJSON(JSONArray json) throws JSONException {
        List<Question> questions = new ArrayList<Question>(20);
        try {
            for (int i=0; i < json.length(); i++) {
                Question q = parseFromJSON(json.getJSONObject(i));
                questions.add(q);
            }
        } catch (JSONException e) {
            e.printStackTrace();
            throw new JSONException("Error while parsing question list");
        }
        return questions;
    }

    public Question parseFromJSON (JSONObject qJSON) throws JSONException {
        Question question = new Question();
        try {
            question.setId(qJSON.getString("_id"));
            question.setQuestion(qJSON.getString("question"));
            question.setCreatedBy(qJSON.getString("createdBy"));
            question.setCreatedBy(qJSON.getString("created"));
            question.setNumberOfTimeAnswered(qJSON.getInt("numberOfTimeAnswered"));
            JSONArray answerArray = qJSON.getJSONArray("answers");
            for (int i = 0; i < answerArray.length(); i++ ) {
                JSONObject jsonAnswer = answerArray.getJSONObject(i);
                Question.Answer a = new Question.Answer();
                a.setIdAnswer(jsonAnswer.getString("_id"));
                if(jsonAnswer.has("answer")) {
                    a.setAnswer(jsonAnswer.getString("answer"));
                    a.setNumberOfTimeChosen(jsonAnswer.getInt("numberOfTimeChoosen"));
                    question.addAnswer(a);
                }
            }
        } catch ( JSONException e) {
            e.printStackTrace();
            throw new JSONException("Error while parsing question");
        }
        return question;
    }
}