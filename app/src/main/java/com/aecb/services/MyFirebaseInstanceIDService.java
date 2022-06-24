package com.aecb.services;

import com.google.firebase.iid.FirebaseInstanceId;
import com.tumblr.remember.Remember;

import java.util.concurrent.atomic.AtomicReference;

import timber.log.Timber;

public class MyFirebaseInstanceIDService {
    public static final String KEY_FCM_ID = "KEY_FCM_ID";

    /*public static String onTokenRefresh() {
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Timber.d("Refreshed token: " + refreshedToken);
        Remember.putString(KEY_FCM_ID, refreshedToken);
        return refreshedToken;
    }*/


    public static String onTokenRefresh() {
        AtomicReference<String> refreshedToken = new AtomicReference<>("");
        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        Timber.e("getInstanceId failed", task.getException());
                        return;
                    }

                    // Get new Instance ID token
                    refreshedToken.set(task.getResult().getToken());
                    Timber.d("Refreshed token: " + refreshedToken);
                    Remember.putString(KEY_FCM_ID, refreshedToken.get());
                });
        return refreshedToken.get();
    }

}