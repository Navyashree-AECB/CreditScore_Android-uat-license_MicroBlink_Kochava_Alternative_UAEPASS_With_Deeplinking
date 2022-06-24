package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.loginflow.touchid.presenter.TouchIdContract;
import com.aecb.ui.loginflow.touchid.presenter.TouchIdPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class TouchIdModule {

    @Provides
    @FragmentScope
    public TouchIdContract.Presenter touchIdPresenter(TouchIdPresenterImpl touchIdPresenter) {
        return touchIdPresenter;
    }
}