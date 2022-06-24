package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.aboutus.presenter.AboutusContract;
import com.aecb.ui.aboutus.presenter.AboutusImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class AboutUsModule {

    @Provides
    @ActivityScope
    public AboutusContract.Presenter aboutusPresenter(AboutusImpl aboutusPresenter) {
        return aboutusPresenter;
    }
}
