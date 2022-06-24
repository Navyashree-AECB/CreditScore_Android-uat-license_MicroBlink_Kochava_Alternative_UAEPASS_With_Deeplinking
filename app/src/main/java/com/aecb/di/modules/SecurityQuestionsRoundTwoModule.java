package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.registerflow.securityquestionsroundtwo.presenter.SecurityQuestionsRoundTwoContract;
import com.aecb.ui.registerflow.securityquestionsroundtwo.presenter.SecurityQuestionsRoundTwoPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class SecurityQuestionsRoundTwoModule {

    @Provides
    @ActivityScope
    public SecurityQuestionsRoundTwoContract.Presenter securityQuestionsRoundTwoPresenter(
            SecurityQuestionsRoundTwoPresenterImpl securityQuestionsRoundTwoPresenter) {
        return securityQuestionsRoundTwoPresenter;
    }
}