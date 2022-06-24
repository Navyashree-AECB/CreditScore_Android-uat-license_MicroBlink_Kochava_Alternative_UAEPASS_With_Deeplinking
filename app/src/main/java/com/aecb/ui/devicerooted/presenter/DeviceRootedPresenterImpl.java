package com.aecb.ui.devicerooted.presenter;

import com.aecb.base.mvp.MvpBasePresenterImpl;

import javax.inject.Inject;

public class DeviceRootedPresenterImpl extends MvpBasePresenterImpl<DeviceRootedContract.View>
        implements DeviceRootedContract.Presenter {

    @Inject
    DeviceRootedPresenterImpl() {
    }
}