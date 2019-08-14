package com.example.help4u;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

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

        RadioButton veryInterested = v.findViewById( R.id.radioButton_answer_vi );
        RadioButton interested = v.findViewById( R.id.radioButton_answer_in );
        RadioButton slightlyInterested = v.findViewById( R.id.radioButton_answer_si );
        RadioButton notInterested = v.findViewById( R.id.radioButton_answer_ni );

        final int questionIndex = pageNumber * 5 + i; //index of the test questions, 5 questions per page, start from 0

        //to remain the selected answer in the radio group after user scroll over the question
        switch(CareerQuestionnaire.selectedAnswer.get(questionIndex)){
            case R.id.radioButton_answer_vi:
                veryInterested.setChecked(true);
                break;
            case R.id.radioButton_answer_in:
                interested.setChecked(true);
                break;
            case R.id.radioButton_answer_si:
                slightlyInterested.setChecked(true);
                break;
            case R.id.radioButton_answer_ni:
                notInterested.setChecked(true);
        }

        //add a listener to record user's answer
        rg.setOnCheckedChangeListener( new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int selectedIndex) {
                CareerQuestionnaire.selectedAnswer.set(questionIndex, selectedIndex);
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
