package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.registerflow.otpverify.presenter.OtpVerifyContract;
import com.aecb.ui.registerflow.otpverify.presenter.OtpVerifyPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class OtpVerifyModule {

    @Provides
    @FragmentScope
    public OtpVerifyContract.Presenter otpVerifyFragmentPresenter(OtpVerifyPresenterImpl otpVerifyPresenter) {
        return otpVerifyPresenter;
    }
}
