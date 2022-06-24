package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.addpassportdob.presenter.AddDobPassportContract;
import com.aecb.ui.addpassportdob.presenter.AddDobPassportPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class AddDobPassportModule {

    @Provides
    @ActivityScope
    public AddDobPassportContract.Presenter addDobPassportPresenter(AddDobPassportPresenterImpl addDobPassportPresenter) {
        return addDobPassportPresenter;
    }
}