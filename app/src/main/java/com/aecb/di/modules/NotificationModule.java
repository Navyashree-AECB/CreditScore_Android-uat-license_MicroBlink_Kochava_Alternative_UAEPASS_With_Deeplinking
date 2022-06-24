package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.notification.presenter.NotificationContract;
import com.aecb.ui.notification.presenter.NotificationPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class NotificationModule {

    @Provides
    @FragmentScope
    public NotificationContract.Presenter otpVerifyFragmentPresenter(NotificationPresenterImpl otpVerifyPresenter) {
        return otpVerifyPresenter;
    }
}
