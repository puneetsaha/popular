package com.example.puneetkumar.myapplication.webservices.async;

import android.os.AsyncTask;
import android.util.Log;

import com.example.puneetkumar.myapplication.model.User;
import com.example.puneetkumar.myapplication.webservices.http.EndpointList;
import com.example.puneetkumar.myapplication.webservices.http.IHttpClient;
import com.example.puneetkumar.myapplication.webservices.parser.UserParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;


/**
 * Created by kevin-alien on 26/11/2014.
 */
public class PostLogin extends AsyncTask<Void, Void, User> {

    public static String TAG = PostLogin.class.getName();

    public interface OnUserLoggedIn {
        public void userIsLoggedIn( User user);
        public void error();
    }

    private User toLogin;

    private IHttpClient mHttpClient;

    private OnUserLoggedIn callback;

    @Override
    protected User doInBackground(Void... params) {
        HashMap<String, String> httpParams = new HashMap<String, String>();
        httpParams.put("username", toLogin.getUsername());
        httpParams.put("password", toLogin.getPassword());
        String response = mHttpClient.postData(EndpointList.LOGIN_ENDPOINT, httpParams);
        UserParser uParser = new UserParser();
        try {
            Log.d(TAG, "Response for login : " + response);
            return uParser.parseJSONId(new JSONObject(response));
        } catch (JSONException e) {
            e.printStackTrace();
            Log.e(TAG, "Error while parsing user from login");
            return null;
        }
    }

    @Override
    protected void onPostExecute(User user) {
        if (user != null ) {
            callback.userIsLoggedIn(user);
        } else {
            callback.error();
        }
    }

    public IHttpClient getHttpClient() {
        return mHttpClient;
    }

    public void setHttpClient(IHttpClient mHttpClient) {
        this.mHttpClient = mHttpClient;
    }

    public User getToLogin() {
        return toLogin;
    }

    public void setToLogin(User toLogin) {
        this.toLogin = toLogin;
    }

    public OnUserLoggedIn getCallback() {
        return callback;
    }

    public void setCallback(OnUserLoggedIn callback) {
        this.callback = callback;
    }

}