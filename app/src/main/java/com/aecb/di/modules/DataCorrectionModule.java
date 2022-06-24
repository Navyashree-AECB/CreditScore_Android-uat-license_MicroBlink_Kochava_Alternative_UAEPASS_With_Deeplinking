package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.helpandsupport.datacorrection.presenter.DataCorrectionContract;
import com.aecb.ui.helpandsupport.datacorrection.presenter.DataCorrectionImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class DataCorrectionModule {
    @Provides
    @FragmentScope
    public DataCorrectionContract.Presenter dataCorrection(DataCorrectionImpl dataCorrection) {
        return dataCorrection;
    }
}

