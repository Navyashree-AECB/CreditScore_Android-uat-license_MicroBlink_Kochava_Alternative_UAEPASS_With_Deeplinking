package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.startsup.presenter.StartupContract;
import com.aecb.ui.startsup.presenter.StartupPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class StartupActivityModule {

    @Provides
    @ActivityScope
    public StartupContract.Presenter startupActivityPresenter(StartupPresenterImpl startupPresenter) {
        return startupPresenter;
    }
}