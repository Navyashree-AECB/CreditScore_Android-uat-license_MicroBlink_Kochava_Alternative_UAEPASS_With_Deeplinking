package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.purchaseoptions.presenter.PurchaseOptionsContract;
import com.aecb.ui.purchaseoptions.presenter.PurchaseOptionsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class PurchaseOptionsModule {

    @Provides
    @FragmentScope
    public PurchaseOptionsContract.Presenter purchaseOptionsPresenter(PurchaseOptionsPresenterImpl purchaseOptionsPresenter) {
        return purchaseOptionsPresenter;
    }
}