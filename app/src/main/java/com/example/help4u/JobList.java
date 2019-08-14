package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class JobList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ListView jobListView;
    private TextView jobCategory;
    private Spinner  job_category_list;

    String[] compName = {"Company 1","Company 2","Company 3","Company 4","Company 5"};
    String[] compDesc = {"Company 1 desc","Company 2 desc","Company 3 desc","Company 4 desc","Company 5 desc"};
    String[] compFullDesc ={"Company 1 full desc","Company 2 full desc","Company 3 full desc",
            "Company 4 full desc","Company 5 full desc"};
    String[] compLat = { "3.158424","3.158424","3.158424","3.158424","3.158424"};
    String[] compLong = {"101.752893","101.752893","101.752893","101.752893","101.752893"};



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        jobCategory = (TextView) findViewById(R.id.job_category);
        jobListView = (ListView) findViewById(R.id.job_list_view);


        CustomAdapter customAdapter = new CustomAdapter();
        jobListView.setAdapter(customAdapter);

        Spinner jobCategoryList = findViewById(R.id.job_category_list);
        ArrayAdapter<CharSequence> adapter =ArrayAdapter.createFromResource(this,R.array.Joblist,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        jobCategoryList.setAdapter(adapter);
        jobCategoryList.setOnItemSelectedListener(this);

        jobListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getApplicationContext(),JobDetails.class);
                intent.putExtra("compName",compName[position]);
                intent.putExtra("compDesc",compDesc[position]);
                intent.putExtra("compFullDesc",compFullDesc[position]);
                intent.putExtra("compLat",compLat[position]);
                intent.putExtra("compLong",compLong[position]);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String text = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    private class CustomAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            return compName.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view1 = getLayoutInflater().inflate(R.layout.job_row_data,null);


            TextView name = view1.findViewById(R.id.companyName);
            TextView name2 = view1.findViewById(R.id.jobSmallDescription);

            name.setText(compName[position]);
            name2.setText(compDesc[position]);

            return view1;
        }
    }

}
