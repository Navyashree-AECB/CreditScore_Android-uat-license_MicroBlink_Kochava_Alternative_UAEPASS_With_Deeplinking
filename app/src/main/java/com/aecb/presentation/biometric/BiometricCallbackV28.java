package com.aecb.presentation.biometric;

import android.content.Context;
import android.hardware.biometrics.BiometricPrompt;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.startsup.view.StartupActivity;

@RequiresApi(api = Build.VERSION_CODES.P)
public class BiometricCallbackV28 extends BiometricPrompt.AuthenticationCallback {

    private Context mContext;
    private CancellationSignal mCancellationSignal;

    public BiometricCallbackV28(Context context, CancellationSignal cancellationSignal) {
        mContext = context;
        mCancellationSignal = cancellationSignal;
    }

    @Override
    public void onAuthenticationSucceeded(BiometricPrompt.AuthenticationResult result) {
        super.onAuthenticationSucceeded(result);
        if (mContext instanceof LoginActivity) {
            ((LoginActivity) mContext).biometricAuthenticationSucceeded();
        } else if (mContext instanceof StartupActivity) {
            ((StartupActivity) mContext).biometricAuthenticationSucceeded();
        }
    }

    @Override
    public void onAuthenticationHelp(int helpCode, CharSequence helpString) {
        super.onAuthenticationHelp(helpCode, helpString);
        this.cancellationSignal("Fingerprint Auth Help \n" + helpString);
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        super.onAuthenticationError(errorCode, errString);
        this.cancellationSignal("Fingerprint Auth error\n" + errString);
    }

    @Override
    public void onAuthenticationFailed() {
        super.onAuthenticationFailed();
        this.cancellationSignal("Fingerprint Authentication failed.");
    }

    public void cancellationSignal(String e) {
        Log.e("Biometric", e);
        if (this.mCancellationSignal != null && !this.mCancellationSignal.isCanceled()) {
            this.mCancellationSignal.cancel();
            this.mCancellationSignal = null;
        }
    }

    public CancellationSignal getCancellationSignal() {
        return this.mCancellationSignal;
    }
}