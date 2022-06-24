package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.registerflow.securityquestionsroundone.presenter.SecurityQuestionsRoundOneContract;
import com.aecb.ui.registerflow.securityquestionsroundone.presenter.SecurityQuestionsRoundOnePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class SecurityQuestionsRoundOneModule {

    @Provides
    @ActivityScope
    public SecurityQuestionsRoundOneContract.Presenter securityQuestionsRoundOnePresenter(
            SecurityQuestionsRoundOnePresenterImpl securityQuestionsRoundOnePresenter) {
        return securityQuestionsRoundOnePresenter;
    }
}