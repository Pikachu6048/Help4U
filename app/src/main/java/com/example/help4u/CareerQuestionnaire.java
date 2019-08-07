package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

/*
 * Created by YeuHarn
 * */

public class CareerQuestionnaire extends AppCompatActivity {

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
        for(int i = 0; i < 20; i++)
            selectedAnswer.add(-1); //questions that haven't answer will be -1

        currentPageNumber = 0;

        mProgressBar = (ProgressBar) findViewById( R.id.progressBar_test_questions );
        mProgressBar.setScaleY(3f); //makes the progress bar thicker
        mProgressBar.setProgress( 25 ); //initially start from 25%

        mPagerAdapter = new QuestionStatePagerAdapter( getSupportFragmentManager() );
        mViewPager = (ViewPager)findViewById( R.id.question_container );
        this.setupViewPager( mViewPager );

        mNextButton = (Button)findViewById( R.id.button_next_question );
        mNextButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int answeredQues = 0; //to keep track the number of answered questions
                for(int i = 0; i < selectedAnswer.size(); i++){
                    if(selectedAnswer.get(i) != -1)
                        answeredQues++;
                }

                //check if all question answered in current page, before moving to next page
                //if user is in last page
                if(mNextButton.getText().equals( "Submit" )){
                    //go to result activity
                    Toast.makeText( getApplicationContext(), "Go to result", Toast.LENGTH_SHORT ).show();
                }
                //if user already answered all questions in the page
                else if(answeredQues == ((currentPageNumber + 1) * 5)){
                    //go to next page
                    mViewPager.setCurrentItem( ++currentPageNumber, true );

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

    private void setupViewPager(ViewPager viewPager){
        String[] testQuestions = getResources().getStringArray( R.array.questions );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 0 ) );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 1 ) );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 2 ) );
        mPagerAdapter.addFragment( QuestionFragment.newInstance( testQuestions, 3 ) );

        viewPager.setAdapter( mPagerAdapter );
    }
}
