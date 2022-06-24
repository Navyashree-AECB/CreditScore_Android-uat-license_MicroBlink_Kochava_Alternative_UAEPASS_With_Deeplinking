package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.contactusregistration.presenter.ContactUsRegistrationContract;
import com.aecb.ui.contactusregistration.presenter.ContactUsRegistrationPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class ContactUsRegistrationModule {

    @Provides
    @FragmentScope
    public ContactUsRegistrationContract.Presenter contactUsRegistrationPresenter(
            ContactUsRegistrationPresenterImpl contactUsRegistrationPresenter) {
        return contactUsRegistrationPresenter;
    }
}
