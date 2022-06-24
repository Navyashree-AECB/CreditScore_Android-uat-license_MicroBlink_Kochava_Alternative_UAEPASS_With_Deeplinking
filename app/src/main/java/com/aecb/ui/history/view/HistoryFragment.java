package com.aecb.ui.history.view;

import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.databinding.FragmentHistoryBinding;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.history.HistoryPagerAdapter;
import com.aecb.ui.history.presenter.HistoryContract;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.ui.notification.view.NotificationFragment;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import static com.aecb.ui.dashboard.view.DashboardActivity.notificationCount;
import static com.aecb.util.FirebaseLogging.visitedScoreHistory;

public class HistoryFragment extends BaseFragment<HistoryContract.View, HistoryContract.Presenter>
        implements HistoryContract.View, TabLayout.OnTabSelectedListener, View.OnClickListener {

    @Inject
    public HistoryContract.Presenter mPresenter;
    HistoryPagerAdapter historyPagerAdapter;
    FragmentHistoryBinding historyBinding;
    private long lastClickTime = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_history;
    }

    @Override
    public HistoryContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        historyBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        View view = historyBinding.getRoot();
        enableBinding();
        return view;
    }

    private void enableBinding() {
        if (notificationCount != 0) {
            historyBinding.ivHistoryFragmentNotification.setImageResource(R.drawable.ic_notification_icon);
        }
        historyBinding.ivHistoryFragmentNotification.setOnClickListener(this);
        historyBinding.tabLayout.addTab(historyBinding.tabLayout.newTab().setText(getString(R.string.report_history)));
        historyBinding.tabLayout.addTab(historyBinding.tabLayout.newTab().setText(getString(R.string.score_history)));
        historyBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        historyBinding.tabLayout.addOnTabSelectedListener(this);
        historyBinding.ivAecbLogo.setOnClickListener(this);
        historyPagerAdapter = new HistoryPagerAdapter(getChildFragmentManager(), historyBinding.tabLayout.getTabCount());
        historyBinding.viewPager.setAdapter(historyPagerAdapter);
        historyBinding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(historyBinding.tabLayout));
        historyBinding.viewPager.post(() -> historyBinding.viewPager.setCurrentItem(0));
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        historyBinding.viewPager.setCurrentItem(tab.getPosition());
        if (tab.getPosition() == 1) {
            visitedScoreHistory();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

}