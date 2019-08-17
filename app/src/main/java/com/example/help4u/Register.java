package com.example.help4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    // TAG for Debugging
    private final String TAG = "Register Activity";

    // Declare an instance of Firebase Auth
    private FirebaseAuth mFirebaseAuth;

    // Declare Edit Text Field
    private EditText mtext_input_username;
    private EditText mtext_input_password;
    private EditText mtext_input_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        // Assign Edit Text Fields
        mtext_input_username = findViewById(R.id.text_input_username);
        mtext_input_password = findViewById(R.id.text_input_password);
        mtext_input_confirm_password = findViewById(R.id.text_input_confirm_password);

        // Button Click Listener
        findViewById(R.id.button_sign_up).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);
        findViewById(R.id.button_sign_in).setOnClickListener(this);

        // Start Auth Instance
        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void createAccount(String email, String password) {
        if (!validateSignUpCredentials()) {
            return;
        }

        // START create user with email
        mFirebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success
//                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            Toast.makeText(Register.this, "Account has successfully been created!",
                                    Toast.LENGTH_SHORT).show();
                            SignInAfterSignUp();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Register.this,
                                    "This email has already been registered with Help4U!", Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, "Account cannot be created!",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void SignInAfterSignUp() {
        // If all the Login Credentials are correct then
        // start Main Activity
        // Perform Login Intent
        String user_Correct_Email = mtext_input_username.getText().toString().trim();
        String user_Correct_Password = mtext_input_confirm_password.getText().toString().trim();
        mFirebaseAuth.signInWithEmailAndPassword(user_Correct_Email, user_Correct_Password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign Up success
                            Toast.makeText(Register.this, "Login Successfully!",
                                    Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        }
                    }
                });
    }

    private boolean validateSignUpCredentials() {
        boolean valid = true;
        Pattern pattern = Patterns.EMAIL_ADDRESS;

        String email = mtext_input_username.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mtext_input_username.setError("Required");
            valid = false;
        } else if(!pattern.matcher(email).matches()) {
            mtext_input_username.setError("Invalid Email Address Format");
            valid = false;
        }
        else {
            mtext_input_username.setError(null);
        }

        String password = mtext_input_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mtext_input_password.setError("Required");
            valid = false;
        } else if (mtext_input_password.length() < 6) {
            mtext_input_password.setError("Password must be more than 6 characters");
            valid = false;
        }
        else {
            mtext_input_password.setError(null);
        }

        String confirmPassword = mtext_input_confirm_password.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            mtext_input_confirm_password.setError("Required");
            valid = false;
        } else if (!confirmPassword.equals(password)) {
            mtext_input_confirm_password.setError("Password does not match");
            valid = false;
        } else if (mtext_input_confirm_password.length() < 6) {
            mtext_input_confirm_password.setError("Password must be more than 6 characters");
            valid = false;
        }

        return valid;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_sign_up:
                createAccount(mtext_input_username.getText().toString(), mtext_input_confirm_password.getText().toString());
                break;
            case R.id.button_sign_in:
                // Sign In
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            case R.id.button_cancel:
                // Cancel button goes back to previous activity
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }
}
