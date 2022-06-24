package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.splash.presenter.SplashContract;
import com.aecb.ui.splash.presenter.SplashPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class SplashActivityModule {

    @Provides
    @ActivityScope
    public SplashContract.Presenter splashActivityPresenter(SplashPresenterImpl splashPresenter) {
        return splashPresenter;
    }
}
