package com.example.puneetkumar.myapplication.webservices.async;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import com.example.puneetkumar.myapplication.model.User;
import com.example.puneetkumar.myapplication.webservices.http.*;
import com.example.puneetkumar.myapplication.webservices.parser.*;
import com.example.puneetkumar.myapplication.db.*;


public class GetUserDetails extends AsyncTask<Void, Void, User> {

    public static final String TAG = GetUserDetails.class.getCanonicalName();

    public interface  OnUserDetails {
        public void onUserDetails(User u);

        public void onError();
    }

    private String userId;

    private OnUserDetails callback;

    private Context mContext;

    private IHttpClient mHttpClient;

    @Override
    protected User doInBackground(Void... params) {
        HashMap<String, String > httpParams = new HashMap<String, String>();
        httpParams.put("userid", userId);
        String response = mHttpClient.postData(EndpointList.USER_ENDPOINT, httpParams);
        try {
            Log.d(TAG, "Obtaining user from server : " + response);
            User u = new UserParser().parseFromJson(new JSONObject(response));
            for (String key : u.getAnsweredQuestions().keySet()) {
                ContentValues cv = new ContentValues();
                cv.put(TableQuestion.ID, key);
                cv.put(TableQuestion.USER_ANSWER, u.getAnsweredQuestions().get(key));
                mContext.getContentResolver().insert(ContentProviderContract.Questions.URI, cv);
            }
            return u;
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "The user retrieved could not be parsed");
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        if (user == null) {
            callback.onError();
        } else {
            callback.onUserDetails(user);
        }
    }

    public IHttpClient getHttpClient() {
        return mHttpClient;
    }

    public void setHttpClient(IHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    public OnUserDetails getCallback() {
        return callback;
    }

    public void setCallback(OnUserDetails callback) {
        this.callback = callback;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }
}
