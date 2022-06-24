package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.maintanance.presenter.MaintenanceContract;
import com.aecb.ui.maintanance.presenter.MaintenancePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class MaintenanceActivityModule {
    @Provides
    @ActivityScope
    public MaintenanceContract.Presenter maintenancePresenter(MaintenancePresenterImpl maintenancePresenter) {
        return maintenancePresenter;
    }
}
