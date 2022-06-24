package com.aecb.ui.maintanance.view;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.databinding.ActivityMaintenanceBinding;
import com.aecb.ui.maintanance.presenter.MaintenanceContract;
import com.aecb.util.ValidationUtil;

import javax.inject.Inject;

public class MaintenanceActivity extends BaseActivity<MaintenanceContract.View,
        MaintenanceContract.Presenter> implements MaintenanceContract.View {

    @Inject
    public MaintenanceContract.Presenter mPresenter;
    ActivityMaintenanceBinding maintenanceBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        maintenanceBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        getBundleValues();

    }

    @NonNull
    @Override
    public MaintenanceContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_maintenance;
    }

    @Override
    public void initToolbar() {

    }

    private void getBundleValues() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            String des = bundle.getString(AppConstants.AppUnderMaintenance.DES);
            String title = bundle.getString(AppConstants.AppUnderMaintenance.Title);
            if (!ValidationUtil.isNullOrEmpty(title)) {
                maintenanceBinding.tvTitle.setText(title);
            }
            if (!ValidationUtil.isNullOrEmpty(des)) {
                maintenanceBinding.tvDescription.setText(des);
            }
        }
    }
}