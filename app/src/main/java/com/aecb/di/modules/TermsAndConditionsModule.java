package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.termsandconditions.presenter.TermsConditionContract;
import com.aecb.ui.termsandconditions.presenter.TermsConditionPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class TermsAndConditionsModule {

    @Provides
    @FragmentScope
    public TermsConditionContract.Presenter termsConditionPresenter(TermsConditionPresenterImpl termsConditionPresenter) {
        return termsConditionPresenter;
    }
}