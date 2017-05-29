package com.example.puneetkumar.myapplication.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import com.example.puneetkumar.myapplication.R;
import com.example.puneetkumar.myapplication.model.*;
import com.example.puneetkumar.myapplication.webservices.async.*;
import com.example.puneetkumar.myapplication.webservices.http.*;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;


public class SignupActivity extends Activity {

    public static String TAG = SignupActivity.class.getCanonicalName();

    @Bind(R.id.but_submit)
    Button submitButton;

    @Bind(R.id.rg_gender)
    RadioGroup genderGroup;


    @Bind(R.id.et_username)
    EditText etUsername;

    @Bind(R.id.et_password)
    EditText etPassword;

    @Bind(R.id.spinner_ethnicity)
    Spinner spinnerEthnicity;

    /** Stores the date to display to the user **/
    private String dateToDisplay ="";

    private String genderSelected = "female";

    private String ethnicitySelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_activity);
        ethnicitySelected = getString(R.string.pick_a_race);
        ButterKnife.bind(this);
        populateSpinner();
        setListener();
    }

    protected void populateSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.ethnicity_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEthnicity.setAdapter(adapter);
    }

    protected void setListener() {
        genderGroup.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radio_female:
                        genderSelected = "female";
                        break;
                    case R.id.radio_male:
                        genderSelected="male";
                        break;
                }
            }
        });
    }



    @OnClick(R.id.but_submit)
    public void submitData() {
        PostCreateAccount postAccount = new PostCreateAccount();
        postAccount.setCallback(new PostCreateAccount.OnUserAccountCreated() {

            @Override
            public void onAccountCreatedSuccess(User user) {
                user.setIsActive(1);
                user.saveInDB(getContentResolver());
                saveUserIdToSharedPreferences(user.getId());
                Intent openQuestion = new Intent( SignupActivity.this, QuestionActivity.class);
                UserController.getInstance().setUserId(user.getId());
                openQuestion.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(openQuestion);
                finish();
            }

            @Override
            public void onAccountCreationFailed(String errorMessage) {
                Toast.makeText(SignupActivity.this,
                        R.string.error_while_sending_information_to_our_server,Toast.LENGTH_LONG)
                        .show();
            }
        });
        User userToCreate = new User();
/*
        userToCreate.setUsername(etUsername.getText().toString());
        userToCreate.setPassword(etPassword.getText().toString());
*/
        userToCreate.setGender(genderSelected);
        userToCreate.setEthnicity(ethnicitySelected);

        ArrayList<Integer> inputErrors = userToCreate.validateUser();
        if (inputErrors.size() == 0) {
            postAccount.setToSend(userToCreate);
            postAccount.setHttpClient(new HttpClient());
            postAccount.execute();
        } else {
            displayErrors(inputErrors);
        }
    }



    public void saveUserIdToSharedPreferences(String userId) {
        Log.d(TAG, "Saving id " + userId + "to shared preferences ");
        SharedPreferences prefs = this.getSharedPreferences(
                LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LoginActivity.USER_ID_KEY, userId);
        editor.commit();
    }

    public void displayErrors(ArrayList<Integer> errors) {
        if (errors.contains(User.INVALID_USER_NAME)) {
            Toast.makeText(this, "The username needs to have 5 chars at least", Toast.LENGTH_LONG).show();
        }
        if (errors.contains(User.INVALID_PASSWORD)) {
            Toast.makeText(this, "The password needs to have 4 chars at least", Toast.LENGTH_LONG).show();
        }
    }


}
