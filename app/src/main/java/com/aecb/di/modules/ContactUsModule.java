package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.helpandsupport.contactus.presenter.ContactUsContract;
import com.aecb.ui.helpandsupport.contactus.presenter.ContactUsFragmentImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ContactUsModule {

    @Provides
    @FragmentScope
    public ContactUsContract.Presenter contactUsPresenter(ContactUsFragmentImpl contactUsFragmentPresenter) {
        return contactUsFragmentPresenter;
    }
}
