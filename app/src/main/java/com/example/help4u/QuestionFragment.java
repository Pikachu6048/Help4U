package com.example.help4u;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;


/*
 * Created by YeuHarn
 * A fragment contains ListView for displaying test questions
 */
public class QuestionFragment extends Fragment {

    private String[] questions;
    private int pageNumber;

    public QuestionFragment() {
        // Required empty public constructor
    }

    public static final QuestionFragment newInstance(String[] allQuestions, int pageNum){
        QuestionFragment questionFragment = new QuestionFragment();

        questionFragment.questions = new String[5];
        questionFragment.pageNumber = pageNum;

        //this will calculate the first question for every page
        //page1: index=0; page2: index=5; page3: index=10 ...
        int firstQuesInPage = (pageNum) * 5;

        for(int i = 0; i < 5; i++){
            questionFragment.questions[i] = allQuestions[firstQuesInPage + i];
        }

        return questionFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate( R.layout.fragment_question, container, false );

        //find list view that use to display the test questions
        ListView mListViewQuestions = (ListView) view.findViewById( R.id.listView_questions );

        TestQuestionAdapter adapter = new TestQuestionAdapter( getActivity(), questions, pageNumber );
        mListViewQuestions.setAdapter( adapter );

        // Inflate the layout for this fragment
        return view;
    }

/*
 * An adapter class that set the questions displayed in fragment_question > listView_questions
 * */
    private class TestQuestionAdapter extends BaseAdapter {

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
            TextView question_num = v.findViewById( R.id.textView_question_num );
            TextView question = v.findViewById( R.id.textView_question );
            RadioGroup rg = v.findViewById( R.id.radioGroup_answer );

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
}
