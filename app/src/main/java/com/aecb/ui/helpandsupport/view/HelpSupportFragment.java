package com.aecb.ui.helpandsupport.view;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.databinding.FragmentHelpSupportBinding;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.helpandsupport.adapter.HelpSupportPagerAdapter;
import com.aecb.ui.helpandsupport.presenter.HelpSupportContract;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.ui.notification.view.NotificationFragment;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import static com.aecb.AppConstants.ActivityIntentCode.FOR_NO_SCORE_CONTACT_US;
import static com.aecb.AppConstants.ActivityIntentCode.FOR_NO_SCORE_FAQ;
import static com.aecb.AppConstants.IntentKey.OPERATION_TYPE;
import static com.aecb.ui.dashboard.view.DashboardActivity.notificationCount;

public class HelpSupportFragment extends BaseFragment<HelpSupportContract.View, HelpSupportContract.Presenter>
        implements HelpSupportContract.View, View.OnClickListener, TabLayout.BaseOnTabSelectedListener {

    @Inject
    public HelpSupportContract.Presenter mPresenter;
    Bundle bundle;
    private FragmentHelpSupportBinding helpSupportBinding;
    private long lastClickTime = 0;

    public static HelpSupportFragment newInstance(Bundle bundle) {
        HelpSupportFragment fragment = new HelpSupportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        helpSupportBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        View view = helpSupportBinding.getRoot();
        bundle = this.getArguments();
        if (bundle != null && bundle.containsKey(OPERATION_TYPE)) {
            enableBinding(bundle.getInt(OPERATION_TYPE));
        } else {
            enableBinding(0);
        }
        return view;
    }

    private void enableBinding(int operationType) {
        if (notificationCount != 0) {
            helpSupportBinding.ivHistoryFragmentNotification.setImageResource(R.drawable.ic_notification_icon);
        }
        helpSupportBinding.ivAecbLogo.setOnClickListener(this);
        helpSupportBinding.ivHistoryFragmentNotification.setOnClickListener(this);
        //TODO Remove faq for sometime
        //helpSupportBinding.tabLayout.addTab(helpSupportBinding.tabLayout.newTab().setText(R.string.help_support_faq_tab1));
        helpSupportBinding.tabLayout.addTab(helpSupportBinding.tabLayout.newTab().setText(R.string.contact_details));
       // helpSupportBinding.tabLayout.addTab(helpSupportBinding.tabLayout.newTab().setText(R.string.help_support_data_currection_tab3));
        helpSupportBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        helpSupportBinding.viewPagerHelpSupport.setAdapter(new HelpSupportPagerAdapter(getFragmentManager(), helpSupportBinding.tabLayout.getTabCount()));
        helpSupportBinding.tabLayout.addOnTabSelectedListener(this);
        helpSupportBinding.viewPagerHelpSupport.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(helpSupportBinding.tabLayout));
        if (operationType != 0) {
            if (operationType == FOR_NO_SCORE_FAQ) {
                helpSupportBinding.viewPagerHelpSupport.post(() -> helpSupportBinding.viewPagerHelpSupport.setCurrentItem(0));
            } else if (operationType == FOR_NO_SCORE_CONTACT_US) {
                helpSupportBinding.viewPagerHelpSupport.post(() -> helpSupportBinding.viewPagerHelpSupport.setCurrentItem(1));
            }
        } else
            helpSupportBinding.viewPagerHelpSupport.post(() -> helpSupportBinding.viewPagerHelpSupport.setCurrentItem(0));
        helpSupportBinding.viewPagerHelpSupport.setOffscreenPageLimit(3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivHistoryFragmentNotification:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                DashboardActivity.currentTabPosition = 4;
                Fragment someFragment = new NotificationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.flScreensContainer, someFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.ivAecbLogo:
                ((DashboardActivity) getActivity()).loadScreens(new HomeFragment(), 0);
                break;
        }
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_help_support;
    }

    @Override
    public HelpSupportContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        helpSupportBinding.viewPagerHelpSupport.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}