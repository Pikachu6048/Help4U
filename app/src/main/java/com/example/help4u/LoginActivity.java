package com.example.help4u;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;

    // Declare Edit Text Input
    private TextInputEditText mUsername_login_input;
    private TextInputEditText mPassword_login_input;

    // Declare Text Input Layout to Display Error
    private TextInputLayout mtextInputLayout_Username;
    private TextInputLayout mtextInputLayout2_Password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Assign Edit Text Input Fields
        mUsername_login_input = findViewById(R.id.username_login_input);
        mPassword_login_input = findViewById(R.id.password_login_input);

        // Declare Buttons Click Listener
        findViewById(R.id.button_login).setOnClickListener(this);
        findViewById(R.id.button_sign_up).setOnClickListener(this);
        findViewById(R.id.button_cancel).setOnClickListener(this);

        // Assign Text Input Layout to XML Layout
        mtextInputLayout_Username = findViewById(R.id.textInputLayout_Username);
        mtextInputLayout2_Password = findViewById(R.id.textInputLayout2_Password);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_login:
                // Check username + password Input
                String email = mUsername_login_input.getText().toString().trim();
                String password = mPassword_login_input.getText().toString().trim();

                // Validation on username + password Input
                if (!validateForm()) {
                    return;
                }

                // Perform Login Intent
                mFirebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success
                                    Toast.makeText(LoginActivity.this, "Login Successfully!",
                                            Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                                    finish();
                                } else {
                                    // If sign in fails, display error message to the user
                                    Toast.makeText(LoginActivity.this, "Invalid Login Credentials!",
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                break;
            case R.id.button_sign_up:
                // Launch Sign Up Intent
                startActivity(new Intent(this, Register.class));
                finish();
                break;
            case R.id.button_cancel:
                // Perform Cancel Intent
                // means Exit the App
                // Confirm to quit App
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Quit " + this.getResources().getString(R.string.app_name));
                builder.setMessage("Confirm Quit " + this.getResources().getString(R.string.app_name) + "?")
                        .setCancelable(false)
                        .setPositiveButton( R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Intent intent = new Intent(Intent.ACTION_MAIN);
                                intent.addCategory(Intent.CATEGORY_HOME);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alertQuit = builder.create();
                alertQuit.show();
                break;
        }
    }

    // Do validation on Login Credentials
    private boolean validateForm() {
        boolean valid = true;
        Pattern pattern = Patterns.EMAIL_ADDRESS;

        String email = mUsername_login_input.getText().toString();
        if (TextUtils.isEmpty(email)) {
            mtextInputLayout_Username.setError("Required");
            valid = false;
        } else if(!pattern.matcher(email).matches()) {
            mtextInputLayout_Username.setError("Invalid Email Address Format");
            valid = false;
        }
        else {
            mtextInputLayout_Username.setError(null);
        }

        String password = mPassword_login_input.getText().toString();
        if (TextUtils.isEmpty(password)) {
            mtextInputLayout2_Password.setError("Required");
            valid = false;
        } else if (mPassword_login_input.length() < 6) {
            mtextInputLayout2_Password.setError("Password must be more than 6 characters");
            valid = false;
        }
        else {
            mtextInputLayout2_Password.setError(null);
        }

        return valid;
    }
}
