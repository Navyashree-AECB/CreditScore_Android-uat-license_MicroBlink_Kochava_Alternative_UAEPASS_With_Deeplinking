package com.aecb.presentation.blurcustomview;

import androidx.annotation.AnyThread;

@AnyThread
public class SimpleAsyncPolicy implements AsyncPolicy {

    @Override
    public boolean shouldAsync(boolean isRenderScript, long computation) {
        return !isRenderScript;
    }

    @Override
    public void putSampleData(boolean isRenderScript, long computation, long timeInNanos) {
    }
}