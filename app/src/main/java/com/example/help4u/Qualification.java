package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

/*
* Created by YeuHarn
* An activity for user to choose the qualification before taking career test
* */
public class Qualification extends AppCompatActivity {

    public static final String QUALIFICATION = "com.example.help4u.Qualification"; //to transfer user qualification to following activities

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_qualification );

        //set "IT" as user qualification
        Button qualiIT = findViewById( R.id.button_quali_IT );
        qualiIT.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCareerQuestionnaire( Job.IT_CATEGORY_DESC );
            }
        } );

        //set "Business" as user qualification
        Button qualiBusiness = findViewById( R.id.button_quali_business );
        qualiBusiness.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCareerQuestionnaire( Job.BUSINESS_CATEGORY_DESC );
            }
        } );
    }

    private void launchCareerQuestionnaire(String qualification){
        Intent intent = new Intent( this, CareerQuestionnaire.class );
        intent.putExtra( QUALIFICATION, qualification ); //transfer the selected qualification to following activity
        startActivity( intent );
    }
}
