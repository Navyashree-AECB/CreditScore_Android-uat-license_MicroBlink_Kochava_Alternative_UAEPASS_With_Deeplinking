package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.introscreen.intromain.presenter.IntroMainContract;
import com.aecb.ui.introscreen.intromain.presenter.IntroMainPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class IntroMainModule {

    @Provides
    @ActivityScope
    public IntroMainContract.Presenter introPresenter(IntroMainPresenterImpl introPresenter) {
        return introPresenter;
    }
}
