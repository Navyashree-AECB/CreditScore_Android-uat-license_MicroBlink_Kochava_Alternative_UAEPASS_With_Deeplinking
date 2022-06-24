package com.aecb.util.inflatelayout;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.LayoutRes;

import com.aecb.listeners.TextChangeListener;

public class InflateUtils {

    public static View inflate(Context context, @LayoutRes int resId, ViewGroup parent, boolean attachToRoot) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        return inflater.inflate(resId, parent, attachToRoot);
    }

    public static void setVisibility(View view, boolean visible) {
        view.setVisibility(visible ? View.VISIBLE : View.GONE);
    }

    public static void afterTextChanged(EditText editText, TextChangeListener afterTextChanged) {
        editText.addTextChangedListener((new TextWatcher() {
            public void beforeTextChanged(CharSequence p0, int p1, int p2, int p3) {
            }

            public void onTextChanged(CharSequence p0, int p1, int p2, int p3) {
            }

            public void afterTextChanged(Editable editable) {
                afterTextChanged.invoke(String.valueOf(editable));
            }
        }));
    }
}