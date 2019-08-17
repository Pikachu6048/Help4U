package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
 * Created by YeuHarn
 * An activity to display career test question in fragments
 * related classes: CareerQuestionnaire, QuestionFragment, QuestionStatePagerAdapter, TestQuestionAdapter
 * related layouts: activity_career_questionnaire, fragment_question, layout_test_question(layout template for one test question)
 * */

public class CareerQuestionnaire extends AppCompatActivity {

    public static final int TOTAL_QUESTIONS = 20; //total number of test questions
    public static final int DEFAULT_ANSWER = -1; //default value for questions that have not been answered yet

    public static ArrayList<Integer> selectedAnswer; //to record user's selected answer throughout the test
    public static int currentPageNumber; //to keep track on which page of questions user is answering

    private ProgressBar mProgressBar;
    private QuestionStatePagerAdapter mPagerAdapter; //adapter for ViewPager
    private ViewPager mViewPager; //fragment container
    private Button mNextButton; //to let user navigate to next page question

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_career_questionnaire );

        //initialize selectedAnswer
        selectedAnswer = new ArrayList<>();
        for(int i = 0; i < TOTAL_QUESTIONS; i++)
            selectedAnswer.add(DEFAULT_ANSWER); //questions that haven't answer will be -1

        currentPageNumber = 0;

        mProgressBar = findViewById( R.id.progressBar_test_questions );

        mPagerAdapter = new QuestionStatePagerAdapter( getSupportFragmentManager() );
        mViewPager = findViewById( R.id.question_container );
        this.setupViewPager( mViewPager );

        mNextButton = findViewById( R.id.button_next_question );
        mNextButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int answeredQues = 0; //to keep track the number of answered questions
                for(int i = 0; i < selectedAnswer.size(); i++){
                    if(selectedAnswer.get(i) != DEFAULT_ANSWER)
                        answeredQues++;
                }

                //check if all question answered in current page, before moving to next page
                //if user is in last page
                if(mNextButton.getText().equals( getResources().getString( R.string.submit ) ) && answeredQues == TOTAL_QUESTIONS){
                    launchTestResult(); //go to career test result activity
                }
                //if user already answered all questions in the page
                else if(answeredQues == ((currentPageNumber + 1) * 5)){
                    mViewPager.setCurrentItem( ++currentPageNumber, true ); //go to next page

                    mProgressBar.setProgress( mProgressBar.getProgress() + 25 ); //increase progress bar

                    //if user is in last page of questions
                    if(mProgressBar.getProgress() == 100){
                        mNextButton.setText( getResources().getString( R.string.submit) ); //set button text to "Submit"
                    }
                }
                //user didn't answered all questions in the page
                else{
                    Toast.makeText( getApplicationContext(), getResources().getString( R.string.please_answer_all_question), Toast.LENGTH_SHORT ).show();
                }
            }
        } );
    }

    //to create fragments for test questions, 1 fragment(page) 5 test questions, total 4 fragments
    private void setupViewPager(ViewPager viewPager){
        String[] testQuestions = getResources().getStringArray( R.array.questions ); //get test questions

        //setup question fragments
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 0 ) );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 1 ) );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 2 ) );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 3 ) );

        viewPager.setAdapter( mPagerAdapter );
    }

    private void launchTestResult(){
        Intent intent = new Intent( this, CareerTestResult.class );

        //to indicate user is directly navigate to Test Result page from Career Test main page
        intent.putExtra( CareerTestResult.PREVIOUS_ACTIVITY, "CareerQuestionnaire" );

        String qualification = getIntent().getStringExtra( Qualification.QUALIFICATION );
        intent.putExtra( Qualification.QUALIFICATION, qualification );

        startActivity( intent );
    }

/*
 * A adapter class that use to manage activity_career_questionnaire > question_container
 * A class for navigating between questions fragments
 * */
    private class QuestionStatePagerAdapter extends FragmentStatePagerAdapter {

        private List<Fragment> mFragmentList;

        public QuestionStatePagerAdapter(FragmentManager fm) {
            super( fm );
            mFragmentList = new ArrayList<>();
        }

        public void addFragment(Fragment fragment){
            mFragmentList.add(fragment);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get( position );
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }
    }
}
