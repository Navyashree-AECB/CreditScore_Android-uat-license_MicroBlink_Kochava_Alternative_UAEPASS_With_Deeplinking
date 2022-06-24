package com.aecb.presentation.monthpicker;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.util.ScreenUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MonthPicker {

    private AlertDialog mAlertDialog;
    private MonthPicker.Builder builder;
    private Context context;
    private TextView mPositiveButton;
    private TextView mNegativeButton;
    private DateMonthDialogListener dateMonthDialogListener;
    private OnCancelMonthDialogListener onCancelMonthDialogListener;
    private boolean isBuild = false;

    public MonthPicker(Context context) {
        this.context = context;
        builder = new Builder();
    }

    public MonthPicker(Activity activity) {
        this.context = activity;
        builder = new Builder();
    }

    public void show() {
        if (isBuild) {
            mAlertDialog.show();
            builder.setDefault();
        } else {
            builder.build();
            isBuild = true;
        }
    }

    /**
     * set action callback when positive button clicked
     *
     * @param dateMonthDialogListener
     * @return
     */
    public MonthPicker setPositiveButton(DateMonthDialogListener dateMonthDialogListener) {
        this.dateMonthDialogListener = dateMonthDialogListener;
        mPositiveButton.setOnClickListener(builder.positiveButtonClick());
        return this;
    }

    /**
     * set action callback when negative button clicked
     *
     * @param onCancelMonthDialogListener
     * @return
     */
    public MonthPicker setNegativeButton(OnCancelMonthDialogListener onCancelMonthDialogListener) {
        this.onCancelMonthDialogListener = onCancelMonthDialogListener;
        mNegativeButton.setOnClickListener(builder.negativeButtonClick());
        return this;
    }

    /**
     * change text positive button
     *
     * @param text
     * @return
     */
    public MonthPicker setPositiveText(String text) {
        mPositiveButton.setText(text);
        return this;
    }

    /**
     * change text negative button
     *
     * @param text
     * @return
     */
    public MonthPicker setNegativeText(String text) {
        mNegativeButton.setText(text);
        return this;
    }

    /**
     * set localization show month
     *
     * @param locale
     * @return
     */
    public MonthPicker setLocale(Locale locale) {
        builder.setLocale(locale);
        return this;
    }

    /**
     * change default selected month (1 - 12)
     *
     * @param month
     * @return
     */
    public MonthPicker setSelectedMonth(int month) {
        builder.setSelectedMonth(month);
        return this;
    }

    /**
     * change default selected year
     *
     * @param year
     * @return
     */
    public MonthPicker setSelectedYear(int year) {
        builder.setSelectedYear(year);
        return this;
    }

    /**
     * change color theme
     *
     * @param color
     * @return
     */
    public MonthPicker setColorTheme(int color) {
        builder.setColorTheme(color);
        return this;
    }

    public void dismiss() {
        if (App.appComponent.getDataManager().getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
            context.getResources().getConfiguration().setLocale(localeStartDate);
            Locale.setDefault(context.getResources().getConfiguration().locale);
        } else {
            Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ARABIC);
            context.getResources().getConfiguration().setLocale(localeStartDate);
            Locale.setDefault(context.getResources().getConfiguration().locale);
        }
        mAlertDialog.dismiss();
    }

    private class Builder implements MonthAdapter.OnSelectedListener {

        NumberPicker nopicker;
        RecyclerView recyclerView;
        ImageView btnBack;
        private MonthAdapter monthAdapter;
        private TextView mTitleMonth, mTitleYear;
        private TextView mYear;
        private int year;
        private int month;
        private AlertDialog.Builder alertBuilder;
        private View contentView;

        private Builder() {
            alertBuilder = new AlertDialog.Builder(context);
            alertBuilder.setCancelable(false);
            contentView = LayoutInflater.from(context).inflate(R.layout.dialog_month_picker, null);
            contentView.setFocusable(true);
            contentView.setFocusableInTouchMode(true);

            mTitleMonth = (TextView) contentView.findViewById(R.id.titleMonth);
            mTitleYear = (TextView) contentView.findViewById(R.id.titleYear);
            mYear = (TextView) contentView.findViewById(R.id.textYear);
            nopicker = (NumberPicker) contentView.findViewById(R.id.yearPicker);

            btnBack = (ImageView) contentView.findViewById(R.id.btnBack);

            ImageView next = (ImageView) contentView.findViewById(R.id.btnNext);
            next.setOnClickListener(nextButtonClick());

            ImageView previous = (ImageView) contentView.findViewById(R.id.btnPrevious);
            previous.setOnClickListener(previousButtonClick());

            mPositiveButton = (TextView) contentView.findViewById(R.id.btnOk);
            mNegativeButton = (TextView) contentView.findViewById(R.id.btnCancel);

            monthAdapter = new MonthAdapter(context, this);

            recyclerView = (RecyclerView) contentView.findViewById(R.id.rvMonth);
            recyclerView.setLayoutManager(new GridLayoutManager(context, 4));
            recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(monthAdapter);

            Date date = new Date();
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);

            nopicker.setMinValue(year);
            nopicker.setMaxValue(year + 50);
            nopicker.setWrapSelectorWheel(false);
            nopicker.setValue(year);
            nopicker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

            setColorTheme(getColorByThemeAttr(context, android.R.attr.colorPrimary, R.color.colorPrimary));

            nopicker.setOnValueChangedListener((numberPicker, i, i2) -> {
                year = i2;
                mYear.setText(year + "");
                mTitleYear.setText("" + year);
            });
            btnBack.setOnClickListener(v -> {
                recyclerView.setVisibility(View.VISIBLE);
                nopicker.setVisibility(View.GONE);
                btnBack.setVisibility(View.GONE);
            });
        }

        private int getColorByThemeAttr(Context context, int attr, int defaultColor) {
            TypedValue typedValue = new TypedValue();
            Resources.Theme theme = context.getTheme();
            boolean got = theme.resolveAttribute(attr, typedValue, true);
            return got ? typedValue.data : defaultColor;
        }

        //set default config
        private void setDefault() {
            recyclerView.setVisibility(View.VISIBLE);
            nopicker.setVisibility(View.GONE);
            btnBack.setVisibility(View.GONE);

            Date date = new Date();

            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            year = cal.get(Calendar.YEAR);
            month = cal.get(Calendar.MONTH);

            monthAdapter.setSelectedItem(month);
            mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
            mTitleYear.setText("" + year);
            monthAdapter.notifyDataSetChanged();
            mYear.setText(year + "");
        }

        public void setLocale(Locale locale) {
            monthAdapter.setLocale(locale);
        }

        public void setSelectedMonth(int index) {
            monthAdapter.setSelectedItem(index);
            mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
            mTitleYear.setText("" + year);
        }

        public void setSelectedYear(int year) {
            this.year = year;
            mYear.setText(year + "");
            mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
            mTitleYear.setText("" + year);
        }

        public void setColorTheme(int color) {
            RelativeLayout linearToolbar = (RelativeLayout) contentView.findViewById(R.id.routToolbar);
            linearToolbar.setBackgroundColor(color);

            monthAdapter.setBackgroundMonth(color);
            mPositiveButton.setTextColor(color);
            mNegativeButton.setTextColor(color);
        }


        public void build() {
            monthAdapter.setSelectedItem(month);
            mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
            mTitleYear.setText("" + year);
            mYear.setText(year + "");

            mAlertDialog = alertBuilder.create();
            mAlertDialog.show();
            mAlertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            mAlertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_MASK_STATE);
            mAlertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            mAlertDialog.getWindow().setBackgroundDrawableResource(R.drawable.material_dialog_window);
            mAlertDialog.getWindow().setContentView(contentView);
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(mAlertDialog.getWindow().getAttributes());
            ScreenUtils screenUtils = new ScreenUtils(context);
            int width = (screenUtils.getWidth() * 85) / 100;
            mAlertDialog.getWindow().setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT);

        }

        public View.OnClickListener nextButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nopicker.getMaxValue() == year) {
                        return;
                    }
                    year++;
                    mYear.setText(year + "");
                    mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
                    mTitleYear.setText("" + year);
                    nopicker.setValue(year);
                }
            };
        }

        public View.OnClickListener previousButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (nopicker.getMinValue() == year) {
                        return;
                    }
                    year--;
                    mYear.setText(year + "");
                    mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
                    mTitleYear.setText("" + year);
                    nopicker.setValue(year);
                }
            };
        }

        public View.OnClickListener positiveButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String lbl = mTitleMonth.getText().toString() + ", " + mTitleYear.getText().toString();
                    dateMonthDialogListener.onDateMonth(
                            monthAdapter.getMonth(),
                            monthAdapter.getStartDate(),
                            monthAdapter.getEndDate(),
                            year, lbl);

                    mAlertDialog.dismiss();
                    if (App.appComponent.getDataManager().getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                        Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
                        context.getResources().getConfiguration().setLocale(localeStartDate);
                        Locale.setDefault(context.getResources().getConfiguration().locale);
                    } else {
                        Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ARABIC);
                        context.getResources().getConfiguration().setLocale(localeStartDate);
                        Locale.setDefault(context.getResources().getConfiguration().locale);
                    }
                }
            };
        }

        public View.OnClickListener negativeButtonClick() {
            return new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onCancelMonthDialogListener.onCancel(mAlertDialog);
                    if (App.appComponent.getDataManager().getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                        Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
                        context.getResources().getConfiguration().setLocale(localeStartDate);
                        Locale.setDefault(context.getResources().getConfiguration().locale);
                    } else {
                        Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ARABIC);
                        context.getResources().getConfiguration().setLocale(localeStartDate);
                        Locale.setDefault(context.getResources().getConfiguration().locale);
                    }
                }
            };
        }

        @Override
        public void onContentSelected() {
            recyclerView.setVisibility(View.GONE);
            nopicker.setVisibility(View.VISIBLE);
            btnBack.setVisibility(View.VISIBLE);

            nopicker.setValue(year);
            mTitleMonth.setText(String.format("%s, ", monthAdapter.getShortMonth()));
            mTitleYear.setText("" + year);
        }
    }

}