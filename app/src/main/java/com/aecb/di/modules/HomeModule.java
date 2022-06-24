package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.home.presenter.HomeContract;
import com.aecb.ui.home.presenter.HomePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class HomeModule {

    @Provides
    @FragmentScope
    public HomeContract.Presenter homePresenter(HomePresenterImpl homePresenter) {
        return homePresenter;
    }
}