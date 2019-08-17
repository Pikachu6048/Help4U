package com.example.help4u;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Setting extends AppCompatActivity {

    private Activity thisActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_setting );

        thisActivity = this;

        //clear test result in SharedPreferences
        Button clearTestResult = findViewById( R.id.button_clearResult );
        clearTestResult.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder( thisActivity )
                        .setTitle( R.string.clear_test_result )
                        .setMessage( R.string.clear_test_result_message )
                        .setPositiveButton( R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                getSharedPreferences( CareerTestResult.SHARED_PREFS, Context.MODE_PRIVATE )
                                        .edit()
                                        .clear()
                                        .apply();

                                Toast.makeText(thisActivity, "Test Result cleared", Toast.LENGTH_SHORT).show();
                            }
                        } )
                        .setNegativeButton( R.string.no, null )
                        .setIcon( R.drawable.ic_delete_black_24dp )
                        .show();
            }
        } );
    }
}
