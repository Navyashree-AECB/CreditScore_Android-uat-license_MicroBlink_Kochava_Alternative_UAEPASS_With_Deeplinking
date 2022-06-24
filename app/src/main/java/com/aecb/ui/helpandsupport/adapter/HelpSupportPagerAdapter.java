package com.aecb.ui.helpandsupport.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.aecb.ui.creditreport.faq.view.FAQFragment;
import com.aecb.ui.helpandsupport.contactus.view.ContactUsFragment;
import com.aecb.ui.helpandsupport.datacorrection.view.DataCorrectionFragment;

public class HelpSupportPagerAdapter extends FragmentStatePagerAdapter {
    int tabCount;

    public HelpSupportPagerAdapter(FragmentManager fragmentManager, int tabCount) {
        super(fragmentManager);
        this.tabCount = tabCount;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            //TODO Remove faq for sometime
           /* case 0:
                return new FAQFragment();*/
            case 0:
                return new ContactUsFragment();
           /* case 1:
                return new DataCorrectionFragment();*/
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return tabCount;
    }
}