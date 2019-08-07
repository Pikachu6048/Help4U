package com.example.help4u;

import android.content.Context;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/*
* Created by YeuHarn
* An adapter class that set the questions displayed in fragment_question > listView_questions
* */
public class TestQuestionAdapter extends BaseAdapter {

    private LayoutInflater mInflater;
    private String[] questions;
    private int pageNumber;

    public TestQuestionAdapter(Context c, String[] q, int pageNum){
        questions = q;
        pageNumber = pageNum;
        mInflater = (LayoutInflater)c.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
    }

    @Override
    public int getCount() {
        return questions.length;
    }

    @Override
    public Object getItem(int i) {
        return questions[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        final View v = mInflater.inflate( R.layout.layout_test_question, null );
        TextView question_num = (TextView)v.findViewById( R.id.textView_question_num );
        TextView question = (TextView)v.findViewById( R.id.textView_question );
        RadioGroup rg = (RadioGroup)v.findViewById( R.id.radioGroup_answer );

        RadioButton ans1 = v.findViewById( R.id.radioButton_answer1 );
        RadioButton ans2 = v.findViewById( R.id.radioButton_answer2 );
        RadioButton ans3 = v.findViewById( R.id.radioButton_answer3 );
        RadioButton ans4 = v.findViewById( R.id.radioButton_answer4 );

        final int questionIndex = pageNumber * 5 + i; //index of the test questions, 5 questions per page, start from 0

        //to remain the selected answer in the radio group after user scroll over the question
        switch(CareerQuestionnaire.selectedAnswer.get(questionIndex)){
            case R.id.radioButton_answer1:
                ans1.setChecked(true);
                break;
            case R.id.radioButton_answer2:
                ans2.setChecked(true);
                break;
            case R.id.radioButton_answer3:
                ans3.setChecked(true);
                break;
            case R.id.radioButton_answer4:
                ans4.setChecked(true);
        }

        //add a listener to record user's answer
        rg.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selectedIndex) {
                RadioButton rb = v.findViewById( selectedIndex );

                CareerQuestionnaire.selectedAnswer.set(questionIndex, selectedIndex);

                Log.d("OnCheckedListener", "selected == " + rb.getText());
            }
        } );

        //set test question numbering label
        question_num.setText( R.string.question_label );
        question_num.append( " " + (questionIndex + 1) );

        //set test question text
        question.setText( questions[i] );

        return v;
    }
}
