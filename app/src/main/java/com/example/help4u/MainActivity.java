package com.example.help4u;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.bumptech.glide.Glide;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.view.Change;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.widget.Button;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import de.hdodenhof.circleimageview.CircleImageView;

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
    private Button mLogout;

    // Test btn to other activity by wenz11
    private Button mActivityTest;

    // Database Reference
    private DatabaseReference reference;

    // Profile Picture in Navigation Header
    private CircleImageView muserImageNavHeader;
    private TextView mnameNavHeader;
    private TextView muserEmail;

    // Database Listener
    private ValueEventListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        /*splash screen by wenz11*/
        /*Change this activity's manifest to AppTheme.Splash
        Temporary disable it for designing purposes   */
//        setTheme(R.style.AppTheme_NoActionBar);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Declare Navigation Header Image
        NavigationView navigationView2 = findViewById(R.id.nav_view);
        View hView2 = navigationView2.getHeaderView(0);
        muserImageNavHeader = hView2.findViewById(R.id.userImageNavHeader);
        mnameNavHeader = hView2.findViewById(R.id.nameNavHeader);
        muserEmail = hView2.findViewById(R.id.userEmail);

        /*Sign in operation by wenz11*/
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        // Initialize Firebase Auth
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, Launch the Login activity
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        } else {
            mUsername = mFirebaseUser.getEmail();
            // Database Reference
            reference = FirebaseDatabase.getInstance().getReference("userprofile").child(mFirebaseUser.getUid());


            // Assign the variables to the Navigation Header
            mListener = reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserProfileToDatabase user = dataSnapshot.getValue(UserProfileToDatabase.class);
                    // Assign the user profile
                    mnameNavHeader.setText(user.getName());
                    muserEmail.setText(user.getEmail());

                    if (user.getPhotoUrl().equals("default")) {
                        muserImageNavHeader.setImageResource(R.drawable.pikademo);
                    } else {
                        // Load Image from the database
                        Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(muserImageNavHeader);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error
                }
            });
        }
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

        // Get Log out button on Nav Header Drawer by wenz11
        View hView = navigationView.getHeaderView(0);
        Button mBtnLogout = hView.findViewById(R.id.btnLogout);
        mBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Add sign out operation
                signOut();
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        });

        muserImageNavHeader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UserProfile.class);
                startActivity(intent);
            }
        });

        // Set Text View to the user's email
        TextView mUserEmail = hView.findViewById(R.id.userEmail);
        mUserEmail.setText(mUsername);

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

        // Launch Log Out Activity on Main Screen
        mLogout = findViewById(R.id.log_out);
        mLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LaunchLogOut();
            }
        });
    }

    // Test Title Screen Activity by wenz11
    private void LaunchActivityTest() {
        Intent intent = new Intent(this, CareerTest.class);
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

    public void LaunchLogOut() {
        // Add sign out operation
        signOut();
        startActivity(new Intent(this, MainActivity.class));
        finish();
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
                // Add sign out operation
                signOut();
                startActivity(new Intent(this, MainActivity.class));
                finish();
                return true;
            case R.id.action_settings:
                // Add startActivity intent if click on setting
                startActivity( new Intent( this, Setting.class ) );
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
            Intent intent = new Intent(this, UserProfile.class);
            startActivity(intent);
        } else if (id == R.id.nav_wish_list) {
            Intent intent = new Intent(this, JobWishlist.class);
            startActivity(intent);
        } else if (id == R.id.nav_career_test) {
            Intent intent = new Intent(this, CareerTest.class);
            startActivity(intent);
        } else if (id == R.id.nav_job_list) {
            Intent intent = new Intent(this, JobList.class);
            startActivity(intent);
        }else if (id == R.id.nav_setting) {
            Intent intent = new Intent( this, Setting.class );
            startActivity( intent );

        } else if (id == R.id.nav_about_us) {

            Intent intent = new Intent(this, AboutUs.class);
            startActivity(intent);
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

    // Sign Out Function
    private void signOut() {
        mFirebaseAuth.signOut();
        Toast.makeText(this, "Successfully Logout!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in
        // Can happen during onStart and onResume
        FirebaseUser currentUser = mFirebaseAuth.getCurrentUser();
        if (currentUser != null) {
            // Get User Email
            mUsername = currentUser.getEmail();
        }
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
        if (mFirebaseUser != null) {
            reference.removeEventListener(mListener);
        }
    }
/*by wenz11*/
}
