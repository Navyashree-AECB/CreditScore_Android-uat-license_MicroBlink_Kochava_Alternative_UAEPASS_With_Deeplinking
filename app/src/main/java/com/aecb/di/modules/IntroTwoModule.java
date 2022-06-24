package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.introscreen.introtwo.presenter.IntroTwoContract;
import com.aecb.ui.introscreen.introtwo.presenter.IntroTwoPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class IntroTwoModule {

    @Provides
    @FragmentScope
    public IntroTwoContract.Presenter introTwoPresenter(IntroTwoPresenterImpl introTwoPresenter) {
        return introTwoPresenter;
    }
}