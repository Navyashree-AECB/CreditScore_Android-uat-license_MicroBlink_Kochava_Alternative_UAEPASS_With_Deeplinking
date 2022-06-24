package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.dashboard.presenter.DashboardContract;
import com.aecb.ui.dashboard.presenter.DashboardPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class DashboardModule {

    @Provides
    @ActivityScope
    public DashboardContract.Presenter dashboardPresenter(DashboardPresenterImpl dashboardPresenter) {
        return dashboardPresenter;
    }
}
