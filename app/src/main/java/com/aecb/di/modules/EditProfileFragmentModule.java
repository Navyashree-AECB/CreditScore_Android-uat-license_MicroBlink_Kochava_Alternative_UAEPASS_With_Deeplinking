package com.aecb.di.modules;

import com.aecb.di.modules.app.CompositeDisposableModule;
import com.aecb.di.scopes.FragmentScope;
import com.aecb.ui.editprofile.presenter.EditProfileContract;
import com.aecb.ui.editprofile.presenter.EditProfilePresenterImpl;

import dagger.Module;
import dagger.Provides;

@Module(includes = {CompositeDisposableModule.class})
public final class EditProfileFragmentModule {

    @Provides
    @FragmentScope
    public EditProfileContract.Presenter exitContractPresenterImpl(EditProfilePresenterImpl exitPresenter) {
        return exitPresenter;
    }
}