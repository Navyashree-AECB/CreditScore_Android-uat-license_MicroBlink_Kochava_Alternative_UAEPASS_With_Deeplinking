package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.purchasejourney.cardupdatedeletefragment.presenter.PaymentUpdateDeleteContract;
import com.aecb.ui.purchasejourney.cardupdatedeletefragment.presenter.PaymentUpdateDeletePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class PaymentUpdateDeleteModule {

    @Provides
    @FragmentScope
    public PaymentUpdateDeleteContract.Presenter cardUpdateDeletePresenter(PaymentUpdateDeletePresenterImpl cardUpdateDeletePresenter) {
        return cardUpdateDeletePresenter;
    }
}