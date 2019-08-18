package com.example.help4u;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class JobList extends AppCompatActivity  {

    private ListView jobListView;
    private Spinner job_category_list;
    private CustomAdapter customAdapter;



    String[] jobName;

    String[] jobDesc = {"Ambition Group Malaysia Sdn Bhd", "Vitality Boost Sdn Bhd",
                        "MULTI EPSILON SOLUTIONS SDN BHD", "SEEK ASIA",
                        "RGF Executive Search Malaysia Sdn Bhd", "AsiaPay (M) Sdn Bhd",
                        "Astro", "Captii Ventures Pte. Ltd"};


    String[] jobFullDesc = {
            "Ensure accurate and timely preparation of management and financial reports in compliance with approved accounting standard.",
            "Ensure accurate and timely submission of regulatory requirements including SST, Withholding Tax and Income Tax.",
            "You be responsible in designing, build and maintain efficient, reusable and reliable code.\n " +
                    "Have knowledge in C#, ASP.net, MS SQL, HTML, JS skills would be great.",
            " You will maintain a strong collaborative relationship with the Product Manager who shares the responsibility of balancing platform health and product growth.",
            "the Data Scientist will be responsible for all analysis across multiple markets in building predicative models and subsequently putting models into production. Working hand in hand with the cybersecurity team, the Data Scientist will work on enhancing the business",
            "Knowledge in Java, HTML5, Javascript, CSS, Node.js, jQuery and Bootstrap\n" +
                    "Knowledge in mobile application development such as iOS, Android and React Native is a plus",
            "We are looking for a Data Scientist who will support our products, ecommerce, customer, advertising, media contents and other divisions in Astro and assist leadership with the insights gained from analyzing company wide and related data.",
            "Lead the tech team through process of designing and solutioning. With the aim of creating great products that will delight users and not the easiest to execute"
    };

    String[] jobSalary = {"RM3000-3500", "RM3000",
                        "RM3000-3500", "RM3500-7000",
                        "RM10000-15000", "RM4000",
                        "Above expected salary", "RM10000-15000"};

    String[] compAddress = {"Suite 20-03, Level 20, The Intermark 348, Jalan Tun Razak, 50400 KL",
                            "Jalan Desa, Taman Desa, 58100 Kuala Lumpur, Federal Territory of Kuala Lumpur, Malaysia",
                            "48 Jalan 11/146, Bandar Tasik Selatan, Sg. Besi 57000 Kuala Lumpur",
                            "20, Menara AIA Cap Square No. 10, Jalan Munshi Abdullah 50100 Kuala Lumpur",
                            "The Gardens South Tower, Mid Valley City, Lingkaran Syed Putra, 59200 Kuala Lumpur, Federal Territory of Kuala Lumpur, Malaysia",
                            "19A-17-3, Level 17, UOA Centre, No.21 Jalan Pinang, 50450 Kuala Lumpur",
                            "57000 Lebuhraya Bukit Jalil Technology Park Malaysia Kuala Lumpur Federal Territory of Kuala Lumpur Malaysia",
                            "Suite 20-03, Level 20, The Intermark 348, Jalan Tun Razak, 50400 KL"};

    String[] compEmail = {"karyen.wong@ambition.com.my", " enquiries@oaic.gov.au", " info@multisoft.com.my",
                        "smacartney@seek.com.au", "nazmina.khan@rgf-executive.com", "sales@asiapay.com,"
                        , "th_outreach@astro.com.my", "careers@captii.vc"};


    int[] compLogo = {R.drawable.ambition_logo, R.drawable.boost_logo, R.drawable.multi_logo,
                        R.drawable.seek_asia, R.drawable.rgf_logo, R.drawable.asiapay_logo,
                        R.drawable.astro_logo, R.drawable.captii_logo};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_job_list );

        jobName = getResources().getStringArray( R.array.job_list_data );
        jobListView = findViewById( R.id.job_list_view );
        job_category_list = findViewById( R.id.job_category_list );

        //setup available options for job category spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource( this, R.array.Joblist, android.R.layout.simple_spinner_item );
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item );
        job_category_list.setAdapter( adapter );

        //setup selected item changed listener for job category spinner
        job_category_list.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //set a new adapter for listView
                customAdapter = new CustomAdapter( job_category_list.getSelectedItem().toString() );
                jobListView.setAdapter( customAdapter );
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //do nothing
            }
        } );

        //setup list view adapter with job category spinner selected value, initially will be "Default"
        customAdapter = new CustomAdapter(job_category_list.getSelectedItem().toString());
        jobListView.setAdapter( customAdapter );

        //transfer info of selected job in the list view to job detail activity
        jobListView.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent( getApplicationContext(), JobDetails.class );

                int dataIndex = customAdapter.getJobListDataIndex().get( position );

                intent.putExtra( "jobName", jobName[dataIndex] );
                intent.putExtra( "jobDesc", jobDesc[dataIndex] );
                intent.putExtra( "jobFullDesc", jobFullDesc[dataIndex] );
                intent.putExtra( "comp_Logo", compLogo[dataIndex] );
                intent.putExtra( "salary", jobSalary[dataIndex] );
                intent.putExtra( "comp_Address", compAddress[dataIndex] );
                intent.putExtra( "compEmail", compEmail[dataIndex] );

                // get position
                intent.putExtra("position", String.valueOf(dataIndex));
                startActivity( intent );
            }
        } );
    }

    //a custom adapter class for setting the content of job_list_view element
    private class CustomAdapter extends BaseAdapter {
        //list of corresponding data index
        //this will serve as a lookup table to map list view selected item to the actual data array
        private ArrayList<Integer> jobListDataIndex;

        //parameter (jobNameSelected) allow the list view to display jobs with specific job name only
        private CustomAdapter(String jobNameSelected){
            jobListDataIndex = new ArrayList<>();

            //if job category spinner is not "Default"
            if(!jobNameSelected.equals( getResources().getStringArray( R.array.Joblist )[0] )){

                //search through job name array, if equals to spinner selected value, add into index list
                for(int i = 0; i < jobName.length; i++){
                    if(jobName[i].equals( jobNameSelected )){
                        jobListDataIndex.add( i ); //add the equivalence index number in data array into the list
                    }
                }
            }
            //else job category spinner is "Default"
            else{
                //add all element into display list
                for (int i = 0; i < jobName.length; i++) {
                    jobListDataIndex.add( i );
                }
            }
        }

        ArrayList<Integer> getJobListDataIndex() {
            return jobListDataIndex;
        }

        @Override
        public int getCount() {
            return jobListDataIndex.size();
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
            View view1 = getLayoutInflater().inflate( R.layout.job_row_data, null );

            //find view element
            TextView name = view1.findViewById( R.id.companyName );
            TextView name2 = view1.findViewById( R.id.jobSmallDescription );
            ImageView image = view1.findViewById( R.id.compLogo );

            int dataIndex = jobListDataIndex.get( position );
            //set text for current list view element
            name.setText( jobName[dataIndex] );
            name2.setText( jobDesc[dataIndex] );
            image.setImageResource( compLogo[dataIndex] );

            return view1;
        }
    }
}

