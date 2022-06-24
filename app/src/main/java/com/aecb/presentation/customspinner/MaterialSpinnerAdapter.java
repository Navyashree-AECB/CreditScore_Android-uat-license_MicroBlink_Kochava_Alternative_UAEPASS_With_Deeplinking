package com.aecb.presentation.customspinner;

import android.content.Context;

import java.util.List;

import timber.log.Timber;

public class MaterialSpinnerAdapter<T> extends MaterialSpinnerBaseAdapter {

    private final List<T> items;

    public MaterialSpinnerAdapter(Context context, List<T> items) {
        super(context);
        this.items = items;
    }

    @Override
    public int getCount() {
        int size = items.size();
        if (size == 1 || isHintEnabled()) return size;
        return size - 1;
    }

    @Override
    public T getItem(int position) {
        if (isHintEnabled()) {
            return items.get(position);
        } else if (position >= getSelectedIndex() && items.size() != 1) {
            return items.get(position + 1);
        } else {
            return items.get(position);
        }
    }

    @Override
    public T get(int position) {
        try {
            return items.get(position);
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
            return null;
        }
    }

    @Override
    public List<T> getItems() {
        return items;
    }
}