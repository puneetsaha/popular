package com.example.puneetkumar.myapplication.webservices.async;

import android.os.AsyncTask;

import com.example.puneetkumar.myapplication.dialogs.DatePicker;
import com.example.puneetkumar.myapplication.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import com.example.puneetkumar.myapplication.webservices.http.*;
import com.example.puneetkumar.myapplication.webservices.parser.UserParser;

public class PostCreateAccount extends AsyncTask<Void, Void, User> {

    public static final String TAG = PostCreateAccount.class.getCanonicalName();

    /**
     * Callback interface to notify the caller of this activity that the
     * the user account has been created on server side
     */
    public interface OnUserAccountCreated {
        abstract void onAccountCreatedSuccess(User user);
        public void onAccountCreationFailed(String errorMessage);
    }

    public void setToSend(User toSend) {
        this.toSend = toSend;
    }

    /** HttpClient in charge of sending the call to the web service **/
    private IHttpClient mHttpClient;

    /** Callback interface **/
    private OnUserAccountCreated mCallback;

    /** User object containing data to send to the server **/
    private User toSend;

    /** Detect if an error has happened while creating the account **/
    private boolean errorDetected = false;


    @Override
    protected User doInBackground(Void... params) {
        HashMap<String, String> httpParams = new HashMap<String, String>(5);
        httpParams.put("username", toSend.getUsername());
        httpParams.put("password", toSend.getPassword());
        httpParams.put("ethnicity", toSend.getEthnicity());
        httpParams.put("gender", toSend.getGender());

        try {
            String serverResponse = mHttpClient.postData(EndpointList.ACCOUNT_CREATION_ENDPOINT, httpParams);
            User ret = new UserParser().parseJSONId(new JSONObject(serverResponse));
            return ret;
        } catch (JSONException e) {
            e.printStackTrace();
            errorDetected = true;
        }
        return null;
    }

    @Override
    protected void onPostExecute(User user) {
        if (!errorDetected) {
            mCallback.onAccountCreatedSuccess(user);
        } else {
            mCallback.onAccountCreationFailed("Something went wrong ");
        }
    }

    public IHttpClient getHttpClient() {
        return mHttpClient;
    }

    public void setHttpClient(IHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    public OnUserAccountCreated getCallback() {
        return mCallback;
    }

    public void setCallback(OnUserAccountCreated mCallback) {
        this.mCallback = mCallback;
    }

    public User getToSend() {
        return toSend;
    }
}