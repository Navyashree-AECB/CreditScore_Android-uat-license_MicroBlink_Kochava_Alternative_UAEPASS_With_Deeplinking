package com.aecb.presentation.blurcustomview;

import android.content.Context;

import androidx.annotation.NonNull;

public enum SmartAsyncPolicyHolder {
    INSTANCE;

    private AsyncPolicy smartAsyncPolicy;

    public void init(@NonNull Context context) {
        smartAsyncPolicy = new SmartAsyncPolicy(context, true);
    }

    public AsyncPolicy smartAsyncPolicy() {
        return smartAsyncPolicy;
    }
}