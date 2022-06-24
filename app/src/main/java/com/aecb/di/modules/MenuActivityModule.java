package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.menu.presenter.MenuContract;
import com.aecb.ui.menu.presenter.MenuPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class MenuActivityModule {

    @Provides
    @ActivityScope
    public MenuContract.Presenter menuPresenter(MenuPresenterImpl menuPresenter) {
        return menuPresenter;
    }
}