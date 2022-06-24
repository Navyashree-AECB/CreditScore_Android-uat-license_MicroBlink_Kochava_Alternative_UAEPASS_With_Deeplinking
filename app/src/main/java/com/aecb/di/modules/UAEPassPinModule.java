package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.loginflow.uaepasspin.presenter.UAEPassPinContract;
import com.aecb.ui.loginflow.uaepasspin.presenter.UAEPassPinPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class UAEPassPinModule {

    @Provides
    @ActivityScope
    public UAEPassPinContract.Presenter uaePinActivityPresenter(UAEPassPinPresenterImpl uaePinPresenter) {
        return uaePinPresenter;
    }
}