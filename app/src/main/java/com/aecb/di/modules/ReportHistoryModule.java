package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.reporthistory.presenter.ReportHistoryContract;
import com.aecb.ui.reporthistory.presenter.ReportHistoryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ReportHistoryModule {

    @Provides
    @FragmentScope
    public ReportHistoryContract.Presenter reportHistoryPresenter(ReportHistoryPresenterImpl reportHistoryPresenter) {
        return reportHistoryPresenter;
    }
}