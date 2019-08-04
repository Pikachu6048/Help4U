package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CareerTest extends AppCompatActivity {

    private Button mTakeTestButton;

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
    }

    //button onClick listener by YeuHarn
    private void launchCareerQuestionnaire(){
        Intent intent = new Intent(this, CareerQuestionnaire.class );
        startActivity(intent);
    }
}
