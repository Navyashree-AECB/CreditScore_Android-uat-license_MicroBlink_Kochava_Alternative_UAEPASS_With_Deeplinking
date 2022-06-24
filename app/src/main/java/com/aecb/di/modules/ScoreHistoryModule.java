package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.scorehistory.presenter.ScoreHistoryContract;
import com.aecb.ui.scorehistory.presenter.ScoreHistoryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ScoreHistoryModule {

    @Provides
    @FragmentScope
    public ScoreHistoryContract.Presenter scoreHistoryPresenter(ScoreHistoryPresenterImpl scoreHistoryPresenter) {
        return scoreHistoryPresenter;
    }
}