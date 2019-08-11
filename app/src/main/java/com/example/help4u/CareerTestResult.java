package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/*
* Created by YeuHarn
* An activity to display user's career test result and compute recommended job, save and load test results from user local device ( SharedPreferences )
* */

public class CareerTestResult extends AppCompatActivity {

    private TextView mTestResult; //to display the computed test result
    private TextView mRecommendedJob; //to display computed recommended job
    private Button mMoreJobButton;
    private ArrayList<Integer> testResult;

    public static final String SHARED_PREFS = "com.example.help4u.TestResult"; //SharedPreferences name
    public static final String SHARED_PREFS_KEY = "TestAnswer"; //key to store data into SharedPreferences

    public static final String PREVIOUS_ACTIVITY = "com.example.help4U.PreviousActivity"; //to indicate user navigate to this page from which activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_career_test_result );

        mTestResult = (TextView) findViewById( R.id.textView_test_result );
        mTestResult.setText( this.getTestResult() );

        mRecommendedJob = (TextView) findViewById( R.id.textView_recommended_job );
        mRecommendedJob.setText( this.getRecommendedJob() );

        mMoreJobButton = (Button) findViewById( R.id.button_more_job );
        mMoreJobButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchJobList();
            }
        } );

        this.saveResult(); //save test results into SharedPreferences
    }

    //to save test results into SharedPreferences
    public void saveResult(){
        ArrayList<Integer> testAnswers = CareerQuestionnaire.selectedAnswer;

        //if no test answer is recorded, exit function
        if(testAnswers == null){
            return;
        }

        int answeredQues = 0; //to keep track the number of answered questions
        for(int i = 0; i < testAnswers.size(); i++){
            if(testAnswers.get(i) != CareerQuestionnaire.DEFAULT_ANSWER)
                answeredQues++;
        }

        //if all test questions are answered, save test results into sharedPreferences
        if(answeredQues == CareerQuestionnaire.TOTAL_QUESTIONS){
            SharedPreferences sharedPreferences = getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE );
            SharedPreferences.Editor editor = sharedPreferences.edit();

            //store response of each test question into sharedPreferences
            for(int i = 0; i < testAnswers.size(); i++){
                editor.putInt( SHARED_PREFS_KEY + i, testAnswers.get(i) );
            }

            editor.apply(); //save changes
        }
    }

    //to load test results from SharedPreferences
    public ArrayList<Integer> loadResult(){
        ArrayList<Integer> testAnswers = null;

        SharedPreferences sharedPreferences = getSharedPreferences( SHARED_PREFS, Context.MODE_PRIVATE );

        //if test result data exists in sharedPreferences
        if(sharedPreferences.contains( SHARED_PREFS_KEY + 0 )){
            testAnswers = new ArrayList<>();

            for(int i = 0; i < CareerQuestionnaire.TOTAL_QUESTIONS; i++){
                testAnswers.add( sharedPreferences.getInt( SHARED_PREFS_KEY + i, CareerQuestionnaire.DEFAULT_ANSWER ) );
            }
        }

        //else test result not exist in sharedPreferences, return null
        return testAnswers;
    }

    private String getTestResult(){
        String previousActivity = getIntent().getExtras().getString( PREVIOUS_ACTIVITY );

        //if user navigate to this page directly from Career Test main page
        if(previousActivity.equals( "CareerTest" )){
            testResult = loadResult(); //get test result from SharedPreferences
        }
        //if user navigate to this page after answering all test questions
        else if(previousActivity.equals( "CareerQuestionnaire" )){
            testResult = CareerQuestionnaire.selectedAnswer;
        }
        else{
            testResult = null;
        }

        //if test result available
        if(testResult != null){
            //*****compute test result
            return "";
        }
        //else test result not available
        else{
            return getResources().getString( R.string.test_result_not_available );
        }
    }

    private String getRecommendedJob(){
        /*
        *  ***get test result
        *  if test result available, compute recommended job based on test result
        *  else return "Recommended Job not available"
        * */

        //if test result available
        if(testResult != null){
            //*****compute recommended job
            return "";
        }
        //else test result not available
        else{
            return getResources().getString( R.string.recommended_job_not_available);
        }
    }

    private void launchJobList(){
        Intent intent = new Intent( this, JobList.class );

        //intent.putExtra("com.example.help4U.RecommendedJob", %#recommendedJob#%);
        //*****optional, to display jobs related to test result
        //or just display all jobs available in database

        startActivity( intent );
    }
}
