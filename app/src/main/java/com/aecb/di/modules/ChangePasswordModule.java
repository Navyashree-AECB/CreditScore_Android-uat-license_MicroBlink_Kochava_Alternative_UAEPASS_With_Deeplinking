package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.changepassword.presenter.ChangePasswordContract;
import com.aecb.ui.changepassword.presenter.ChangePasswordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ChangePasswordModule {

    @Provides
    @ActivityScope
    public ChangePasswordContract.Presenter changePasswordPresenterImpl(ChangePasswordPresenterImpl changePasswordPresenterImpl) {
        return changePasswordPresenterImpl;
    }
}
