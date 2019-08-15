package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class JobDetails extends AppCompatActivity {
    TextView jobName;
    TextView jobSmallDesc;
    TextView jobFullDesc;
    TextView jobSalary;
    ImageView compLogo;
    TextView compAddress;

    String companyName;
    String companyAddress;

    private Button mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);

        jobName = findViewById(R.id.companyName);
        jobSmallDesc = findViewById(R.id.companySmallDescription);
        jobFullDesc = findViewById(R.id.companyFullDescription);
        jobSalary = findViewById(R.id.job_salary);
        compLogo = findViewById(R.id.comp_logo);
        compAddress = findViewById(R.id.comp_address);

        Intent intent = getIntent();

        jobName.setText(intent.getStringExtra("jobName"));
        jobSmallDesc.setText(intent.getStringExtra("jobDesc"));
        jobFullDesc.setText(intent.getStringExtra("jobFullDesc"));
        compLogo.setImageResource(intent.getIntExtra("comp_Logo",0));
        jobSalary.setText(intent.getStringExtra("salary"));
        compAddress.setText(intent.getStringExtra("comp_Address"));
        companyName= (intent.getStringExtra("compName"));
        companyAddress= (intent.getStringExtra("comp_Address"));
        mNavigation = findViewById(R.id.btn_navigation);
        mNavigation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNavigation();
            }
        });
    }

    private void LaunchNavigation() {

        Uri getAddress = Uri.parse("geo:0,0?q=" + companyAddress );
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, getAddress);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }
}
