package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.ActivityScope;
import com.aecb.ui.registerflow.contactdetails.presenter.ContactDetailsContract;
import com.aecb.ui.registerflow.contactdetails.presenter.ContactDetailsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ContactDetailsModule {

    @Provides
    @ActivityScope
    public ContactDetailsContract.Presenter contactDetailsPresenter(ContactDetailsPresenterImpl contactDetailsPresenter) {
        return contactDetailsPresenter;
    }
}