package com.example.help4u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

import static com.example.help4u.Job.ACCOUNTANT;
import static com.example.help4u.Job.DATA_SCIENTIST;
import static com.example.help4u.Job.ENTREPRENEUR;
import static com.example.help4u.Job.IT_CATEGORY_DESC;
import static com.example.help4u.Job.PROGRAMMER;
import static com.example.help4u.Job.TOTAL_NUM_OF_JOB;

/*
* Created by YeuHarn
* An activity to display user's career test result and compute recommended job, save and load test results from user local device ( SharedPreferences )
* */

public class CareerTestResult extends AppCompatActivity {

    private TextView mITLabel;
    private TextView mBusinessLabel;
    private ProgressBar mITScore;
    private ProgressBar mBusinessScore;
    private TextView mITPercent;
    private TextView mBusinessPercent;
    private TextView mNotAvailable;

    private TextView mRecommendedJobTitle; //to display computed recommended job title
    private TextView mRecommendedJobDesc; //to display computed recommended job description
    private int[] testResult; //to store test answers
    private String userQualification; //to store user's qualification

    public static final String SHARED_PREFS = "com.example.help4u.TestResult"; //SharedPreferences name
    public static final String SHARED_PREFS_KEY = "Result"; //key to store data into SharedPreferences

    public static final String PREVIOUS_ACTIVITY = "com.example.help4u.PreviousActivity"; //to indicate user navigate to this page from which activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_career_test_result );

        //elements to display test result
        mITLabel = findViewById( R.id.textView_IT_label );
        mBusinessLabel = findViewById( R.id.textView_Business_label );
        mITScore = findViewById( R.id.progressBar_IT );
        mBusinessScore = findViewById( R.id.progressBar_Business );
        mITPercent = findViewById( R.id.textView_IT_percentage );
        mBusinessPercent = findViewById( R.id.textView_Business_percentage );
        mNotAvailable = findViewById( R.id.textView_result_not_available );

        this.setTestResult(); //setup view elements for test result and get userQualification for setRecommendedJob method

        //elements to display recommended job
        mRecommendedJobTitle = findViewById( R.id.textView_recommended_job_title );
        mRecommendedJobDesc = findViewById( R.id.textView_recommended_job_desc );

        this.setRecommendedJob(userQualification); //setup view elements for recommended job based on the passed in userQualification

