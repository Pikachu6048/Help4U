package com.example.help4u;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class JobList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ListView jobListView;
    private TextView jobCategory;
    private Spinner  job_category_list;
    String job_cate = "Default";

    String[] jobName = {"Accountant","Accountant","Programmer","Software Manager","Data Scientist","Company 6"
    ,"Company 7", "Company 8"};

    String[] jobDesc = {"Ambition Group Malaysia Sdn Bhd","Vitality Boost Sdn Bhd","MULTI EPSILON SOLUTIONS SDN BHD",
            "SEEK ASIA","RGF Executive Search Malaysia Sdn Bhd",
            "Company 6 desc","Company 7 desc","Company 8 desc",};


    String[] jobFullDesc ={"Ensure accurate and timely preparation of management and financial reports in compliance with approved accounting standard.",
            "Ensure accurate and timely submission of regulatory requirements including SST, Withholding Tax and Income Tax.",
            "You be responsible in designing, build and maintain efficient, reusable and reliable code.\n " +
                    "Have knowledge in C#, ASP.net, MS SQL, HTML, JS skills would be great.",
            " You will maintain a strong collaborative relationship with the Product Manager who shares the responsibility of balancing platform health and product growth.",
            "the Data Scientist will be responsible for all analysis across multiple markets in building predicative models and subsequently putting models into production. Working hand in hand with the cybersecurity team, the Data Scientist will work on enhancing the business",
            "Company 6 full desc",
            "Company 7 full desc",
            "Company 8 full desc"};

    String [] jobSalary = {"RM3000-3500","RM3000","RM3000-3500","RM3500-7000","RM10000-15000","RM4000","RM3000-3500","RM9000"};

    String[] compAddress ={"Suite 20-03, Level 20, The Intermark 348, Jalan Tun Razak, 50400 KL",
    "Jalan Desa, Taman Desa, 58100 Kuala Lumpur, Federal Territory of Kuala Lumpur, Malaysia",
            "48 Jalan 11/146, Bandar Tasik Selatan, Sg. Besi 57000 Kuala Lumpur",
    "20, Menara AIA Cap Square No. 10, Jalan Munshi Abdullah 50100 Kuala Lumpur",
    "The Gardens South Tower, Mid Valley City, Lingkaran Syed Putra, 59200 Kuala Lumpur, Federal Territory of Kuala Lumpur, Malaysia",
    "Suite 20-03, Level 20, The Intermark 348, Jalan Tun Razak, 50400 KL",
    "Suite 20-03, Level 20, The Intermark 348, Jalan Tun Razak, 50400 KL",
    "Suite 20-03, Level 20, The Intermark 348, Jalan Tun Razak, 50400 KL",};

    String[] compEmail = {"karyen.wong@ambition.com.my"," enquiries@oaic.gov.au"," info@multisoft.com.my",
            "smacartney@seek.com.au","nazmina.khan@rgf-executive.com","karyen.wong@ambition.com.my"
    ,"karyen.wong@ambition.com.my","karyen.wong@ambition.com.my"};





     int[] compLogo ={R.drawable.ambition_logo,R.drawable.boost_logo,R.drawable.multi_logo,
        R.drawable.seek_asia,R.drawable.rgf_logo,R.drawable.ambition_logo,R.drawable.ambition_logo,R.drawable.ambition_logo};


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
                intent.putExtra("jobName",jobName[position]);
                intent.putExtra("jobDesc",jobDesc[position]);
                intent.putExtra("jobFullDesc",jobFullDesc[position]);
                intent.putExtra("comp_Logo",compLogo[position]);
                intent.putExtra("salary",jobSalary[position]);
                intent.putExtra("comp_Address",compAddress[position]);
                intent.putExtra("compEmail",compEmail[position]);
                startActivity(intent);
            }
        });

    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String job_category = parent.getItemAtPosition(position).toString();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


        private class CustomAdapter extends BaseAdapter {


            @Override
            public int getCount() {
                return jobName.length;
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
                View view1 = getLayoutInflater().inflate(R.layout.job_row_data, null);


                TextView name = view1.findViewById(R.id.companyName);
                TextView name2 = view1.findViewById(R.id.jobSmallDescription);
                ImageView image = view1.findViewById(R.id.compLogo);
                name.setText(jobName[position]);
                name2.setText(jobDesc[position]);
                image.setImageResource(compLogo[position]);

                return view1;
            }
        }
    }

