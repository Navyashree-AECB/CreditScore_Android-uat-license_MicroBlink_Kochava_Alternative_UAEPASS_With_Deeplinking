package com.aecb.util.visibility;

import android.view.View;

public class Visibility {
    public static void setVisibility(View view, VisibilityStates visible) {
        switch (visible) {
            case VISIBLE:
                view.setVisibility(View.VISIBLE);
                break;
            case GONE:
                view.setVisibility(View.GONE);
                break;
            case INVISIBLE:
                view.setVisibility(View.INVISIBLE);
        }
    }
}