package com.example.puneetkumar.myapplication.webservices.async;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import com.example.puneetkumar.myapplication.model.Question;
import com.example.puneetkumar.myapplication.webservices.http.IHttpClient;
import com.example.puneetkumar.myapplication.db.TableQuestion;
import com.example.puneetkumar.myapplication.db.ContentProviderContract;
import com.example.puneetkumar.myapplication.webservices.requestobject.QuestionPageRequest;
import com.example.puneetkumar.myapplication.webservices.http.EndpointList;
import com.example.puneetkumar.myapplication.webservices.parser.QuestionParser;

public class GetQuestion extends AsyncTask<Void, Void, List<Question>>{

    public static final String TAG = GetQuestion.class.getCanonicalName();

    public static final int CACHE_MAX_SIZE = 80;

    public static final int SERVER_PAGE_SIZE = 20;

    public interface OnQuestionListReceived {
        public void onQuestionListReceived(List<Question> questionList);

        public void onError();

        public void onEmptyListReceived();
    }

    private IHttpClient mHttpClient;

    private QuestionPageRequest mRequest;

    private OnQuestionListReceived callback;

    private Context mContext;

    private boolean errorDetected;

    @Override
    protected List<Question> doInBackground(Void... params) {
        HashMap<String, String> httpParams = new HashMap<String, String>();
        httpParams.put("page", mRequest.getPage());
        httpParams.put("userid", mRequest.getUserId());
        httpParams.put("answered", mRequest.getAnswered());
        httpParams.put("tag", mRequest.getTag());
        Log.d(TAG, "Tag sent to server " + mRequest.getTag());
        String response = mHttpClient.getData(EndpointList.QUESTION_ENDPOINT, httpParams);
        Cursor c=null;
        try {
            Log.d(TAG, "response for question list " +  response);
            List<Question> questions = new QuestionParser().parseFromJSON(new JSONArray(response));

            // Calculate the space to make for new cache
            ContentResolver cr = mContext.getContentResolver();
            int currentNumberOfQuestionInCache = cr.query(ContentProviderContract.Questions.URI, null, null, null, null ).getCount();
            int placeToMake = (currentNumberOfQuestionInCache + questions.size()) - CACHE_MAX_SIZE;
            Log.d(TAG, " Place to make for new Questions" + placeToMake);
            if (placeToMake > 0 ) {
                // Get the array of the 6 lats
                 c = cr.query(
                        ContentProviderContract.Questions.URI,
                        new String []{TableQuestion.ID},
                        null, null ,
                        TableQuestion.LATEST_UPDATE + " ASC " +
                                " LIMIT " + placeToMake );
                String idsToDelete [] = new String[placeToMake];
                while (c.moveToNext()) {
                    String idToDelete = c.getString(c.getColumnIndex(TableQuestion.ID));
                    int d = cr.delete(ContentProviderContract.Questions.URI, TableQuestion.ID +"=? ",
                            new String[]{idToDelete});
                    Log.d(TAG, "deleted " + d);
                }
            }

            for (Question q : questions) {
                ContentValues cv = new ContentValues();
                cv.put(TableQuestion.ID, q.getId());
                cv.put(TableQuestion.QUESTION, q.getQuestion());
                cv.put(TableQuestion.NUMBER_OF_TIME_ANSWER, q.getNumberOfTimeAnswered());
                cv.put(TableQuestion.LATEST_UPDATE, System.currentTimeMillis());
                //cv.put(TableQuestion.USER_ANSWER, );
                Log.d(TAG,"size of answers"+ q.getAnswers().size());
                for (int i = 0; i < q.getAnswers().size() ; i++) {
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
                    // inserting into local db .The questions are shown from local db.
                    mContext.getContentResolver().insert(ContentProviderContract.Questions.URI, cv);

                }
            }
            return questions;
        } catch (JSONException e) {
            e.printStackTrace();
            errorDetected = true;
            return null;
        }
        finally
        {
            if(c!=null){
                c.close();
            }
        }
    }

    @Override
    protected void onPostExecute(List<Question> questions) {
        if (errorDetected || questions == null ) {
            callback.onError();
        } else if (questions.size() == 0) {
            callback.onEmptyListReceived();
        } else {
            callback.onQuestionListReceived(questions);
        }
    }

    public QuestionPageRequest getRequest() {
        return mRequest;
    }

    public void setRequest(QuestionPageRequest mRequest) {
        this.mRequest = mRequest;
    }

    public IHttpClient getHttpClient() {
        return mHttpClient;
    }

    public void setHttpClient(IHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    public OnQuestionListReceived getCallback() {
        return callback;
    }

    public void setCallback(OnQuestionListReceived callback) {
        this.callback = callback;
    }

    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

}