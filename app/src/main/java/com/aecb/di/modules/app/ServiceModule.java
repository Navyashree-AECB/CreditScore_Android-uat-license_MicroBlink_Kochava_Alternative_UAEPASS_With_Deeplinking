package com.aecb.di.modules.app;

import com.aecb.di.scopes.ServiceScope;
import com.aecb.services.MyFirebaseMessagingService;

import dagger.Module;
import dagger.Provides;

@Module
public final class ServiceModule {

/*    @ServiceScope
    MyFirebaseMessagingService mService;

    @Provides
    @ServiceScope
    MyFirebaseMessagingService provideMyService() {
        return mService;
    }*/


    @Provides
    @ServiceScope
    public MyFirebaseMessagingService serviceS(MyFirebaseMessagingService mService) {
        return mService;
    }
}