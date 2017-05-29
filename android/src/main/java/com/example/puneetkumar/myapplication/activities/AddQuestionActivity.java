package com.example.puneetkumar.myapplication.activities;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.puneetkumar.myapplication.R;
import com.example.puneetkumar.myapplication.model.Question;
import com.example.puneetkumar.myapplication.webservices.async.PostQuestion;
import com.example.puneetkumar.myapplication.webservices.http.HttpClient;
import com.example.puneetkumar.myapplication.webservices.requestobject.CreateQuestionRequest;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;

/**
 * Created by kevin-alien on 06/12/2014.
 */
public class AddQuestionActivity  extends Activity {

    @Bind(R.id.et_question)
    EditText etQuestion;

    @Bind(R.id.et_answer1)
    EditText etAnswer1;

    @Bind(R.id.et_answer2)
    EditText etAnswer2;

    @Bind(R.id.et_answer3)
    EditText etAnswer3;

    @Bind(R.id.et_answer4)
    EditText etAnswer4;

    @Bind(R.id.et_tags)
    EditText etTags;

    @Bind(R.id.but_add_question)
    Button butAddQuestion;

    String userId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);
        ButterKnife.bind(this);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        userId = getIntent().getExtras().getString(QuestionActivity.ID_USER);
    }

    @OnClick(R.id.but_add_question)
    public void sendQuestionToServer () {
        if (validate()) {
            CreateQuestionRequest cqr = new CreateQuestionRequest();
            cqr.setUserId(userId);
            cqr.setQuestion(etQuestion.getText().toString());
            cqr.setAnswer1(etAnswer1.getText().toString());
            cqr.setAnswer2(etAnswer2.getText().toString());
            cqr.setAnswer3(etAnswer3.getText().toString());
            cqr.setAnswer4(etAnswer4.getText().toString());
            cqr.setTags(etTags.getText().toString());
            sendQuestionRequestToServer(cqr);
        } else {
            Toast.makeText(this, R.string.data_is_invalid, Toast.LENGTH_LONG).show();
        }
    }

    protected void sendQuestionRequestToServer(CreateQuestionRequest cqr) {
        PostQuestion pq = new PostQuestion();
        pq.setmRequest(cqr);
        pq.setmClient(new HttpClient());
        pq.setCallback(  new PostQuestion.OnQuestionCreated() {
            @Override
            public void onQuestionCreated(Question q) {
                etQuestion.setText("");
                etAnswer1.setText("");
                etAnswer2.setText("");
                etAnswer3.setText("");
                etAnswer4.setText("");
                etTags.setText("");
                Toast.makeText(AddQuestionActivity.this, R.string.your_question_has_been_created,
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError() {
                Toast.makeText(AddQuestionActivity.this, R.string.error_while_sending_information_to_our_server,
                        Toast.LENGTH_LONG).show();
            }
        });
        pq.execute();
    }

    public boolean validate() {
        boolean validated = true;
        EditText [] questionToValidate = {etAnswer1, etAnswer2, etAnswer3, etAnswer4};
        int numberOfQuestionValidated = 0;
        for(EditText et : questionToValidate) {
            if (et.getText().length() > 0 ) {
                numberOfQuestionValidated ++ ;
            }
        }
        if (numberOfQuestionValidated < 2) validated = false;
        if (etQuestion.getText().length() == 0) validated = false;
        if (etTags.getText().length() == 0) validated = false;
        return validated;
    }

}