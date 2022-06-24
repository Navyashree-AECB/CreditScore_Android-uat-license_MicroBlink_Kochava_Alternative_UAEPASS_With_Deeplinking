package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.scanemirate.presenter.ScanEmirateIdContract;
import com.aecb.ui.scanemirate.presenter.ScanEmirateIdImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ScanEmirateIdModule {

    @Provides
    @ActivityScope
    public ScanEmirateIdContract.Presenter presenter(ScanEmirateIdImpl scanEmirateId) {
        return scanEmirateId;
    }
}
