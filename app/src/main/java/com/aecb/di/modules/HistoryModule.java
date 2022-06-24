package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.history.presenter.HistoryContract;
import com.aecb.ui.history.presenter.HistoryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class HistoryModule {

    @Provides
    @FragmentScope
    public HistoryContract.Presenter historyPresenter(HistoryPresenterImpl historyPresenter) {
        return historyPresenter;
    }
}