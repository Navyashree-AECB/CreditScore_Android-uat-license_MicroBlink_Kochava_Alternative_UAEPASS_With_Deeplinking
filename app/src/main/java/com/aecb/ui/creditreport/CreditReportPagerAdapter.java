package com.aecb.ui.creditreport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.aecb.ui.creditreport.faq.view.FAQFragment;
import com.aecb.ui.creditreport.glossary.view.GlossaryFragment;
import com.aecb.ui.creditreport.intro.view.IntroFragment;

import static com.aecb.AppConstants.IntentKey.CREDIT_REPORT;

public class CreditReportPagerAdapter extends FragmentPagerAdapter {

    private int totalTabs;
    private Bundle bundle;

    public CreditReportPagerAdapter(FragmentManager fm, int totalTabs, Bundle bundle) {
        super(fm);
        this.totalTabs = totalTabs;
        this.bundle = bundle;
    }

    // this is for fragment tabs
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return IntroFragment.newInstance(bundle);
            //TODO Remove faq and glossary for sometime
           /* case 1:
                bundle.putBoolean(CREDIT_REPORT, true);
                return FAQFragment.newInstance(bundle);
            case 2:
                return GlossaryFragment.newInstance(bundle);*/
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