package com.example.help4u;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;

public class JobDetails extends AppCompatActivity {
    TextView jobName; //job name
    TextView jobSmallDesc; //company name
    TextView jobFullDesc; //long job description
    TextView jobSalary;
    ImageView compLogo;
    TextView compAddress;
    TextView compEmail;

    // Declare Wish List Button
    private Button mbtn_wishlist;

    // Temporary variable to hold position in Job List
    private String mStringposition;

    // Get Drawables Value
    private int mGetDrawable;

    // Database Reference
    private DatabaseReference reference;
    private DatabaseReference reference2;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Storage Reference
    private StorageReference storageReference;
    private StorageTask uploadTask;

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

        // get mposition value from intent
        mStringposition = (intent.getStringExtra("position"));

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

        // Assign Wishlist button
        mbtn_wishlist = findViewById(R.id.btn_wishlist);
        mbtn_wishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddToWishList();
            }
        });

        // Start Auth Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();

        // Storage Reference
        storageReference = FirebaseStorage.getInstance().getReference("companylogo");

        // Get image Drawable
        mGetDrawable = intent.getIntExtra("comp_Logo",0);
    }

    private void LaunchEmail() {


    

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
        intent.putExtra( CalendarContract.Events.TITLE, getResources().getString( R.string.job_interview) + " - " + jobName.getText() );
        intent.putExtra( CalendarContract.Events.EVENT_LOCATION, compAddress.getText() );
        intent.putExtra( CalendarContract.Events.DESCRIPTION, jobSmallDesc.getText() );

        //set event begin & end time and duration
        intent.putExtra( CalendarContract.EXTRA_EVENT_ALL_DAY, true );
        intent.putExtra( CalendarContract.EXTRA_EVENT_BEGIN_TIME, dateTime.getTimeInMillis() );
        intent.putExtra( CalendarContract.EXTRA_EVENT_END_TIME, dateTime.getTimeInMillis() );

        startActivity( intent );
    }

    // Convert Image into Mime
    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    // Store Job Details to wish list using firebase database
    private void AddToWishList() {
        Toast.makeText(this, getResources().getString( R.string.adding_to_wishlist_toast),
                Toast.LENGTH_SHORT).show();
        reference = FirebaseDatabase.getInstance().getReference("jobwishlist").child(mFirebaseUser.getUid()).child(mStringposition);

        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("position", mStringposition);
        hashMap.put("photourl", "");
        hashMap.put("jobtitle", jobSmallDesc.getText().toString().trim());
        hashMap.put("jobdescription", jobFullDesc.getText().toString().trim());
        reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                // Successfully Added to Wish List
                if(task.isSuccessful()) {
                    Toast.makeText(JobDetails.this, getResources().getString( R.string.add_to_wishlist_successful),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        Intent intent = new Intent(JobDetails.this, JobList.class);
        startActivity(intent);

        // Get uri of drawable - Store image to database from drawables
        Resources res = getResources();

        Uri resUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE +
                "://" + res.getResourcePackageName(mGetDrawable)
                + '/' + res.getResourceTypeName(mGetDrawable)
                + '/' + res.getResourceEntryName(mGetDrawable));

        if (resUri != null){
            final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                    +"."+getFileExtension(resUri));

            uploadTask = fileReference.putFile(resUri);
            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return fileReference.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUri = task.getResult();
                        String mUri = downloadUri.toString();

                        reference2 = FirebaseDatabase.getInstance().getReference("jobwishlist").child(mFirebaseUser.getUid()).child(mStringposition);
                        HashMap<String, Object> map = new HashMap<>();
                        map.put("photourl", mUri);
                        reference2.updateChildren(map);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    // Display Failure Message
                }
            });
        }
    }
}
