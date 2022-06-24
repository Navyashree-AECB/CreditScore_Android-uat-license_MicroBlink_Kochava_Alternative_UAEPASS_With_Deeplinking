package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.loginflow.forgotpassword.presenter.ForgotPasswordContract;
import com.aecb.ui.loginflow.forgotpassword.presenter.ForgotPasswordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ForgotPasswordModule {

    @Provides
    @ActivityScope
    public ForgotPasswordContract.Presenter forgotPasswordPresenter(ForgotPasswordPresenterImpl forgotPasswordPresenter) {
        return forgotPasswordPresenter;
    }
}