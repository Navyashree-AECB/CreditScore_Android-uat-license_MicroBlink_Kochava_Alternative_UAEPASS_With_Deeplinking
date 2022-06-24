package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.profile.presenter.ProfileContract;
import com.aecb.ui.profile.presenter.ProfilePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ProfileActivityModule {

    @Provides
    @ActivityScope
    public ProfileContract.Presenter profilePresenter(ProfilePresenterImpl profilePresenter) {
        return profilePresenter;
    }
}
