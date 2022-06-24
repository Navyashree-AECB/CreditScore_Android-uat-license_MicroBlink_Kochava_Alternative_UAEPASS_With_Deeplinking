package com.aecb.presentation.blurcustomview;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.UiThread;

public interface BlurEngine {

    @Nullable
    Bitmap execute(@NonNull Bitmap inBitmap, boolean canReuseInBitmap);

    @Nullable
    Bitmap execute(@NonNull Bitmap inBitmap, @NonNull Bitmap outBitmap);

    void execute(@NonNull Bitmap inBitmap, boolean canReuseInBitmap, @NonNull Callback callback);

    void execute(@NonNull Bitmap inBitmap, @NonNull Bitmap outBitmap, @NonNull Callback callback);

    void destroy();

    @NonNull
    String methodDescription();

    interface Callback {
        @UiThread
        void onFinished(@Nullable Bitmap blurredBitmap);
    }
}