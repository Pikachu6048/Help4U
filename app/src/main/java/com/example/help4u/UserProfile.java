package com.example.help4u;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserProfile extends AppCompatActivity implements View.OnClickListener {

    // Handle storing and retrieving of key value pairs
    private DatabaseReference mDatabaseReference;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;

    // Store User profile
    private String mUsername;
    private String mName;
    private String mPhoneNumber;
    private String mphotoUrl;
    private String mEmail;
    private String mQuote;

    // Declare all variables in Edit Profile
    // 2 Buttons
    private MaterialButton meditProfile_EditButton;
    private MaterialButton meditProfile_SaveButton;

    // 4 Text View exclue email
    private TextView meditProfile_SetName;
    private TextView meditProfile_SetPhoneNumber;
    private TextView meditProfile_SetQuote;
    private TextView meditProfile_SetEmail;

    // 2 TextInputLayout
    private TextInputLayout mtextInputLayout_EditName;
    private TextInputLayout mtextInputLayout_EditPhoneNumber;

    // 2 TextInputEditText
    private TextInputEditText meditProfile_name;
    private TextInputEditText meditProfile_phoneNumber;

    // 1 Edit Quote
    private EditText meditProfile_quote;

    // Storage Reference
    private StorageReference storageReference;
    private StorageTask uploadTask;

    // Get Image Request
    private static  final int IMAGE_REQUEST = 100;
    private static final int PICK_IMAGE_REQUEST = 200;

    // 1 Image Variable
    private Uri mImageUri;

    // Database Reference
    private DatabaseReference reference;

    // Image Click Variable
    private CircleImageView mUserProfilePic;

    // User Favorite Quote
    private String Quote;
    private String Name;
    private String PhoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        requestAppPermissions();

        // Assign 2 Buttons
        meditProfile_EditButton = findViewById(R.id.editProfile_EditButton);
        meditProfile_EditButton.setOnClickListener(this);
        meditProfile_SaveButton = findViewById(R.id.editProfile_SaveButton);
        meditProfile_SaveButton.setOnClickListener(this);

        // Circular Image View Onclick Listener
        mUserProfilePic = findViewById(R.id.userImage);
        mUserProfilePic.setOnClickListener(this);
        mUserProfilePic.setEnabled(false);

        // Assign 4 Text View
        meditProfile_SetName = findViewById(R.id.editProfile_SetName);
        meditProfile_SetPhoneNumber = findViewById(R.id.editProfile_SetPhoneNumber);
        meditProfile_SetQuote = findViewById(R.id.editProfile_SetQuote);
        meditProfile_SetEmail = findViewById(R.id.editProfile_SetEmail);

        // Assign 2 TextInputLayout
        mtextInputLayout_EditName = findViewById(R.id.textInputLayout_EditName);
        mtextInputLayout_EditPhoneNumber = findViewById(R.id.textInputLayout_EditPhoneNumber);

        // Assign 2 TextInputEditText
        meditProfile_name = findViewById(R.id.editProfile_name);
        meditProfile_phoneNumber = findViewById(R.id.editProfile_phoneNumber);

        // Assign 1 Edit Text
        meditProfile_quote = findViewById(R.id.editProfile_quote);

        // Assign Circular Image
        mUserProfilePic = findViewById(R.id.userImage);

        // Get current user
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference("userprofile");

        // Storage Reference
        storageReference = FirebaseStorage.getInstance().getReference("uploads");

        // Database Reference
        reference = FirebaseDatabase.getInstance().getReference("userprofile").child(mFirebaseUser.getUid());

        // Get user email based on the Authentication Email
        mUsername = mFirebaseUser.getEmail();

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserProfileToDatabase user = dataSnapshot.getValue(UserProfileToDatabase.class);
                // Assign the user profile
                meditProfile_SetName.setText(user.getName());
                Name = user.getName();
                meditProfile_SetPhoneNumber.setText(user.getPhonenumber());
                PhoneNumber = user.getPhonenumber();
                meditProfile_SetQuote.setText(user.getQuote());
                Quote = user.getQuote();
                mphotoUrl = user.getPhotoUrl();
                meditProfile_SetEmail.setText(user.getEmail());

                if (user.getPhotoUrl().equals("default")) {
                    mUserProfilePic.setImageResource(R.drawable.pikademo);
                } else {
                    // Load Image from the database
                    Glide.with(getApplicationContext()).load(user.getPhotoUrl()).into(mUserProfilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error
            }
        });
    }

    public void EditUserProfile() {
        // Make edit button gone and show save button
        meditProfile_EditButton.setVisibility(View.GONE);
        meditProfile_SaveButton.setVisibility(View.VISIBLE);

        mUserProfilePic.setEnabled(true);

        meditProfile_SetName.setVisibility(View.GONE);
        meditProfile_SetPhoneNumber.setVisibility(View.GONE);
        meditProfile_SetQuote.setVisibility(View.GONE);

        mtextInputLayout_EditName.setVisibility(View.VISIBLE);
        mtextInputLayout_EditPhoneNumber.setVisibility(View.VISIBLE);
        meditProfile_quote.setVisibility(View.VISIBLE);
        if(Quote.equals("Quote")) {
            meditProfile_quote.setText("");
        } else {
            meditProfile_quote.setText(Quote);
        }
        if (Name.equals("Name")) {
            meditProfile_name.setText("");
        } else {
            meditProfile_name.setText(Name);
        }
        if (PhoneNumber.equals("Phone Number")) {
            meditProfile_phoneNumber.setText("");
        } else {
            meditProfile_phoneNumber.setText(PhoneNumber);
        }
    }

    public void ShowUserProfile() {
        // Do validation
        if (!validateUserProfileInput()) {
            return;
        }

        // Save the Changes to Text View
        meditProfile_SetName.setText(meditProfile_name.getText().toString());
        meditProfile_SetPhoneNumber.setText(meditProfile_phoneNumber.getText().toString());
        meditProfile_SetQuote.setText(meditProfile_quote.getText().toString());

        // Save as Local Variable
        mName = meditProfile_SetName.getText().toString().trim();
        mPhoneNumber = meditProfile_SetPhoneNumber.getText().toString().trim();
        if (mphotoUrl == null) {
            mphotoUrl = "default";
        }
        mQuote = meditProfile_SetQuote.getText().toString().trim();

        // Save to Database
        SaveToDatabase();

        // Hide keyboard on clicking save button
        hideKeyboard();

        // Make save button gone and show edit button
        meditProfile_EditButton.setVisibility(View.VISIBLE);
        meditProfile_SaveButton.setVisibility(View.GONE);

        mUserProfilePic.setEnabled(false);

        meditProfile_SetName.setVisibility(View.VISIBLE);
        meditProfile_SetPhoneNumber.setVisibility(View.VISIBLE);
        meditProfile_SetQuote.setVisibility(View.VISIBLE);

        mtextInputLayout_EditName.setVisibility(View.GONE);
        mtextInputLayout_EditPhoneNumber.setVisibility(View.GONE);
        meditProfile_quote.setVisibility(View.GONE);
    }

    public void SaveToDatabase() {
        // Save the data to the Database
        // Pass all the parameters to store into the database
        FirebaseUser user = mFirebaseAuth.getCurrentUser();
        UserProfileToDatabase changeUserProfile = new UserProfileToDatabase(mFirebaseUser.getUid(), mUsername, mName, mPhoneNumber, mphotoUrl, mQuote);
        mDatabaseReference.child(mFirebaseUser.getUid()).setValue(changeUserProfile);
    }

    // Do validation on User Input Details Credentials
    private boolean validateUserProfileInput() {
        boolean valid = true;
        Pattern pattern = Pattern.compile("^(\\+?6?01)[0-46-9]-*[0-9]{7,8}$");

        String User_Name = meditProfile_name.getText().toString();
        if (TextUtils.isEmpty(User_Name)) {
            mtextInputLayout_EditName.setError("Required");
            valid = false;
        } else {
            mtextInputLayout_EditName.setError(null);
        }

        String Phone_Number = meditProfile_phoneNumber.getText().toString();
        if (TextUtils.isEmpty(Phone_Number)) {
            mtextInputLayout_EditPhoneNumber.setError("Required");
            valid = false;
        } else if(!pattern.matcher(Phone_Number).matches()) {
            mtextInputLayout_EditPhoneNumber.setError("Invalid Phone Number Format +60XXXXXXXXX");
            valid = false;
        } else {
            mtextInputLayout_EditPhoneNumber.setError(null);
        }

        return valid;
    }

    // Hide Keyboard
    private void hideKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void openImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getApplicationContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private  void uploadImage() {
        Toast.makeText(this, "At start Upload Image", Toast.LENGTH_SHORT).show();
        final ProgressDialog pd = new ProgressDialog(UserProfile.this);
        pd.setMessage("Uploading");
        pd.show();

        Thread mThread = new Thread() {
            @Override
            public void run() {
                if (mImageUri != null) {
                    final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                            +"."+getFileExtension(mImageUri));

                    uploadTask = fileReference.putFile(mImageUri);
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

                                reference = FirebaseDatabase.getInstance().getReference("userprofile").child(mFirebaseUser.getUid());
                                HashMap<String, Object> map = new HashMap<>();
                                map.put("photoUrl", mUri);
                                reference.updateChildren(map);

                                pd.dismiss();
                            } else {
                                Toast.makeText(UserProfile.this, "Could Not Update Image", Toast.LENGTH_SHORT).show();
                                pd.dismiss();
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserProfile.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            pd.dismiss();
                        }
                    });
                } else {
                    Toast.makeText(UserProfile.this, "No image selected", Toast.LENGTH_SHORT).show();
                }
            }
        };
        mThread.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "At start activity result", Toast.LENGTH_SHORT).show();

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
        && data != null && data.getData() != null) {
            mImageUri = data.getData();

            if (uploadTask != null && uploadTask.isInProgress()) {
                Toast.makeText(this, "Upload in progress", Toast.LENGTH_SHORT).show();
            } else {
                uploadImage();
            }
        }
    }

    private void requestAppPermissions() {
        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            return;
        }

        if (hasReadPermissions() && hasWritePermissions()) {
            return;
        }

        ActivityCompat.requestPermissions(this,
                new String[] {
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                }, IMAGE_REQUEST); // your request code
    }

    private boolean hasReadPermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    private boolean hasWritePermissions() {
        return (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.editProfile_EditButton:
                // Perform Edit Operation
                EditUserProfile();
                break;
            case R.id.editProfile_SaveButton:
                // Perform Save and Show Operation
                ShowUserProfile();
                break;
            case R.id.userImage:
                openImage();
                Toast.makeText(this, "Image is Clicked", Toast.LENGTH_SHORT).show();
                break;
        }
    }
}
