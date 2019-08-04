/*
* Created by YeuHarn
* */

package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ProgressBar;

public class CareerQuestionnaire extends AppCompatActivity {

    private ProgressBar mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_career_questionnaire );

        mProgressBar = (ProgressBar) findViewById( R.id.progressBar_test_questions );
        mProgressBar.setScaleY(3f);
    }
}