        //navigate user to JobList activity if user click on "More Job" button
        Button moreJobButton = findViewById( R.id.button_more_job );
        moreJobButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchJobList();
            }
        } );

        this.saveResult(); //save test results into SharedPreferences
    }

    //to display test result and get user qualification
    private void setTestResult(){
        String previousActivity = getIntent().getStringExtra( PREVIOUS_ACTIVITY );

        //if user navigate to this page directly from Career Test main page
        if(previousActivity.equals( "CareerTest" )){
            testResult = loadResult(); //get test result from SharedPreferences
            userQualification = loadQualification(); //get user qualification from SharedPreferences
        }
        //if user navigate to this page after answering all test questions
        else if(previousActivity.equals( "CareerQuestionnaire" )){
            testResult = this.computeInterestScore( CareerQuestionnaire.selectedAnswer ); //compute interest score from latest test answers
            userQualification = getIntent().getStringExtra(Qualification.QUALIFICATION); //get user qualification from previous activity
        }
        else{
            testResult = null;
        }

        //if test result available, compute interest scores for each job category and visualize in progress bar
        if(testResult != null){
            //show all result progress bar and labels
            mITLabel.setVisibility( View.VISIBLE );
            mBusinessLabel.setVisibility( View.VISIBLE );
            mITScore.setVisibility( View.VISIBLE );
            mBusinessScore.setVisibility( View.VISIBLE );
            mITPercent.setVisibility( View.VISIBLE );
            mBusinessPercent.setVisibility( View.VISIBLE );

            //hide "Test Result not available" TextView
            mNotAvailable.setVisibility( View.INVISIBLE );

            //total up interest scores for each category
            int it = testResult[PROGRAMMER] + testResult[DATA_SCIENTIST];
            int business = testResult[ACCOUNTANT] + testResult[ENTREPRENEUR];

            it = Math.max( it, 0 ); //set to 0 if IT scored negative value
            business = Math.max( business, 0 ); //set to 0 if Business scored negative value

            int sum = it + business;

            //if both categories scored 0, set both progress bar to 0%
            if(sum == 0){
                mITScore.setProgress( 0 );
                mITPercent.setText( "0%" );

                mBusinessScore.setProgress( 0 );
                mBusinessPercent.setText( "0%" );
            }
            //if IT category scored 0, set Business progress bar to 100%
            else if(it == 0){
                mITScore.setProgress( 0 );
                mITPercent.setText( "0%" );

                mBusinessScore.setProgress( 100 );
                mBusinessPercent.setText( "100%" );
            }
            //if Business category scored 0, set IT progress bar to 100%
            else if(business == 0){
                mITScore.setProgress( 100 );
                mITPercent.setText( "100%" );

                mBusinessScore.setProgress( 0 );
                mBusinessPercent.setText( "0%" );
            }
            //else both categories scored more than 0, calculate relative percentage and set progress bar
            else{
                mITScore.setProgress( it * 100 / sum );
                mITPercent.setText( it * 100 / sum + "%" );

                mBusinessScore.setProgress( business * 100 / sum );
                mBusinessPercent.setText( business * 100 / sum + "%" );
            }
        }
        //else test result not available
        else{
            //hide all result progress bar
            mITLabel.setVisibility( View.INVISIBLE );
            mBusinessLabel.setVisibility( View.INVISIBLE );
            mITScore.setVisibility( View.INVISIBLE );
            mBusinessScore.setVisibility( View.INVISIBLE );
            mITPercent.setVisibility( View.INVISIBLE );
            mBusinessPercent.setVisibility( View.INVISIBLE );

            //show "Test Result not available" TextView
            mNotAvailable.setVisibility( View.VISIBLE );
        }
    }

    //to calculate interest score for all 4 jobs based on the answers user given in the career test
    private int[] computeInterestScore(ArrayList<Integer> testAnswer){
        //if no test answer to compute, return null
        if(testAnswer == null){
            return null;
        }

        //to record interest score for each job
        int[] interestScore = new int[]{0,0,0,0};

        //score rating for each job in each test question
        int[] score_programmer = getResources().getIntArray( R.array.score_programmer );
        int[] score_dataScientist = getResources().getIntArray( R.array.score_dataScientist );
        int[] score_accountant = getResources().getIntArray( R.array.score_accountant );
        int[] score_entrepreneur = getResources().getIntArray( R.array.score_entrepreneur );

        //calculate total interest score for each job
        for(int i = 0; i < CareerQuestionnaire.TOTAL_QUESTIONS; i++){
            switch(testAnswer.get(i)){
                //if user answer "Very Interested", multiplier = 2
                case R.id.radioButton_answer_vi:
                    interestScore[PROGRAMMER] += score_programmer[i] * 2;
                    interestScore[DATA_SCIENTIST] += score_dataScientist[i] * 2;
                    interestScore[ACCOUNTANT] += score_accountant[i] * 2;
                    interestScore[ENTREPRENEUR] += score_entrepreneur[i] * 2;
                    break;

                //if user answer "Interested", multiplier = 1
                case R.id.radioButton_answer_in:
                    interestScore[PROGRAMMER] += score_programmer[i];
                    interestScore[DATA_SCIENTIST] += score_dataScientist[i];
                    interestScore[ACCOUNTANT] += score_accountant[i];
                    interestScore[ENTREPRENEUR] += score_entrepreneur[i];
                    break;

                //if user answer "Slightly Interested", multiplier = 0.5
                case R.id.radioButton_answer_si:
                    interestScore[PROGRAMMER] += score_programmer[i] / 2;
                    interestScore[DATA_SCIENTIST] += score_dataScientist[i] / 2;
                    interestScore[ACCOUNTANT] += score_accountant[i] / 2;
                    interestScore[ENTREPRENEUR] += score_entrepreneur[i] / 2;
                    break;

                //if user answer "Not Interested", multiplier = (-1)
                case R.id.radioButton_answer_ni:
                    interestScore[PROGRAMMER] += score_programmer[i] * -1;
                    interestScore[DATA_SCIENTIST] += score_dataScientist[i] * -1;
                    interestScore[ACCOUNTANT] += score_accountant[i] * -1;
                    interestScore[ENTREPRENEUR] += score_entrepreneur[i] * -1;
            }
        }

        return interestScore;
    }

    //to compute recommended job based on test result and user's Qualification
    private void setRecommendedJob(String qualification){
        //if test result not available, display "Recommended Job not available"
        if(testResult == null){
            mRecommendedJobTitle.setText( "" );
            mRecommendedJobDesc.setText( R.string.recommended_job_not_available );
            return;
        }

        //else test result available, get job title and job description resources
        String[] jobTitle = getResources().getStringArray( R.array.job_title );
        String[] jobDesc = getResources().getStringArray( R.array.job_desc );

        //check with user's Qualification and get the higher interest score and display relevant job title & job desc
        //if user's Qualification is "IT", check interest score for jobs in IT category
        if(qualification.equals( IT_CATEGORY_DESC )){
            //if user's interest score for "Programmer" is higher than "Data Scientist"
            if(Math.max( testResult[PROGRAMMER], testResult[DATA_SCIENTIST] ) == testResult[PROGRAMMER]){
                mRecommendedJobTitle.setText( jobTitle[PROGRAMMER] );
                mRecommendedJobDesc.setText( jobDesc[PROGRAMMER] );
            }
            else{
                mRecommendedJobTitle.setText( jobTitle[DATA_SCIENTIST] );
                mRecommendedJobDesc.setText( jobDesc[DATA_SCIENTIST] );
            }
        }
        //else user's Qualification is "Business", check interest score for jobs in Business category
        else{
            //if user's interest score for "Accountant" is higher than "Entrepreneur"
            if(Math.max( testResult[ACCOUNTANT], testResult[ENTREPRENEUR] ) == testResult[ACCOUNTANT]){
                mRecommendedJobTitle.setText( jobTitle[ACCOUNTANT] );
                mRecommendedJobDesc.setText( jobDesc[ACCOUNTANT] );
            }
            else{
                mRecommendedJobTitle.setText( jobTitle[ENTREPRENEUR] );
                mRecommendedJobDesc.setText( jobDesc[ENTREPRENEUR] );
            }
        }
    }

    //to save test results into SharedPreferences
    public void saveResult(){
        //if no test result is recorded, exit function
        if(testResult == null){
            return;
        }

        SharedPreferences.Editor editor = getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE ).edit();

        //store user qualification into sharedPreferences
        editor.putString( SHARED_PREFS_KEY + "Qualification", userQualification );

        //store interest score of each job into sharedPreferences
        editor.putInt( SHARED_PREFS_KEY + PROGRAMMER, testResult[PROGRAMMER] );
        editor.putInt( SHARED_PREFS_KEY + DATA_SCIENTIST, testResult[DATA_SCIENTIST] );
        editor.putInt( SHARED_PREFS_KEY + ACCOUNTANT, testResult[ACCOUNTANT] );
        editor.putInt( SHARED_PREFS_KEY + ENTREPRENEUR, testResult[ENTREPRENEUR] );

        editor.apply(); //save changes
    }

    //to load user qualification from SharedPreferences
    public String loadQualification(){
        //get user qualification from sharedPreferences, return null if data not exist
        return getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE ).getString( SHARED_PREFS_KEY + "Qualification", null );
    }

    //to load test results from SharedPreferences
    public int[] loadResult(){
        int[] testRes = null;

        SharedPreferences sharedPreferences = getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE );

        //if test result data exists in sharedPreferences
        if(sharedPreferences.contains( SHARED_PREFS_KEY + PROGRAMMER )){
            testRes = new int[TOTAL_NUM_OF_JOB];

            testRes[PROGRAMMER] = sharedPreferences.getInt( SHARED_PREFS_KEY + PROGRAMMER, 0 );
            testRes[DATA_SCIENTIST] = sharedPreferences.getInt( SHARED_PREFS_KEY + DATA_SCIENTIST, 0 );
            testRes[ACCOUNTANT] = sharedPreferences.getInt( SHARED_PREFS_KEY + ACCOUNTANT, 0 );
            testRes[ENTREPRENEUR] = sharedPreferences.getInt( SHARED_PREFS_KEY + ENTREPRENEUR, 0 );
        }

        //else test result not exist in sharedPreferences, return null
        return testRes;
    }

    private void launchJobList(){
        Intent intent = new Intent( this, JobList.class );
        startActivity( intent );
    }
}
