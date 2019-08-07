package com.example.help4u;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;


/**
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
}
