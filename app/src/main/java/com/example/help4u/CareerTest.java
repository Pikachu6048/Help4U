package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
* Main page for career test, to navigate to CareerQuestionnaire or CareerTestResult
**/
public class CareerTest extends AppCompatActivity {

    private Button mTakeTestButton;
    private Button mTestResultButton;

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
                //*****check if got test result in database
                launchCareerTestResult();

                //*****else display Toast
            }
        } );

    }

    private void launchCareerQuestionnaire(){
        Intent intent = new Intent(this, CareerQuestionnaire.class );
        startActivity(intent);
    }

    private void launchCareerTestResult(){
        Intent intent = new Intent( this, CareerTestResult.class );
        startActivity(intent);
    }
}
