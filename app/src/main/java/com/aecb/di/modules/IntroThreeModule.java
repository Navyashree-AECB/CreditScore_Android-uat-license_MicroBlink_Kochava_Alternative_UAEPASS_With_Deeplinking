package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.introscreen.introthree.presenter.IntroThreeContract;
import com.aecb.ui.introscreen.introthree.presenter.IntroThreePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class IntroThreeModule {

    @Provides
    @FragmentScope
    public IntroThreeContract.Presenter introThreePresenter(IntroThreePresenterImpl introThreePresenter) {
        return introThreePresenter;
    }
}