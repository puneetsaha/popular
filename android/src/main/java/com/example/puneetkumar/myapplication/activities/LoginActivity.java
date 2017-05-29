package com.example.puneetkumar.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


import com.example.puneetkumar.myapplication.R;
import com.example.puneetkumar.myapplication.model.*;
import com.example.puneetkumar.myapplication.custom.*;
import com.example.puneetkumar.myapplication.db.*;
import com.example.puneetkumar.myapplication.webservices.async.*;
import com.example.puneetkumar.myapplication.webservices.requestobject.*;
import com.example.puneetkumar.myapplication.webservices.http.*;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.newrelic.agent.android.*;
import com.facebook.FacebookSdk;


/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends FragmentActivity  {

    private static final String TAG = "LoginActivity";

    protected static final String USER_ID_KEY = "kevinlegoff.com.poll.userid";
    protected static final String NO_USER = "no_user";

    protected static final String SHARED_PREFS = "previous_user";
    CallbackManager callbackManager = null;
    LoginButton  loginButton=null;

/*
    @Bind(R.id.et_username)
    EditText etUsername;
*/

  /*  @Bind(R.id.et_password)
    EditText etPassword;*/

/*
    @OnClick(R.id.but_login)
    public void sendLogin() {
        if (validateLogin()) {
            User userToLogin = new User();
            userToLogin.setUsername(etUsername.getText().toString());
            userToLogin.setPassword(etPassword.getText().toString());
            PostLogin loginTask = new PostLogin();
            loginTask.setHttpClient(new HttpClient());
            loginTask.setToLogin(userToLogin);
            loginTask.setCallback( new PostLogin.OnUserLoggedIn() {
                @Override
                public void userIsLoggedIn(User user) {
                    openQuestionActivityForUserId(user.getId());
                }

                @Override
                public void error() {
                    Toast.makeText(LoginActivity.this, R.string.error_invalid_credential,
                            Toast.LENGTH_LONG).show();
                }
            });
            loginTask.execute();
        }
    }
*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        loginButton = (LoginButton)findViewById(R.id.login_button);
       
        // Callback registration
        Log.d(TAG,"Are we here");
        if (loginButton != null) {
            Log.d(TAG,"Registering callback");
            loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                @Override
                public void onSuccess(LoginResult loginResult) {
                    // App code
                    Log.d(TAG, "result from facebook " + loginResult.getAccessToken().getUserId());
                    openQuestionActivityForUserId(loginResult.getAccessToken().getUserId());
                }

                @Override
                public void onCancel() {
                    // App code
                }

                @Override
                public void onError(FacebookException exception) {
                    Log.d(TAG,"Error from Facebook is" +exception.getMessage());
                    // App code
                }
            });
        }

       // NewRelic.withApplicationToken("AA9f1fffb0d4511e63575dec65b0fa70ef6fe9f2a7"
       // ).start(this.getApplication());
        DBHelper dbHelper = new DBHelper(getApplicationContext(), DBHelper.DATABASE_NAME ,
                null, DBHelper.DATABASE_VERSION);
        dbHelper.getReadableDatabase();



 //       String userId = getUserIdFromSharedPreferences();
 //       Log.d(TAG, "User id from shared prefs " + userId);
  //      if (! userId.equals(NO_USER)) {
   //         openQuestionActivityForUserId(userId);
   //     }


    }

    protected void openQuestionActivityForUserId (String idUser) {
        Log.d(TAG,"Are we in openQuestionActivityForUser");
        Intent i = new Intent(LoginActivity.this, QuestionActivity.class);
        saveUserIdToSharedPreferences(idUser);
        UserController.getInstance().setUserId(idUser);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }

    private String getUserIdFromSharedPreferences() {
        SharedPreferences prefs = this.getSharedPreferences(SHARED_PREFS,
                Context.MODE_PRIVATE);
        String userId = prefs.getString(USER_ID_KEY, NO_USER);
        return userId;
    }

    public void saveUserIdToSharedPreferences(String userId) {
        Log.d(TAG, "Saving id " + userId + "to shared preferences ");
        SharedPreferences prefs = this.getSharedPreferences(
                LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(USER_ID_KEY, userId);
        editor.commit();
    }



/*
    @OnClick(R.id.but_create_account)
    public void openCreateAccount() {
        Intent intent = new Intent(LoginActivity.this, SignupActivity.class);
        startActivity(intent);
        finish();
    }
*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }


    public boolean validateLogin() {
        boolean validated= true;
        return validated;
    }
}