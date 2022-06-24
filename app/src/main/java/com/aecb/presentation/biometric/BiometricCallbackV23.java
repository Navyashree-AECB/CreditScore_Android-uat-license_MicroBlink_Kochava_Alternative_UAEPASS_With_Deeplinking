package com.aecb.presentation.biometric;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.os.CancellationSignal;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

import com.aecb.ui.loginflow.login.view.LoginActivity;
import com.aecb.ui.startsup.view.StartupActivity;

@RequiresApi(api = Build.VERSION_CODES.M)
public class BiometricCallbackV23 extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private CancellationSignal cancellationSignal;

    // Constructor
    public BiometricCallbackV23(Context mContext) {
        context = mContext;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void startAuth(FingerprintManager manager, FingerprintManager.CryptoObject cryptoObject) {
        cancellationSignal = new CancellationSignal();
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.USE_FINGERPRINT) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        manager.authenticate(cryptoObject, cancellationSignal, 0, this, null);
    }

    @Override
    public void onAuthenticationError(int errMsgId, CharSequence errString) {
        this.cancellationSignal("Fingerprint Auth error\n" + errString);
    }

    @Override
    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
        this.cancellationSignal("Fingerprint Auth help\n" + helpString);
    }

    @Override
    public void onAuthenticationFailed() {
        this.cancellationSignal("Fingerprint Auth failed.");
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        if (context instanceof LoginActivity) {
            ((LoginActivity) context).biometricAuthenticationSucceeded();
        } else if (context instanceof StartupActivity) {
            ((StartupActivity) context).biometricAuthenticationSucceeded();
        }
    }

    public void cancellationSignal(String e) {
        Log.e("Biometric", e);
        if (this.cancellationSignal != null && !this.cancellationSignal.isCanceled()) {
            this.cancellationSignal.cancel();
            this.cancellationSignal = null;
        }
    }

    public CancellationSignal getCancellationSignal() {
        return this.cancellationSignal;
    }

}