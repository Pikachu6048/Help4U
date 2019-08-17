package com.example.help4u;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/*
* Main page for career test, to navigate to CareerQuestionnaire or CareerTestResult
**/
public class CareerTest extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_career_test);

        //navigate to Career Questionnaire activity
        Button takeTestButton = findViewById( R.id.button_take_test );
        takeTestButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View v){
                launchCareerQuestionnaire();
            }
        } );

        //navigate to Career Test Result activity
        Button testResultButton = findViewById( R.id.button_test_result );
        testResultButton.setOnClickListener( new View.OnClickListener() {
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
        Intent intent = new Intent(this, Qualification.class );
        startActivity(intent);
    }

    private void launchCareerTestResult(){
        Intent intent = new Intent( this, CareerTestResult.class );

        //to indicate user is directly navigate to Test Result page from Career Test main page
        intent.putExtra( CareerTestResult.PREVIOUS_ACTIVITY, "CareerTest" );

        startActivity(intent);
    }
}
