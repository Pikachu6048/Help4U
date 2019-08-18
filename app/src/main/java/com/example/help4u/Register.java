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
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements View.OnClickListener {

    // TAG for Debugging
    private final String TAG = "Register Activity";

    // Declare an instance of Firebase Auth
    private FirebaseAuth mFirebaseAuth;

    // Declare TextInputLayout Field
    private TextInputLayout mlabel_username;
    private TextInputLayout mlabel_password;
    private TextInputLayout mlabel_confirm_password;

    // Declare TextInputEditText Field
    private TextInputEditText mtext_input_username;
    private TextInputEditText mtext_input_password;
    private TextInputEditText mtext_input_confirm_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register );

        // Assign TextInputLayout Fields
        mlabel_username = findViewById(R.id.label_username);
        mlabel_password = findViewById(R.id.label_password);
        mlabel_confirm_password = findViewById(R.id.label_confirm_password);

        // Assign TextInputEditText Fields
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
                            FirebaseUser user = mFirebaseAuth.getCurrentUser();
                            String userid = user.getUid();

                            DatabaseReference reference;
                            reference = FirebaseDatabase.getInstance().getReference("userprofile").child(userid);

                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id", userid);
                            hashMap.put("email", mtext_input_username.getText().toString());
                            hashMap.put("name", "Name");
                            hashMap.put("phonenumber", "Phone Number");
                            hashMap.put("photoUrl", "default");
                            hashMap.put("quote", "Quote");
                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    // Successfully Sign Up
                                    if(task.isSuccessful()) {
                                        Toast.makeText(Register.this, getResources().getString( R.string.account_create_successful),
                                                Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Register.this, getResources().getString( R.string.login_successful ),
                                                Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Register.this, MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

//                            Toast.makeText(Register.this, "Account has successfully been created!",
//                                    Toast.LENGTH_SHORT).show();
//                            SignInAfterSignUp();
                        } else if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                            Toast.makeText(Register.this,
                                    getResources().getString( R.string.email_already_registered), Toast.LENGTH_SHORT).show();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Register.this, getResources().getString( R.string.account_cannot_create),
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
                            Toast.makeText(Register.this, getResources().getString( R.string.login_successful ),
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
            mlabel_username.setError(getResources().getString( R.string.required ));
            valid = false;
        } else if(!pattern.matcher(email).matches()) {
            mlabel_username.setError(getResources().getString( R.string.invalid_email_format ));
            valid = false;
        }
        else {
            mlabel_username.setError(null);
        }

        String password = mtext_input_password.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mlabel_password.setError(getResources().getString( R.string.required ));
            valid = false;
        } else if (mtext_input_password.length() < 6) {
            mlabel_password.setError(getResources().getString( R.string.password_format_error_message ));
            valid = false;
        }
        else {
            mlabel_password.setError(null);
        }

        String confirmPassword = mtext_input_confirm_password.getText().toString();
        if (TextUtils.isEmpty(confirmPassword)) {
            mlabel_confirm_password.setError(getResources().getString( R.string.required ));
            valid = false;
        } else if (!confirmPassword.equals(password)) {
            mlabel_confirm_password.setError(getResources().getString( R.string.password_not_match));
            valid = false;
        } else if (mtext_input_confirm_password.length() < 6) {
            mlabel_confirm_password.setError( getResources().getString(R.string.password_format_error_message));
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
