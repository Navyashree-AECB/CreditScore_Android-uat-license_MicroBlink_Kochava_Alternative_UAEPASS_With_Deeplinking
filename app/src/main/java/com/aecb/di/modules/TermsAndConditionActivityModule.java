package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.termsandcodition.presenter.TermsAndConditionContract;
import com.aecb.ui.termsandcodition.presenter.TermsAndConditionImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class TermsAndConditionActivityModule {

    @Provides
    @ActivityScope
    public TermsAndConditionContract.Presenter termsAndConditionPresenter(TermsAndConditionImpl termsAndConditionPresenter) {
        return termsAndConditionPresenter;
    }

}
