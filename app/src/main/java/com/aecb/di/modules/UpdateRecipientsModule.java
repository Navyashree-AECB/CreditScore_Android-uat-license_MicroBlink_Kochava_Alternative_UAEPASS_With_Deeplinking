package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.updaterecipients.presenter.UpdateRecipientsContract;
import com.aecb.ui.updaterecipients.presenter.UpdateRecipientsPresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class UpdateRecipientsModule {

    @Provides
    @FragmentScope
    public UpdateRecipientsContract.Presenter updateRecipientsPresenter(UpdateRecipientsPresenterImpl updateRecipientsPresenter) {
        return updateRecipientsPresenter;
    }
}