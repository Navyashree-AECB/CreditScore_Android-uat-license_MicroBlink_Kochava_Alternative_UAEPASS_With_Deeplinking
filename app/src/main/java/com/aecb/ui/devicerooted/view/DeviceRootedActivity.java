package com.aecb.ui.devicerooted.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityDeviceRootedBinding;
import com.aecb.ui.devicerooted.presenter.DeviceRootedContract;

import javax.inject.Inject;

public class DeviceRootedActivity extends BaseActivity<DeviceRootedContract.View,
        DeviceRootedContract.Presenter> implements DeviceRootedContract.View {

    @Inject
    public DeviceRootedContract.Presenter mPresenter;
    ActivityDeviceRootedBinding activityDeviceRootedBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDeviceRootedBinding = DataBindingUtil.setContentView(this, getLayoutRes());
    }

    @NonNull
    @Override
    public DeviceRootedContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_device_rooted;
    }

    @Override
    public void initToolbar() {
    }
}