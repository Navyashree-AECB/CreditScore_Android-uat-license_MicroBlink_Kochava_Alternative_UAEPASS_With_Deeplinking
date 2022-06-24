package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.devicerooted.presenter.DeviceRootedContract;
import com.aecb.ui.devicerooted.presenter.DeviceRootedPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class DeviceRootedModule {
    @Provides
    @ActivityScope
    public DeviceRootedContract.Presenter deviceRootedPresenter(DeviceRootedPresenterImpl deviceRootedPresenter) {
        return deviceRootedPresenter;
    }
}