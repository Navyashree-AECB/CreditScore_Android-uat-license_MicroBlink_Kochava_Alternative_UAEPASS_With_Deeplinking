package com.aecb.ui.history;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aecb.ui.reporthistory.view.ReportHistoryFragment;
import com.aecb.ui.scorehistory.view.ScoreHistoryFragment;

public class HistoryPagerAdapter extends FragmentStatePagerAdapter {

    int totalTabs;

    public HistoryPagerAdapter(FragmentManager fm, int totalTabs) {
        super(fm);
        this.totalTabs = totalTabs;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new ReportHistoryFragment();
            case 1:
                return new ScoreHistoryFragment();
            default:
                return null;
        }
    }

    // this counts total number of tabs
    @Override
    public int getCount() {
        return totalTabs;
    }
}