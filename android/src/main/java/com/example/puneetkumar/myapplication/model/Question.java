package com.example.puneetkumar.myapplication.model;

import android.app.ActionBar;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.animation.AnimationSet;
import android.widget.ListPopupWindow;

//import com.google.android.gms.games.quest.Quest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import  com.example.puneetkumar.myapplication.db.TableQuestion;
import com.example.puneetkumar.myapplication.db.ContentProviderContract;


public class Question {

    private static final String TAG = "Question";
    String id;

    String question;

    String createdBy;

    String createdOn;

    int numberOfTimeAnswered = 0;

    List<Answer> answers = new ArrayList<Answer>(4);

    private String userAnswer = null;

    public String getId() {
        return id;
    }

    public void addAnswer(Answer toAdd) {
        if (!answers.contains(toAdd)) {
            answers.add(toAdd);
        }
    }

    public static Question loadFromCursor(Cursor c) {
        Question q = new Question();
        q.setId(c.getString(c.getColumnIndex(TableQuestion.ID)));
        q.setQuestion(c.getString(c.getColumnIndex(TableQuestion.QUESTION)));
        q.setNumberOfTimeAnswered(c.getInt(c.getColumnIndex(TableQuestion.NUMBER_OF_TIME_ANSWER)));
        q.setUserAnswer(c.getString(c.getColumnIndex(TableQuestion.USER_ANSWER)));

        Answer answer1 = new Answer();
        answer1.setAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER1)));
        answer1.setIdAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER_1_ID)));
        answer1.setNumberOfTimeChosen(c.getInt(c.getColumnIndex(TableQuestion.ANSWER1_COUNT)));

        Answer answer2 = new Answer();
        answer2.setAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER2)));
        answer2.setIdAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER_2_ID)));
        answer2.setNumberOfTimeChosen(c.getInt(c.getColumnIndex(TableQuestion.ANSWER2_COUNT)));

        Answer answer3 = new Answer();
        answer3.setAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER3)));
        answer3.setIdAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER_3_ID)));
        answer3.setNumberOfTimeChosen(c.getInt(c.getColumnIndex(TableQuestion.ANSWER3_COUNT)));

        Answer answer4 = new Answer();
        answer4.setAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER4)));
        answer4.setIdAnswer(c.getString(c.getColumnIndex(TableQuestion.ANSWER_4_ID)));
        answer4.setNumberOfTimeChosen(c.getInt(c.getColumnIndex(TableQuestion.ANSWER4_COUNT)));

        q.getAnswers().add(answer1);
        q.getAnswers().add(answer2);
        q.getAnswers().add(answer3);
        q.getAnswers().add(answer4);


        return q;
    }

    public List<Answer> getAnswers() {
        return answers;
    }

    public void setAnswers(List<Answer> answers) {
        this.answers = answers;
    }

    public Answer getAnswerAt(int pos) {
        return answers.get(pos);
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getQuestion() {
        return question;
    }

    public int getNumberOfTimeAnswered() {
        return numberOfTimeAnswered;
    }

    public void setNumberOfTimeAnswered(int numberOfTimeAnswered) {
        this.numberOfTimeAnswered = numberOfTimeAnswered;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Question " + this.question);
        int i = 0;
        for (Answer a : answers) {
            sb.append("answer ").append(i).append(" ").append(a.toString());
            i++;
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        try {
            Question q = (Question) o;
            return q.getId().equals(this.getId());
        } catch (ClassCastException e) {
            return false;
        }
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getUserAnswer(Context ctx) {
        if (null != userAnswer && !userAnswer.equals("-1")) {
            return userAnswer;
        } else {

            String ret = "-1";
            // We get the question answer
            Cursor q = ctx.getContentResolver().query(
                    ContentProviderContract.Questions.URI,
                    new String[]{TableQuestion.USER_ANSWER},
                    TableQuestion.ID + " = ?",
                    new String[]{this.id},
                    "");
            if (q.getCount() > 0) {
                q.moveToNext();
                ret = q.getString(q.getColumnIndex(TableQuestion.USER_ANSWER));
                q.close();
            }
            return ret;
        }
    }

    public void updateInDBForAnswer(Context ctx) {
        ContentValues cv = new ContentValues();
        cv.put(TableQuestion.ANSWER1_COUNT, answers.get(0).getNumberOfTimeChosen());
        cv.put(TableQuestion.ANSWER2_COUNT, answers.get(0).getNumberOfTimeChosen());
        cv.put(TableQuestion.ANSWER3_COUNT, answers.get(0).getNumberOfTimeChosen());
        cv.put(TableQuestion.ANSWER4_COUNT, answers.get(0).getNumberOfTimeChosen());
        cv.put(TableQuestion.NUMBER_OF_TIME_ANSWER, numberOfTimeAnswered);
        cv.put(TableQuestion.USER_ANSWER, userAnswer);
        cv.put(TableQuestion.LATEST_UPDATE, System.currentTimeMillis());
        ctx.getContentResolver().update(
                ContentProviderContract.Questions.URI,
                cv,
                TableQuestion.ID + " = ?",
                new String[]{this.id}
        );

    }

    public void incrementNumberOfTimeAnswered() {
        numberOfTimeAnswered++;
    }


    public static class Answer {
        String idAnswer;

        String answer;

        int numberOfTimeChosen;

        public int getNumberOfTimeChosen() {
            return numberOfTimeChosen;
        }

        public void setNumberOfTimeChosen(int numberOfTimeChosen) {
            this.numberOfTimeChosen = numberOfTimeChosen;
        }

        public void incrementNumberOfTimeChosen() {
            this.numberOfTimeChosen++;
        }

        public String getAnswer() {
            return answer;
        }

        public void setAnswer(String answer) {
            this.answer = answer;
        }

        public String getIdAnswer() {
            return idAnswer;
        }

        public void setIdAnswer(String idAnswer) {
            this.idAnswer = idAnswer;
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(answer);
            return sb.toString();
        }

        @Override
        public boolean equals(Object o) {
            try {
                Answer a = (Answer) o;
                return a.getIdAnswer().equals(this.getIdAnswer());
            } catch (ClassCastException e) {
                return false;
            }
        }
    }


    public static void removeQuestionsHistory(Context ctx) {
        int deleted =
                ctx.getContentResolver().delete(ContentProviderContract.Questions.URI, null, null);
        Log.d(TAG, " Number of questions deleted = " + deleted);
    }
}