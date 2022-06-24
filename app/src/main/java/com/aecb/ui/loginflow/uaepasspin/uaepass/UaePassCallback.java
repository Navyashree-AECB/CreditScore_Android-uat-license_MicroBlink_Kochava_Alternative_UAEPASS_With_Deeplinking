package com.aecb.ui.loginflow.uaepasspin.uaepass;

public interface UaePassCallback {
    void uaePassResponse(int type, String response);
    void uaePassError(int type);
}
