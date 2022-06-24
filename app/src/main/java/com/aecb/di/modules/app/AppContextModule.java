package com.aecb.di.modules.app;

import android.app.Application;
import android.content.Context;

import com.aecb.di.qualifiers.AppContext;
import com.aecb.di.scopes.AppScope;

import dagger.Binds;
import dagger.Module;

@Module
public abstract class AppContextModule {

    @Binds
    @AppContext
    @AppScope
    public abstract Context bindContext(Application application);
}
