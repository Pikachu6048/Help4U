package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

/*
* Main page for career test, to navigate to CareerQuestionnaire or CareerTestResult
**/
public class CareerTest extends AppCompatActivity {

    private Button mTakeTestButton; //navigate to Career Questionnaire activity
    private Button mTestResultButton; //navigate to Career Test Result activity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_test);

        mTakeTestButton = (Button) findViewById( R.id.button_take_test );
        mTakeTestButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                launchCareerQuestionnaire();
            }
        } );

        mTestResultButton = (Button) findViewById( R.id.button_test_result );
        mTestResultButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences( CareerTestResult.SHARED_PREFS, Context.MODE_PRIVATE );

                //test result exist in database
                if(sharedPreferences.contains( CareerTestResult.SHARED_PREFS_KEY + 0 )){
                    launchCareerTestResult();
                }
                else{
                    Toast.makeText( getApplicationContext(), getResources().getString( R.string.test_result_not_available), Toast.LENGTH_SHORT ).show();
                }
            }
        } );

    }

    private void launchCareerQuestionnaire(){
        Intent intent = new Intent(this, CareerQuestionnaire.class );
        startActivity(intent);
    }

    private void launchCareerTestResult(){
        Intent intent = new Intent( this, CareerTestResult.class );

        //to indicate user is directly navigate to Test Result page from Career Test main page
        intent.putExtra( CareerTestResult.PREVIOUS_ACTIVITY, "CareerTest" );

        startActivity(intent);
    }
}
