package com.example.puneetkumar.myapplication.webservices.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.puneetkumar.myapplication.model.Question;
import com.example.puneetkumar.myapplication.webservices.http.EndpointList;
import com.example.puneetkumar.myapplication.webservices.http.IHttpClient;
import com.example.puneetkumar.myapplication.webservices.parser.QuestionParser;
import com.example.puneetkumar.myapplication.webservices.requestobject.CreateQuestionRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by kevin-alien on 06/12/2014.
 */
public class PostQuestion extends AsyncTask<Void, Void, Question> {

    private static final String TAG = "PostQuestion";

    public interface  OnQuestionCreated {
        public void onQuestionCreated(Question q);
        public void onError();
    }

    private IHttpClient mClient;

    private CreateQuestionRequest mRequest;

    private OnQuestionCreated callback;

    @Override
    protected Question doInBackground(Void... p) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userid", mRequest.getUserId());
        params.put("question", mRequest.getQuestion());
        params.put("answer1", mRequest.getAnswer1());
        params.put("answer2", mRequest.getAnswer2());
        params.put("answer3", mRequest.getAnswer3());
        params.put("answer4", mRequest.getAnswer4());
        params.put("tag", mRequest.getTags());
        String response = mClient.postData(EndpointList.QUESTION_ENDPOINT, params);
        QuestionParser parser = new QuestionParser();
        Log.d(TAG, "response from server" + response);
        try {
            return parser.parseFromJSON(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Question question) {
        if (question != null) {
            callback.onQuestionCreated(question);
        } else {
            callback.onError();
        }
    }

    public void setmRequest(CreateQuestionRequest mRequest) {
        this.mRequest = mRequest;
    }

    public OnQuestionCreated getCallback() {
        return callback;
    }

    public void setCallback(OnQuestionCreated callback) {
        this.callback = callback;
    }

    public IHttpClient getmClient() {
        return mClient;
    }

    public void setmClient(IHttpClient mClient) {
        this.mClient = mClient;
    }

    public CreateQuestionRequest getmRequest() {
        return mRequest;
    }

}