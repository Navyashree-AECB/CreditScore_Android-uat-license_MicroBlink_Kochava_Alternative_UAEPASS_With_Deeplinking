package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.creditreport.glossary.presenter.GlossaryContract;
import com.aecb.ui.creditreport.glossary.presenter.GlossaryPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class GlossaryFragmentModule {

    @Provides
    @FragmentScope
    public GlossaryContract.Presenter glossaryPresenterImpl(GlossaryPresenterImpl glossaryPresenter) {
        return glossaryPresenter
                ;
    }
}