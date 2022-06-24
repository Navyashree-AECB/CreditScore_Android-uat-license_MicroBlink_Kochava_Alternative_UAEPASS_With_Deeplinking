package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.registerflow.personaldetails.presenter.PersonalDetailsContract;
import com.aecb.ui.registerflow.personaldetails.presenter.PersonalDetailsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class PersonalDetailsModule {

    @Provides
    @ActivityScope
    public PersonalDetailsContract.Presenter personalDetailsPresenter(PersonalDetailsPresenterImpl personalDetailsPresenter) {
        return personalDetailsPresenter;
    }
}