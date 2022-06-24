package com.aecb.di.modules.app;

import dagger.Module;
import dagger.Provides;
import io.reactivex.disposables.CompositeDisposable;

@Module
public class CompositeDisposableModule {
    @Provides
    public CompositeDisposable providesCompositeDisposable() {
        return new CompositeDisposable();
    }
}
