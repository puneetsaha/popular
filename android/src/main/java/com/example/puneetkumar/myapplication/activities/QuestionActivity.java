package com.example.puneetkumar.myapplication.activities;

import android.os.Bundle;
import android.app.Activity;


import android.app.LoaderManager;
import android.app.SearchManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.annotation.Nullable;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.puneetkumar.myapplication.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Bind;
import butterknife.OnClick;
import com.example.puneetkumar.myapplication.webservices.async.*;
import com.example.puneetkumar.myapplication.webservices.requestobject.*;
import com.example.puneetkumar.myapplication.webservices.http.*;
import com.example.puneetkumar.myapplication.model.*;
import com.example.puneetkumar.myapplication.custom.*;
import com.example.puneetkumar.myapplication.db.*;


public class QuestionActivity extends FragmentActivity
        implements QuestionManager, UserManager, LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = QuestionActivity.class.getCanonicalName();

    public static final String ID_USER = "id_user";

    public static final int ID_QUESTION_LOADER = 1;


    protected boolean searchMode = false;

    @Bind(R.id.question_pager)
    CustomViewPager mPager;

    @Bind(R.id.question_answered)
    TextView tvQuestionAnswered;

    private QuestionPageAdapter questionPageAdapter;

    protected List<Question> mQuestions = new ArrayList<Question>();

    protected User mUser;

    protected String tag = "";

    private String mCurrentUserId;

    private int questionPage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setHasOptionsMenu(true);
        setContentView(R.layout.activity_question);
        ButterKnife.bind(this);
        mCurrentUserId = UserController.getInstance().getUserId();
        // Get the good intent
        handleIntent(getIntent());
        getUserFromServer(mCurrentUserId);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            tag = query;
            searchMode = true;
            getActionBar().setTitle(query);
            getActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString(ID_USER, mCurrentUserId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_question, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        // Setting up listener for search
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void setUser(User u) {
        mUser = u;
    }

    @Override
    public void incrementQuestionAnswered() {
        getUser().setNumberOfQuestionAnswered(getUser().getNumberOfQuestionAnswered() + 1);
        tvQuestionAnswered.setText(String.format(QuestionActivity.this.getResources().getString(
                R.string.you_have_answered_x_questions), getUser().getNumberOfQuestionAnswered()));
    }

    @Override
    public User getUser() {
        return mUser;
    }

    public void getUserFromServer(String userId) {
        GetUserDetails getUserDetails = new GetUserDetails();
        getUserDetails.setUserId(UserController.getInstance().getUserId());
        getUserDetails.setHttpClient(new HttpClient());
        getUserDetails.setContext(this);
        getUserDetails.setCallback(new GetUserDetails.OnUserDetails() {
            @Override
            public void onUserDetails(User u) {
                Log.d(TAG,"Please tell me we are here");
                setUser(u);
                tvQuestionAnswered.setText(String.format(QuestionActivity.this.getResources().getString(
                        R.string.you_have_answered_x_questions), u.getNumberOfQuestionAnswered()));
                getNextQuestionPage();
            }

            @Override
            public void onError() {
                Toast.makeText(QuestionActivity.this, R.string.error_while_retrieving_question_list,
                        Toast.LENGTH_LONG).show();
            }
        });
        getUserDetails.execute();
    }

    @Override
    protected void onPause() {
        Intent service = new Intent(this, SendAnswerIntentService.class);
        startService(service);
        Log.d(TAG, "onPause ");
        super.onPause();
    }

    public void getNextQuestionPage() {
        Log.d(TAG, "Get Question page : " + questionPage);
        GetQuestion task = new GetQuestion();
        task.setHttpClient(new HttpClient());
        task.setRequest(new QuestionPageRequest()
                .setPage(Integer.toString(questionPage))
                .setTag(tag));

        task.setContext(this);
        task.setCallback(new GetQuestion.OnQuestionListReceived() {
            @Override
            public void onQuestionListReceived(List<Question> questionList) {
                if (questionList.size() > 0) {
                    questionPage++;
                }
                if (!searchMode) {
                    LoaderManager lm = getLoaderManager();
                    lm.restartLoader(QuestionActivity.ID_QUESTION_LOADER, null,
                            QuestionActivity.this);
                } else {
                    mQuestions = questionList;
                    questionPageAdapter = new QuestionPageAdapter(getSupportFragmentManager());
                    mPager.setAdapter(questionPageAdapter);
                }
            }

            @Override
            public void onError() {
                Toast.makeText(QuestionActivity.this, R.string.error_while_retrieving_question_list,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onEmptyListReceived() {
                Toast.makeText(QuestionActivity.this, R.string.error_no_more_question,
                        Toast.LENGTH_SHORT).show();
            }
        });
        task.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_add_question) {
            openAddQuestionActivity();
            return true;
        }

        if (id == R.id.action_logout) {
            logout();
            return true;
        }

        if (id == R.id.action_load_more) {
            getNextQuestionPage();
            return true;
        }

        if (id == android.R.id.home) {
            searchMode = false;

            tag = "";
            LoaderManager lm = getLoaderManager();
            lm.restartLoader(QuestionActivity.ID_QUESTION_LOADER, null, QuestionActivity.this);
            getActionBar().setTitle(R.string.title_activity_question);
            getActionBar().setDisplayHomeAsUpEnabled(false);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        Question.removeQuestionsHistory(this);
        eraseUserFromSharedPreference();
        this.finish();
    }

    private void eraseUserFromSharedPreference() {
        SharedPreferences prefs = this.getSharedPreferences(
                LoginActivity.SHARED_PREFS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(LoginActivity.USER_ID_KEY, LoginActivity.NO_USER);
        editor.commit();
    }

    protected void openAddQuestionActivity() {
        Log.d(TAG,"Inside openAddQuestionActivity");
        Intent openAddQuestion = new Intent(QuestionActivity.this, AddQuestionActivity.class);
        if(mUser==null){Log.d(TAG,"I should have asked him to use facebook login");}
        openAddQuestion.putExtra(QuestionActivity.ID_USER, UserController.getInstance().getUserId());
        startActivity(openAddQuestion);
    }

    @Override
    public List<Question> getQuestions() {
        return mQuestions;
    }

    @Override
    public Question getQuestionAt(int position) {
        return mQuestions.get(position);
    }

    @Override
    public void addQuestions(List<Question> questions) {
        for (Question q : questions) {
            if (!mQuestions.contains(q)) {
                mQuestions.add(q);
            }
            questionPageAdapter = new QuestionPageAdapter(getSupportFragmentManager());
            mPager.setAdapter(questionPageAdapter);
        }
    }


    protected void setDataLoadedListener() {
        mPager.setOnSwipeOutListener(new CustomViewPager.OnSwipeOutListener() {
            @Override
            public void onSwipeOutAtStart() {
                Log.d(TAG, "Swiped out at start");
            }

            @Override
            public void onSwipeOutAtEnd() {
                Log.d(TAG, "Swiped out at end");
                getNextQuestionPage();
            }
        });
    }

    @Override
    public void updateQuestionWithAnswer(Question question, String answerId) {
        int questionIndex = mQuestions.indexOf(question);
        Question q = mQuestions.get(questionIndex);
        q.setUserAnswer(answerId);
        q.setNumberOfTimeAnswered(q.getNumberOfTimeAnswered() + 1);
        for (Question.Answer a : q.getAnswers()) {
            if (a.getIdAnswer().equals(answerId)) {
                a.setNumberOfTimeChosen(a.getNumberOfTimeChosen() + 1);
            }
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, ContentProviderContract.Questions.URI,
                ContentProviderContract.Questions.FULL_PROJECTION,
                TableQuestion.QUESTION + " <> ?", new String[]{TableQuestion.NO_TEXT},
                TableQuestion.LATEST_UPDATE + " DESC  " +
                        " LIMIT " + (questionPage) * GetQuestion.SERVER_PAGE_SIZE);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mQuestions.clear();
        Log.d(TAG, "Cursor size for the list of question " + data.getCount());
        while (data.moveToNext()) {
            Question q = Question.loadFromCursor(data);
            mQuestions.add(q);
        }
        questionPageAdapter = new QuestionPageAdapter(getSupportFragmentManager());
        mPager.setAdapter(questionPageAdapter);
        setDataLoadedListener();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    private class QuestionPageAdapter extends FragmentStatePagerAdapter {

        public QuestionPageAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
            Log.d(TAG, "Get item : " + i);
            FragmentQuestionPage f = FragmentQuestionPage.newInstance(i);
            return f;
        }

        @Override
        public int getCount() {
            return mQuestions.size();
        }
    }

    public static class FragmentQuestionPage extends Fragment {

        public static final String KEY_POSITION = "position";

        QuestionManager mQuestionManger;

        UserManager mUserManger;

        Context mContext;

        private Question mQuestion;

        @Bind(R.id.tv_question)
        TextView question;

        @Bind(R.id.but_answer1)
        Button answer1;

        @Bind(R.id.but_answer2)
        Button answer2;

        @Bind(R.id.but_answer3)
        Button answer3;

        @Bind(R.id.but_answer4)
        Button answer4;

        @Bind(R.id.progress_answer1)
        ProgressBar progress1;

        @Bind(R.id.progress_answer2)
        ProgressBar progress2;

        @Bind(R.id.progress_answer3)
        ProgressBar progress3;

        @Bind(R.id.progress_answer4)
        ProgressBar progress4;

        AnswerRequest mAnswerRequest = new AnswerRequest();

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            mQuestionManger = (QuestionManager) activity;
            mUserManger = (UserManager) activity;
            mContext = activity;
        }

        public static FragmentQuestionPage newInstance(int position) {
            FragmentQuestionPage questionPage = new FragmentQuestionPage();
            Bundle arguments = new Bundle();
            arguments.putInt(KEY_POSITION, position);
            questionPage.setArguments(arguments);
            return questionPage;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_question_page, container, false);
            ButterKnife.bind(this, view);
            displayQuestionData();
            return view;
        }

        protected void displayQuestionData() {
            int position = getArguments().getInt(KEY_POSITION);
            mQuestion = mQuestionManger.getQuestionAt(position);
            question.setText(mQuestion.getQuestion());
            Log.d(TAG,"this question id is " +  mQuestion.getId());

            if (mQuestion.getAnswerAt(0).getAnswer().length() == 0)
                answer1.setVisibility(View.GONE);
            else answer1.setVisibility(View.VISIBLE);

            if (mQuestion.getAnswerAt(1).getAnswer().length() == 0)
                answer2.setVisibility(View.GONE);
            else answer1.setVisibility(View.VISIBLE);

            if (mQuestion.getAnswerAt(2).getAnswer().length() == 0)
                answer3.setVisibility(View.GONE);
            else answer3.setVisibility(View.VISIBLE);

            if (mQuestion.getAnswerAt(3).getAnswer().length() == 0)
                answer4.setVisibility(View.GONE);
            else answer4.setVisibility(View.VISIBLE);

            answer1.setText(mQuestion.getAnswerAt(0).getAnswer());
            answer2.setText(mQuestion.getAnswerAt(1).getAnswer());
            answer3.setText(mQuestion.getAnswerAt(2).getAnswer());
            answer4.setText(mQuestion.getAnswerAt(3).getAnswer());

            mAnswerRequest.setIdQuestion(mQuestion.getId());
            mAnswerRequest.setIdUser(mUserManger.getUser().getId());
            String answeredId = mQuestion.getUserAnswer(mContext);
            Log.d(TAG, "------------------ Question at " + position +
                    "-----" + mQuestion.toString() + "----------");
            if (answeredId != null && !answeredId.equals("-1")) {
                mAnswerRequest.setIdAnswer(answeredId);
                displayAnswerStat();
            }
        }

        @OnClick(R.id.but_answer1)
        public void setAnswer1() {
            mAnswerRequest.setIdAnswer(mQuestion.getAnswerAt(0).getIdAnswer());
            sendAnswerToServer();
        }

        @OnClick(R.id.but_answer2)
        public void setAnswer2() {
            mAnswerRequest.setIdAnswer(mQuestion.getAnswerAt(1).getIdAnswer());
            sendAnswerToServer();
        }

        @OnClick(R.id.but_answer3)
        public void setAnswer3() {
            mAnswerRequest.setIdAnswer(mQuestion.getAnswerAt(2).getIdAnswer());
            sendAnswerToServer();
        }


        @OnClick(R.id.but_answer4)
        public void setAnswer4() {
            mAnswerRequest.setIdAnswer(mQuestion.getAnswerAt(3).getIdAnswer());
            sendAnswerToServer();
        }

        public void sendAnswerToServer() {
            PostAnswer postAnswer = new PostAnswer();
            postAnswer.setHttpClient(new HttpClient());
            postAnswer.setQuestion(mQuestion);
            postAnswer.setContext(this.getActivity());
            postAnswer.setUserId(mUserManger.getUser().getId());
            postAnswer.setAnswerChoosen(mAnswerRequest.getIdAnswer());
            Log.d(TAG,"we are trying to send data to server");
            postAnswer.setCallback(new PostAnswer.OnAnswerPosted() {
                @Override
                public void onAnswerPost(Question q) {
                    // Update the Question in memory
                    mQuestionManger.updateQuestionWithAnswer(q, mAnswerRequest.getIdAnswer());
                    // Update the number of question answered by user
                    mUserManger.incrementQuestionAnswered();
                    // Update the question in the local database
                    updateQuestionInDb(q, mAnswerRequest.getIdAnswer());
                    displayAnswerStat();
                }

                @Override
                public void onError() {

                }
            });

            postAnswer.execute();
        }

        public void displayAnswerStat() {
            answer1.setEnabled(false);
            answer2.setEnabled(false);
            answer3.setEnabled(false);
            answer4.setEnabled(false);

            Log.d(TAG, "Answer stats : -- total  " + mQuestion.getNumberOfTimeAnswered() + " --"
                    + "-- Answer 1 : " + mQuestion.getAnswers().get(0).getNumberOfTimeChosen() + " " + mQuestion.getAnswers().get(0).toString() + "--"
                    + "-- Answer 2 : " + mQuestion.getAnswers().get(1).getNumberOfTimeChosen() + " " + mQuestion.getAnswers().get(1).toString() + "--"
                    + "-- Answer 3 : " + mQuestion.getAnswers().get(2).getNumberOfTimeChosen() + " " + mQuestion.getAnswers().get(2).toString() + "--"
                    + "-- Answer 4 : " + mQuestion.getAnswers().get(3).getNumberOfTimeChosen() + " " + mQuestion.getAnswers().get(3).toString() + "--"
            );


            if (mQuestion.getUserAnswer(mContext).equals(mQuestion.getAnswers().get(0).getIdAnswer())) {
                answer1.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            }
            if (mQuestion.getUserAnswer(mContext).equals(mQuestion.getAnswers().get(1).getIdAnswer())) {
                answer2.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            }
            if (mQuestion.getUserAnswer(mContext).equals(mQuestion.getAnswers().get(2).getIdAnswer())) {
                answer3.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            }
            if (mQuestion.getUserAnswer(mContext).equals(mQuestion.getAnswers().get(3).getIdAnswer())) {
                answer4.setBackgroundColor(getActivity().getResources().getColor(R.color.colorPrimaryDark));
            }
            Log.d(TAG,"the value0"+ mQuestion.getAnswerAt(0).getNumberOfTimeChosen());
            Log.d(TAG,"the value1"+ mQuestion.getAnswerAt(1).getNumberOfTimeChosen());
            Log.d(TAG,"the value2"+ mQuestion.getAnswerAt(2).getNumberOfTimeChosen());
            Log.d(TAG,"the value3"+ mQuestion.getAnswerAt(3).getNumberOfTimeChosen());


            float answer1Percent = ((float) mQuestion.getAnswerAt(0).getNumberOfTimeChosen() /
                    (float) mQuestion.getNumberOfTimeAnswered()) * 100.0f;

            float answer2Percent = ((float) mQuestion.getAnswerAt(1).getNumberOfTimeChosen() /
                    (float) mQuestion.getNumberOfTimeAnswered()) * 100.0f;

            float answer3Percent = ((float) mQuestion.getAnswerAt(2).getNumberOfTimeChosen() /
                    (float) mQuestion.getNumberOfTimeAnswered()) * 100.0f;

            float answer4Percent = ((float) mQuestion.getAnswerAt(3).getNumberOfTimeChosen() /
                    (float) mQuestion.getNumberOfTimeAnswered()) * 100.0f;

            DecimalFormat df = new DecimalFormat("##.#");
            answer1.setText(mQuestion.getAnswerAt(0).getAnswer()
                    + " (" + df.format(answer1Percent) + "%)");
            answer2.setText(mQuestion.getAnswerAt(1).getAnswer()
                    + " (" + df.format(answer2Percent) + "%)");
            answer3.setText(mQuestion.getAnswerAt(2).getAnswer()
                    + " (" + df.format(answer3Percent) + "%)");
            answer4.setText(mQuestion.getAnswerAt(3).getAnswer()
                    + " (" + df.format(answer4Percent) + "%)");

            if (mQuestion.getAnswerAt(0).getAnswer().length() == 0)
                progress1.setVisibility(View.GONE);
            else progress1.setVisibility(View.VISIBLE);

            if (mQuestion.getAnswerAt(1).getAnswer().length() == 0)
                progress2.setVisibility(View.GONE);
            else progress2.setVisibility(View.VISIBLE);

            if (mQuestion.getAnswerAt(2).getAnswer().length() == 0)
                progress3.setVisibility(View.GONE);
            else progress3.setVisibility(View.VISIBLE);

            if (mQuestion.getAnswerAt(3).getAnswer().length() == 0)
                progress4.setVisibility(View.GONE);
            else progress4.setVisibility(View.VISIBLE);

            progress1.setMax(mQuestion.getNumberOfTimeAnswered());
            progress1.setProgress(mQuestion.getAnswerAt(0).getNumberOfTimeChosen());

            progress2.setMax(mQuestion.getNumberOfTimeAnswered());
            progress2.setProgress(mQuestion.getAnswerAt(1).getNumberOfTimeChosen());

            progress3.setMax(mQuestion.getNumberOfTimeAnswered());
            progress3.setProgress(mQuestion.getAnswerAt(2).getNumberOfTimeChosen());

            progress4.setMax(mQuestion.getNumberOfTimeAnswered());
            progress4.setProgress(mQuestion.getAnswerAt(3).getNumberOfTimeChosen());
        }

        public void updateQuestionInDb(Question questionAnswered, String answerId) {
            ContentResolver cr = getActivity().getContentResolver();
            ContentValues cv = new ContentValues();
            cv.put(TableQuestion.NUMBER_OF_TIME_ANSWER, questionAnswered.getNumberOfTimeAnswered());
            cv.put(TableQuestion.ANSWER1_COUNT, questionAnswered.getAnswerAt(0).getNumberOfTimeChosen());
            cv.put(TableQuestion.ANSWER2_COUNT, questionAnswered.getAnswerAt(1).getNumberOfTimeChosen());
            cv.put(TableQuestion.ANSWER3_COUNT, questionAnswered.getAnswerAt(2).getNumberOfTimeChosen());
            cv.put(TableQuestion.ANSWER4_COUNT, questionAnswered.getAnswerAt(3).getNumberOfTimeChosen());
            cv.put(TableQuestion.USER_ANSWER, answerId);
            cr.update(ContentProviderContract.Questions.URI, cv, TableQuestion.ID + " = ? ",
                    new String[]{questionAnswered.getId()});
            Log.d(TAG, "Update question for id " + questionAnswered.getId() + " answer id " + answerId);
        }


    }

}

/*public class QuestionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
    }
 }*/
