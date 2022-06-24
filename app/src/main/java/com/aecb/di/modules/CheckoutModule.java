package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.checkout.presenter.CheckoutContract;
import com.aecb.ui.checkout.presenter.CheckoutPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class CheckoutModule {

    @Provides
    @ActivityScope
    public CheckoutContract.Presenter checkoutPresenter(CheckoutPresenterImpl checkoutPresenter) {
        return checkoutPresenter;
    }
}