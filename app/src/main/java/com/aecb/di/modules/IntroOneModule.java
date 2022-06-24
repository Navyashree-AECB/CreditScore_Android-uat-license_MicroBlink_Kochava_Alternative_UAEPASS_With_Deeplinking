package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.introscreen.introone.presenter.IntroOneContract;
import com.aecb.ui.introscreen.introone.presenter.IntroOnePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class IntroOneModule {

    @Provides
    @FragmentScope
    public IntroOneContract.Presenter introOnePresenter(IntroOnePresenterImpl introOnePresenter) {
        return introOnePresenter;
    }
}