package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.creditreport.creditreport.presenter.CreditReportContract;
import com.aecb.ui.creditreport.creditreport.presenter.CreditReportPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class CreditReportFragmentModule {

    @Provides
    @FragmentScope
    public CreditReportContract.Presenter creditReportPresenter(CreditReportPresenterImpl creditReportPresenter) {
        return creditReportPresenter;
    }
}