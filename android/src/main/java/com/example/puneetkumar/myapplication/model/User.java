package com.example.puneetkumar.myapplication.model;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

import com.example.puneetkumar.myapplication.db.TableUser;
import com.example.puneetkumar.myapplication.db.ContentProviderContract;


public class User {

    private static final String TAG = User.class.getCanonicalName();

    public static int INVALID_PASSWORD = 102;

    public static int INVALID_USER_NAME = 101;

    public static int INVALID_BIRTHDAY = 103;

    /** The user id from the server**/
    private String id;

    /** The user name to post **/
    private String username;

    /** The gender to post **/
    private String gender;

    /** The password to post  Password will be encrypted on server side**/
    private String password;

    private String ethnicity;



    private int numberOfQuestionAnswered = 0;

    /** HashMap of previousely answered question **/
    HashMap<String, String> mAnsweredQuestions = new HashMap<String, String>();

    private boolean isActive = false;

    public ArrayList<Integer> validateUser() {
        ArrayList<Integer> errors = new ArrayList<Integer>();
        if (username.length() < 5) {
            errors.add(INVALID_USER_NAME);
        }
        if (password.length() < 4) {
            errors.add(INVALID_PASSWORD);
        }

        return errors;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }




    public void setIsActive(int isActive) {
        if (isActive > 0 ) {
            this.isActive = true;
        } else {
            this.isActive = false;
        }
    }

    public String getEthnicity() {
        return ethnicity;
    }

    public void setEthnicity(String ethnicity) {
        this.ethnicity = ethnicity;
    }

    public void saveInDB(ContentResolver cr) {
        ContentValues cv = new ContentValues();
        cv.put(TableUser.GENDER, gender);
        cv.put(TableUser.ETHNICITY, ethnicity);
        cv.put(TableUser.ACTIVE, 1);
        cv.put(TableUser.ID, id);

        Uri uri = new Uri.Builder()
                .scheme( ContentResolver.SCHEME_CONTENT )
                .encodedAuthority( ContentProviderContract.AUTHORITY )
                .appendPath( ContentProviderContract.Users.TABLE )
                .build();

        Log.d(TAG, "URI for insert " + uri.toString());
        cr.insert(uri, cv);
    }

    public void addQuestionAnswer(String questionId, String answerId) {
        mAnsweredQuestions.put(questionId, answerId);
    }

    public String getQuestionAnswered(String questionId) {
        if (mAnsweredQuestions.containsKey(questionId)) {
            return mAnsweredQuestions.get(questionId);
        }
        return null;
    }

    public int getNumberOfQuestionAnswered() {
        return numberOfQuestionAnswered;
    }

    public void setNumberOfQuestionAnswered(int numberOfQuestionAnswered) {
        this.numberOfQuestionAnswered = numberOfQuestionAnswered;
    }

    public HashMap<String, String> getAnsweredQuestions() {
        return mAnsweredQuestions;
    }

    public void setAnsweredQuestions(HashMap<String, String> mAnsweredQuestions) {
        this.mAnsweredQuestions = mAnsweredQuestions;
    }
}