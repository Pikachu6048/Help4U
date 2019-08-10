package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

/*
* Created by YeuHarn
* An activity to display user's career test result and compute recommended job
* */

public class CareerTestResult extends AppCompatActivity {

    private TextView mTestResult;
    private TextView mRecommendedJob;
    private Button mMoreJobButton;

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
    }

    private String getTestResult(){
        /*
        * ***check if test result exist in database/local storage
        * if test result exist, return test result
        * else if all element in CareerQuestionnaire.selectedAnswer != (-1), compute test result
        * else return "Test Result not available"
        *
        **/

        return "Test Result not available";
    }

    private String getRecommendedJob(){
        /*
        *  ***get test result
        *  if test result available, compute recommended job based on test result
        *  else return "Recommended Job not available"
        * */
        return "Recommended Job not available";
    }

    private void launchJobList(){
        Intent intent = new Intent( this, JobList.class );

        //intent.putExtra("com.example.help4U.RecommendedJob", %#recommendedJob#%);
        //*****optional

        startActivity( intent );
    }
}
