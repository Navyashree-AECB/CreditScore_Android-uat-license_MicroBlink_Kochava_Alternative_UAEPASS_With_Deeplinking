package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.creditreport.intro.presenter.IntroContract;
import com.aecb.ui.creditreport.intro.presenter.IntroPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class IntroFragmentModule {

    @Provides
    @FragmentScope
    public IntroContract.Presenter creditReportPresenter(IntroPresenterImpl introPresenter) {
        return introPresenter;
    }
}