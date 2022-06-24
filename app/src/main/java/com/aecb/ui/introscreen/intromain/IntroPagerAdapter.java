package com.aecb.ui.introscreen.intromain;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aecb.ui.introscreen.introfour.view.IntroFourFragment;
import com.aecb.ui.introscreen.introone.view.IntroOneFragment;
import com.aecb.ui.introscreen.introthree.view.IntroThreeFragment;
import com.aecb.ui.introscreen.introtwo.view.IntroTwoFragment;

public class IntroPagerAdapter extends FragmentStatePagerAdapter {
    public IntroPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new IntroOneFragment();
            case 1:
                return new IntroTwoFragment();
            case 2:
                return new IntroThreeFragment();
            case 3:
                return new IntroFourFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;
    }
}
