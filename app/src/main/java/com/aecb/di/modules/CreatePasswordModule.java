package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.registerflow.createpassword.presenter.CreatePasswordContract;
import com.aecb.ui.registerflow.createpassword.presenter.CreatePasswordPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class CreatePasswordModule {

    @Provides
    @ActivityScope
    public CreatePasswordContract.Presenter createPasswordPresenter(CreatePasswordPresenterImpl createPasswordPresenter) {
        return createPasswordPresenter;
    }
}