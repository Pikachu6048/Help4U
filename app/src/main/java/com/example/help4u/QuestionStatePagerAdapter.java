package com.example.help4u;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/*
* Created by YeuHarn
* A adapter class that use to manage activity_career_questionnaire > question_container
* A class for navigating between questions fragments
* */
public class QuestionStatePagerAdapter extends FragmentStatePagerAdapter {

    private List<Fragment> mFragmentList;

    public QuestionStatePagerAdapter(FragmentManager fm) {
        super( fm );
        mFragmentList = new ArrayList<>();
    }

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get( position );
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
