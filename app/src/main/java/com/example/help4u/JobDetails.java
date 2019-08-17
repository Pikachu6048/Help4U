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
    TextView applyJob;
    TextView compEmail;

    String companyName;
    String companyAddress;
    String jobNameinEmail;
    String companyEmail;
    private Button mNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_details);
//find the text view or image view id
        jobName = findViewById(R.id.companyName);
        jobSmallDesc = findViewById(R.id.companySmallDescription);
        jobFullDesc = findViewById(R.id.companyFullDescription);
        jobSalary = findViewById(R.id.job_salary);
        compLogo = findViewById(R.id.comp_logo);
        compAddress = findViewById(R.id.comp_address);
        compEmail = findViewById(R.id.companyEmail);


        Intent intent = getIntent();
//set text
        jobName.setText(intent.getStringExtra("jobName"));
        jobSmallDesc.setText(intent.getStringExtra("jobDesc"));
        jobFullDesc.setText(intent.getStringExtra("jobFullDesc"));
        compLogo.setImageResource(intent.getIntExtra("comp_Logo",0));
        compEmail.setText(intent.getStringExtra("compEmail"));
        jobSalary.setText(intent.getStringExtra("salary"));
        compAddress.setText(intent.getStringExtra("comp_Address"));
        companyName= (intent.getStringExtra("compName"));
        companyAddress= (intent.getStringExtra("comp_Address"));
        companyEmail =(intent.getStringExtra("compEmail"));
        jobNameinEmail = (intent.getStringExtra("jobName"));
        compAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchNavigation();
            }
        });

        compEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchEmail();
            }
        });

    }

    private void LaunchEmail() {


        //   emailIntent.setPackage("com.google.android.apps.email");

        String recipient[] = {companyEmail};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,recipient);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Regarding the issue about the job - " + jobNameinEmail );
        emailIntent.putExtra(Intent.EXTRA_TEXT,"I would like to inquire more job details about this job");


        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Choose an email client"));
    }

    private void LaunchNavigation() {

        Uri getAddress = Uri.parse("geo:0,0?q=" + companyAddress );
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, getAddress);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }
}
