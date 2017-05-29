package com.example.puneetkumar.myapplication.webservices.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import com.example.puneetkumar.myapplication.model.Question;
import com.example.puneetkumar.myapplication.webservices.http.IHttpClient;
import com.example.puneetkumar.myapplication.db.TableAnswerToSend;
import com.example.puneetkumar.myapplication.webservices.async.*;
import com.example.puneetkumar.myapplication.webservices.requestobject.*;
import com.example.puneetkumar.myapplication.webservices.http.*;
import com.example.puneetkumar.myapplication.model.*;
import com.example.puneetkumar.myapplication.custom.*;
import com.example.puneetkumar.myapplication.db.*;


public class PostAnswer extends AsyncTask<Void, Void, Question> {
    public static final String TAG = PostAnswer.class.getCanonicalName();
    private String userId;




    public interface OnAnswerPosted {
        public void onAnswerPost(Question q);

        public void onError();
    }

    //private AnswerRequest mAnswer;

    private Question question;

    private String answerChoosen;

    private IHttpClient mClient;

    private OnAnswerPosted callback;

    private Context mContext;


    @Override
    protected Question doInBackground(Void... params) {
        // Update the question in memory
        for (Question.Answer a : question.getAnswers()) {
            if (a.getIdAnswer().equals(answerChoosen)) {
                a.incrementNumberOfTimeChosen();
                question.incrementNumberOfTimeAnswered();
            }
        }

        if ( mContext!= null ) {
            // Update the db
            question.updateInDBForAnswer(mContext);
            // Add the question to the to send list
            ContentValues cv = new ContentValues();
            cv.put(TableAnswerToSend.QUESTION_ID, question.getId());
            cv.put(TableAnswerToSend.ANSWER_ID, answerChoosen);
            cv.put(TableAnswerToSend.USER_ID, userId);
            mContext.getContentResolver().insert(ContentProviderContract.AnswerToSend.URI, cv);
        }
        return question;


        /*
        HashMap<String, String > httpParams = new HashMap<String, String>(3);
        httpParams.put("userid", mAnswer.getIdUser());
        httpParams.put("answerid", mAnswer.getIdAnswer());
        httpParams.put("questionid", mAnswer.getIdQuestion());


        String serverResponse = mClient.postData(EndpointList.ANSWER_ENDPOINT, httpParams);
        try {
            Log.d(TAG, "Server response " + serverResponse);
            return new QuestionParser().parseFromJSON(new JSONObject(serverResponse ));
        } catch (JSONException e) {
            Log.e(TAG, "Error while parsing response return");
            return null;
        }*/

    }

    @Override
    protected void onPostExecute(Question question) {
        if (question != null) {
            callback.onAnswerPost(question);
        } else {
            callback.onError();
        }
    }


    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public OnAnswerPosted getCallback() {
        return callback;
    }

    public void setCallback(OnAnswerPosted callback) {
        this.callback = callback;
    }

    public IHttpClient getHttpClient() {
        return mClient;
    }

    public void setHttpClient(IHttpClient mClient) {
        this.mClient = mClient;
    }


    public void setAnswerChoosen(String answerChoosen) {
        this.answerChoosen = answerChoosen;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
