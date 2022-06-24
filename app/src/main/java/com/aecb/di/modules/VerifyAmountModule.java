package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.verifyamountforpayment.presenter.VerifyAmountContract;
import com.aecb.ui.verifyamountforpayment.presenter.VerifyAmountPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class VerifyAmountModule {

    @Provides
    @ActivityScope
    public VerifyAmountContract.Presenter verifyAmountPresenter(VerifyAmountPresenterImpl verifyAmountPresenter) {
        return verifyAmountPresenter;
    }
}