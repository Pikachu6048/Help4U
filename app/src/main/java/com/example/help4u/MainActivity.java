package com.example.help4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, GoogleApiClient.OnConnectionFailedListener {

    /* Declaration by wenz11*/
    // Added TAG for logging purpose
    private static final String TAG = "MainActivity";

    // Declare variable
    private String mUsername;
    private String mPhotoUrl;
    public static final String ANONYMOUS = "anonymous";
    private GoogleApiClient mGoogleApiClient;

    // Handle storing and retrieving of key value pairs
    private SharedPreferences mSharedPreferences;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
    /* Declaration by wenz11*/

    private Button careerTest;
    private Button JobList;

    // Test btn to other activity by wenz11
    private Button mActivityTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*Sign in operation by wenz11*/
/*        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            startActivity(new Intent(this, SignInActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getDisplayName();
            if (mFirebaseUser.getPhotoUrl() != null) {
                mPhotoUrl = mFirebaseUser.getPhotoUrl().toString();
            }
        }

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this *//* FragmentActivity *//*, this *//* OnConnectionFailedListener *//*)
                .addApi(Auth.GOOGLE_SIGN_IN_API)
                .build();*/
        /*Sign in operation by wenz11*/

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);

        careerTest = findViewById(R.id.nav_career_test);
        careerTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchCareerTest();
            }
        });

        JobList = findViewById(R.id.nav_job_list);
        JobList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LaunchJobList();
            }
        });

        // Activity Test by wenz11
        mActivityTest = findViewById(R.id.btnTest);
        mActivityTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchActivityTest();
            }
        });
    }

    // Test Title Screen Activity by wenz11
    private void LaunchActivityTest() {
        Intent intent = new Intent(this, JobList.class);
        startActivity(intent);
    }

    private void LaunchJobList() {
        Intent intent = new Intent(this, JobList.class);
        startActivity(intent);
    }


    public void LaunchCareerTest() {

        Intent intent = new Intent(this, CareerTest.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

// by wenz11
        switch (item.getItemId()) {
            case R.id.action_sign_out:
                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                mUsername = ANONYMOUS;
                startActivity(new Intent(this, SignInActivity.class));
                finish();
                return true;
            case R.id.action_settings:
                // Add startActivity intent if click on setting
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            // Handle the camera action
        } else if (id == R.id.nav_profile) {

        } else if (id == R.id.nav_wish_list) {

        } else if (id == R.id.nav_career_test) {
            Intent intent = new Intent(this, CareerTest.class);
            startActivity(intent);
        } else if (id == R.id.nav_job_list) {
            Intent intent = new Intent(this, JobList.class);
            startActivity(intent);
        }else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about_us) {

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

/*by wenz11*/
    // After implementing GoogleApiClient.OnConnectionFailedListener requires this method by wenz11
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(TAG, "onConnectionFailed:" + connectionResult);
        Toast.makeText(this, "Google Play Services error.", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in.
        // TODO: Add code to check if user is signed in.
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
/*by wenz11*/
}
