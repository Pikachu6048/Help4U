package com.example.help4u;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class JobDetails extends AppCompatActivity {
    TextView jobName; //job name
    TextView jobSmallDesc; //company name
    TextView jobFullDesc; //long job description
    TextView jobSalary;
    ImageView compLogo;
    TextView compAddress;
    TextView applyJob;
    TextView compEmail;

    private Button mNavigation;

    //member variables for set reminder function
    private Calendar mReminderDateTime; //to store user selected date and time
    private Activity thisActivity; //to store reference of current activity for launching DatePickerDialog and TimePickerDialog

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

        //"Set Reminder" button for user to set an job interview event in device calendar
        //a date picker dialog will first display to user
        //after that, a time picker dialog will be displayed
        //and lastly the device calendar app will be launched
        thisActivity = this;
        Button setReminder = findViewById( R.id.btn_reminder );
        setReminder.setOnClickListener( setReminderListener );
    }

    private void LaunchEmail() {


        //   emailIntent.setPackage("com.google.android.apps.email");

        String recipient[] = {compEmail.getText().toString()};

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.putExtra(Intent.EXTRA_EMAIL,recipient);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT,"Regarding the issue about the job - " + jobName.getText() );
        emailIntent.putExtra(Intent.EXTRA_TEXT,"I would like to inquire more job details about this job");


        emailIntent.setType("message/rfc822");
        startActivity(Intent.createChooser(emailIntent,"Choose an email client"));
    }

    private void LaunchNavigation() {

        Uri getAddress = Uri.parse("geo:0,0?q=" + compAddress.getText() );
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, getAddress);
        mapIntent.setPackage("com.google.android.apps.maps");
        startActivity(mapIntent);

    }

    //button listener for "Set Reminder" button
    private final Button.OnClickListener setReminderListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            mReminderDateTime = Calendar.getInstance(); //get current system date and time

            //display date picker dialog
            //parameters: current activity, date set listener, current year, current month, current day of month
            new DatePickerDialog( thisActivity, mDateSetListener, mReminderDateTime.get( Calendar.YEAR ),
                    mReminderDateTime.get(Calendar.MONTH), mReminderDateTime.get(Calendar.DAY_OF_MONTH)).show();
        }
    };

    //date set listener for date picker dialog
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
            //record selected date
            mReminderDateTime.set( Calendar.YEAR, year );
            mReminderDateTime.set( Calendar.MONTH, month );
            mReminderDateTime.set( Calendar.DAY_OF_MONTH, dayOfMonth );

            //a time picker dialog will be displayed after user selected a date
            //parameters: current activity, time set listener, current hour, current minute
            new TimePickerDialog(thisActivity, mTimeSetListener, mReminderDateTime.get(Calendar.HOUR_OF_DAY),
                    mReminderDateTime.get(Calendar.MINUTE), false).show();
        }
    };

    //time set listener for time picker dialog
    private final TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hour, int minute) {
            //record selected time
            mReminderDateTime.set(Calendar.HOUR_OF_DAY, hour);
            mReminderDateTime.set(Calendar.MINUTE, minute);

            //launch device calendar intent after user selected a time
            launchCalendarIntent( mReminderDateTime );
        }
    };

    //to launch the default Calendar app in user device
    private void launchCalendarIntent(Calendar dateTime){
        Intent intent = new Intent( Intent.ACTION_INSERT ); //insert a new event into calendar
        intent.setType( "vnd.android.cursor.item/event" );

        //set event title, location(address) and description(company name)
        intent.putExtra( CalendarContract.Events.TITLE, "Job Interview - " + jobName.getText() );
        intent.putExtra( CalendarContract.Events.EVENT_LOCATION, compAddress.getText() );
        intent.putExtra( CalendarContract.Events.DESCRIPTION, jobSmallDesc.getText() );

        //set event begin & end time and duration
        intent.putExtra( CalendarContract.EXTRA_EVENT_ALL_DAY, true );
        intent.putExtra( CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateTime.getTimeInMillis() );
        intent.putExtra( CalendarContract.EXTRA_EVENT_END_TIME, dateTime.getTimeInMillis() );

        startActivity( intent );
    }

}
