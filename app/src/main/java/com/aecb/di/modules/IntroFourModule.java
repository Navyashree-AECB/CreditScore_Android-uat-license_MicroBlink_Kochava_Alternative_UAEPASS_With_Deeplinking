package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.introscreen.introfour.presenter.IntroFourContract;
import com.aecb.ui.introscreen.introfour.presenter.IntroFourPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class IntroFourModule {

    @Provides
    @FragmentScope
    public IntroFourContract.Presenter introFourPresenter(IntroFourPresenterImpl introFourPresenter) {
        return introFourPresenter;
    }
}