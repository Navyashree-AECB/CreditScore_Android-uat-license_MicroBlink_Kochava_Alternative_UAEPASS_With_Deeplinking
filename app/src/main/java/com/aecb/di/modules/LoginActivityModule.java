package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.loginflow.login.presenter.LoginContract;
import com.aecb.ui.loginflow.login.presenter.LoginPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class LoginActivityModule {

    @Provides
    @ActivityScope
    public LoginContract.Presenter loginActivityPresenter(LoginPresenterImpl loginPresenter) {
        return loginPresenter;
    }
}