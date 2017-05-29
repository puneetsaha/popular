package com.example.puneetkumar.myapplication.webservices.parser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import com.example.puneetkumar.myapplication.model.User;

/**
 * Created by kevin-alien on 26/11/2014.
 */
public class UserParser {

    public User parseFromJson(JSONObject object) throws JSONException {
        User ret = new User();
        ret.setId((object.getString("_id")));
        ret.setUsername( object.getString("username"));
        ret.setGender( object.getString("gender"));
        ret.setEthnicity(object.getString("ethnicity"));
        ret.setNumberOfQuestionAnswered(object.getInt("total_answered"));
        JSONArray responseArray = object.getJSONArray("hasAnswered");
        for (int i = 0; i < responseArray.length(); i++) {
            JSONObject jsonResponse = responseArray.getJSONObject(i);
            String question = jsonResponse.getString("question");
            if(jsonResponse.has("answer")) {
                String answer = jsonResponse.getString("answer");
                ret.addQuestionAnswer(question, answer);
            }
        }
        return ret;
    }

    public User parseJSONId(JSONObject object) throws JSONException {
        User ret = new User();
        ret.setId((object.getString("id")));
        return ret;
    }


}