package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JobList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

   private ListView jobListView;
   private TextView jobCategory;
   private Spinner  job_category_list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        jobCategory = (TextView) findViewById(R.id.job_category);
        jobListView = (ListView) findViewById(R.id.job_list_view);


        Spinner jobCategoryList = findViewById(R.id.job_category_list);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.Joblist,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobCategoryList.setAdapter(adapter);
        jobCategoryList.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();
        Toast.makeText(parent.getContext(),text,Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
