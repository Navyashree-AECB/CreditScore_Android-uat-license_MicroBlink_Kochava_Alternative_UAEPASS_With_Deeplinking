package com.aecb.presentation.blurcustomview;

import androidx.annotation.AnyThread;

@AnyThread
public interface AsyncPolicy {
    boolean shouldAsync(boolean isRenderScript, long computation);
    void putSampleData(boolean isRenderScript, long computation, long timeInNanos);
}