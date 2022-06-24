package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.noscorefragment.presenter.NoScoreContract;
import com.aecb.ui.noscorefragment.presenter.NoScoreImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class NoScoreModule {
    @Provides
    @FragmentScope
    public NoScoreContract.Presenter noScorePresenter(NoScoreImpl noScorePresenter) {
        return noScorePresenter;
    }
}
