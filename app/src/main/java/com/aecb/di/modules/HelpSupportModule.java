package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.helpandsupport.presenter.HelpSupportContract;
import com.aecb.ui.helpandsupport.presenter.HelpSupportPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class HelpSupportModule {

    @Provides
    @FragmentScope
    public HelpSupportContract.Presenter helpSupportPresenter(HelpSupportPresenterImpl helpSupportPresenter) {
        return helpSupportPresenter;
    }
}