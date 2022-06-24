package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.searchdialog.presenter.SearchContract;
import com.aecb.ui.searchdialog.presenter.SearchPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class SearchModule {
    @Provides
    @FragmentScope
    public SearchContract.Presenter searchPresenter(SearchPresenterImpl searchPresenter) {
        return searchPresenter;
    }
}
