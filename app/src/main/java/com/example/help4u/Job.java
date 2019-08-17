package com.example.help4u;

/*
* Created by YeuHarn
* An abstract class that holds constants related to jobs
* */
public abstract class Job {
    public static final int TOTAL_NUM_OF_JOB_CATEGORY = 2; //total number of job category
    public static final int TOTAL_NUM_OF_JOB = 4; //total number of jobs

    public static final String IT_CATEGORY_DESC = "IT"; //IT category description
    public static final String BUSINESS_CATEGORY_DESC = "Business"; //Business category description

    public static final int PROGRAMMER = 0; //constant index number for Programmer
    public static final int DATA_SCIENTIST = 1; //constant index number for DataScientist
    public static final int ACCOUNTANT = 2; //constant index number for Accountant
    public static final int ENTREPRENEUR = 3; //constant index number for Entrepreneur

    public static final String PROGRAMMER_DESC = "Programmer"; //constant string for Programmer
    public static final String SOFTWARE_MANAGER_DESC = "Software Manager"; //constant string for Software Manager
    public static final String DATA_SCIENTIST_DESC = "Data Scientist"; //constant string for Data Scientist
    public static final String ACCOUNTANT_DESC = "Accountant"; //constant string for Accountant
    public static final String ENTREPRENEUR_DESC = "Entrepreneur"; //constant string for Entrepreneur
}
