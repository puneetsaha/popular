package com.example.puneetkumar.myapplication.webservices.async;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.example.puneetkumar.myapplication.db.*;
import com.example.puneetkumar.myapplication.webservices.http.*;
import com.example.puneetkumar.myapplication.webservices.parser.*;
import com.example.puneetkumar.myapplication.model.Question;

public class SendAnswerIntentService extends IntentService {

    public static String TAG = "SendAnswerIntentService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     * @param name Used to name the worker thread, important only for debugging.
     */
    public SendAnswerIntentService(String name) {
        super(name);
    }

    public SendAnswerIntentService() {
        super("SendAnswerIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "OnHandle intent");
        // Get the list of answer to send
        Cursor cursor = getContentResolver().query(
                ContentProviderContract.AnswerToSend.URI,
                ContentProviderContract.AnswerToSend.FULL_PROJECTION,
                null,
                null,
                null
        );
        try {
            JSONObject obj = new JSONObject();
            ArrayList<JSONObject> list = new ArrayList<JSONObject>();
            Log.d(TAG, "Cursor size : " + cursor.getCount());
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    if (obj.optString("userid").equals("")) {
                        obj.put("userid", cursor.getString(cursor.getColumnIndex(TableAnswerToSend.USER_ID)));
                    }
                    JSONObject answerObj = new JSONObject();
                    answerObj.put("answerid", cursor.getString(
                            cursor.getColumnIndex(TableAnswerToSend.ANSWER_ID)));
                    answerObj.put("questionid", cursor.getString(
                            cursor.getColumnIndex(TableAnswerToSend.QUESTION_ID)));
                    list.add(answerObj);
                }
                obj.put("answers", new JSONArray(list));
                Log.d(TAG, "json generated" + obj.toString());
                // Sends the request
                HashMap<String, String> params = new HashMap<String, String>(1);
                params.put("answers", obj.toString());
                String serverResponse = new HttpClient().postData(EndpointList.ANSWER_BATCH_ENDPOINT, params);
                Log.d(TAG, "Server response " + serverResponse);
                List<Question> questions = new QuestionParser().parseFromJSON(new JSONArray(serverResponse));
                getContentResolver().delete(ContentProviderContract.AnswerToSend.URI, null, null);
                for (Question q : questions) {
                    ContentValues cv = new ContentValues();
                    cv.put(TableQuestion.ID, q.getId());
                    cv.put(TableQuestion.QUESTION, q.getQuestion());
                    cv.put(TableQuestion.NUMBER_OF_TIME_ANSWER, q.getNumberOfTimeAnswered());
                    cv.put(TableQuestion.LATEST_UPDATE, System.currentTimeMillis());
                    for (int i = 0; i < 4; i++) {
                        Question.Answer a = q.getAnswerAt(i);
                        if (i == 0) {
                            cv.put(TableQuestion.ANSWER_1_ID, a.getIdAnswer());
                            cv.put(TableQuestion.ANSWER1, a.getAnswer());
                            cv.put(TableQuestion.ANSWER1_COUNT, a.getNumberOfTimeChosen());
                        } else if (i == 1) {
                            cv.put(TableQuestion.ANSWER_2_ID, a.getIdAnswer());
                            cv.put(TableQuestion.ANSWER2, a.getAnswer());
                            cv.put(TableQuestion.ANSWER2_COUNT, a.getNumberOfTimeChosen());
                        } else if (i == 2) {
                            cv.put(TableQuestion.ANSWER_3_ID, a.getIdAnswer());
                            cv.put(TableQuestion.ANSWER3, a.getAnswer());
                            cv.put(TableQuestion.ANSWER3_COUNT, a.getNumberOfTimeChosen());
                        } else if (i == 3) {
                            cv.put(TableQuestion.ANSWER_4_ID, a.getIdAnswer());
                            cv.put(TableQuestion.ANSWER4, a.getAnswer());
                            cv.put(TableQuestion.ANSWER4_COUNT, a.getNumberOfTimeChosen());
                        }
                    }
                    getContentResolver().insert(ContentProviderContract.Questions.URI, cv);
                }
            } else {
                Log.d(TAG, "Nothing to update on server");
            }
            // Empty the cache of request to send

        } catch (JSONException e) {
            Log.e(TAG, "Error while building json");
            e.printStackTrace();
        } finally {
            cursor.close();
        }


    }
}