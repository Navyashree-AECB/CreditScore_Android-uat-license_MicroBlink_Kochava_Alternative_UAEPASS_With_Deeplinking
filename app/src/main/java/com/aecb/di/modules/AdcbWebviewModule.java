package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.adcbwebview.presenter.AdcbWebviewContract;
import com.aecb.ui.adcbwebview.presenter.AdcbWebviewPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class AdcbWebviewModule {

    @Provides
    @ActivityScope
    public AdcbWebviewContract.Presenter adcbWebviewPresenter(AdcbWebviewPresenterImpl adcbWebviewPresenter) {
        return adcbWebviewPresenter;
    }
}
