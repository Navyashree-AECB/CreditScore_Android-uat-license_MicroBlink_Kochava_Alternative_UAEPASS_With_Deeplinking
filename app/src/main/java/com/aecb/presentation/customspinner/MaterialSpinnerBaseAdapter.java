package com.aecb.presentation.customspinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.data.preference.PrefHelperImpl;
import com.aecb.util.ValidationUtil;

import java.util.List;

public abstract class MaterialSpinnerBaseAdapter<T> extends BaseAdapter {

    private final Context context;
    private int selectedIndex;
    private int textColor;
    private int backgroundSelector;
    private int popupPaddingTop;
    private int popupPaddingLeft;
    private int popupPaddingBottom;
    private int popupPaddingRight;
    private boolean isHintEnabled;
    private String lang;

    public MaterialSpinnerBaseAdapter(Context context) {
        this.context = context;
        String mAppLang = PrefHelperImpl.getAppLanguage();
        if (!ValidationUtil.isNullOrEmpty(mAppLang)) {
            if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                lang = AppConstants.AppLanguage.ISO_CODE_ENG;
            } else if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                lang = AppConstants.AppLanguage.ISO_CODE_ARABIC;
            }
        } else {
            lang = AppConstants.AppLanguage.ISO_CODE_ENG;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final TextView textView;
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.custom_spinner_item, parent, false);
            textView = convertView.findViewById(R.id.tv_tinted_spinner);
            textView.setTextColor(textColor);

            textView.setPadding(popupPaddingLeft, popupPaddingTop, popupPaddingRight, popupPaddingBottom);

            if (backgroundSelector != 0) {
                textView.setBackgroundResource(backgroundSelector);
            }

            if (!ValidationUtil.isNullOrEmpty(lang)) {
                if (lang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                    textView.setTextDirection(View.LAYOUT_DIRECTION_LTR);
                } else if (lang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                    textView.setTextDirection(View.LAYOUT_DIRECTION_RTL);
                }
            } else {
                textView.setTextDirection(View.LAYOUT_DIRECTION_LTR);
            }
            convertView.setTag(new ViewHolder(textView));
        } else {
            textView = ((ViewHolder) convertView.getTag()).textView;
        }
        String text = "";
        if (getItemText(position) != null) {
            text = getItemText(position);
        }
        textView.setText(text);
        return convertView;
    }

    public String getItemText(int position) {
        return getItem(position) == null ? "" : getItem(position).toString();
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void notifyItemSelected(int index) {
        selectedIndex = index;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public abstract T getItem(int position);

    @Override
    public abstract int getCount();

    public abstract T get(int position);

    public abstract List<T> getItems();

    public boolean isHintEnabled() {
        return this.isHintEnabled;
    }

    public void setHintEnabled(boolean isHintEnabled) {
        this.isHintEnabled = isHintEnabled;
    }

    public MaterialSpinnerBaseAdapter<T> setTextColor(@ColorInt int textColor) {
        this.textColor = textColor;
        return this;
    }

    public MaterialSpinnerBaseAdapter<T> setBackgroundSelector(@DrawableRes int backgroundSelector) {
        this.backgroundSelector = backgroundSelector;
        return this;
    }

    public MaterialSpinnerBaseAdapter<T> setPopupPadding(int left, int top, int right, int bottom) {
        this.popupPaddingLeft = left;
        this.popupPaddingTop = top;
        this.popupPaddingRight = right;
        this.popupPaddingBottom = bottom;
        return this;
    }

    private static class ViewHolder {

        private TextView textView;

        private ViewHolder(TextView textView) {
            this.textView = textView;
        }
    }
}