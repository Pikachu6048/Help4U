package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class JobDetails extends AppCompatActivity {
    TextView compName;
    TextView compSmallDesc;
    TextView compFullDesc;

    String companyName;
    String compLat;
    String compLong;
    private Button mNavigation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        compName = findViewById(R.id.companyName);
        compSmallDesc = findViewById(R.id.companySmallDescription);
        compFullDesc = findViewById(R.id.companyFullDescription);

        Intent intent = getIntent();

        compName.setText(intent.getStringExtra("compName"));
        compSmallDesc.setText(intent.getStringExtra("compDesc"));
        compFullDesc.setText(intent.getStringExtra("compFullDesc"));
        companyName= (intent.getStringExtra("compName"));
        compLat = (intent.getStringExtra("compLat"));
        compLong = (intent.getStringExtra("compLong"));
        mNavigation = findViewById(R.id.btn_navigation);
        mNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNavigation();
            }
        });
    }

    private void LaunchNavigation() {

        Intent intent = new Intent(this, Navigation.class);
        intent.putExtra("compName", companyName);
        intent.putExtra("compLat",compLat);
        intent.putExtra("compLong",compLong);
        startActivity(intent);

    }
}
