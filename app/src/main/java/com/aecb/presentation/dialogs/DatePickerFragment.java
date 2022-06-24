package com.aecb.presentation.dialogs;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.aecb.App;
import com.aecb.AppConstants;
import com.aecb.listeners.OnDateSetListener;
import com.aecb.util.Utilities;
import com.aecb.util.ValidationUtil;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import timber.log.Timber;

/**
 * TODO : must implement OnDateSetListener into your activity
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    private OnDateSetListener onDateSetCallback;

    public DatePickerFragment() {

    }

    /**
     * @param minDate
     * @param maxDate
     * @param date(dd-MM-yyyy) = date want to show to picker view (if you pass 20-04-2017 then date picker show 20 April into date picker)
     * @param identifier       = if multiple date picker in same screen
     * @return
     */

    //TODO : If you pass date parameter then must pass date format
    public static DatePickerFragment newInstance(long minDate, long maxDate, String date, String dateFormat, String identifier, String currentDate) {
        Bundle args = new Bundle();
        args.putString(AppConstants.IntentKey.DATE, date);
        args.putLong(AppConstants.IntentKey.MIN_DATE, minDate);
        args.putLong(AppConstants.IntentKey.MAX_DATE, maxDate);
        args.putString(AppConstants.IntentKey.DATE_FORMAT, dateFormat);
        args.putString(AppConstants.IntentKey.DATE_PICKER_IDENTIFIER, identifier);
        args.putString(AppConstants.IntentKey.CURRENT_DATE, currentDate);
        DatePickerFragment fragment = new DatePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity) {
            try {
                onDateSetCallback = (OnDateSetListener) context;
            } catch (ClassCastException e) {
                throw new ClassCastException(context.toString()
                        + " must implement MyInterface ");
            }
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AppConstants.setLocalLanguageEng(getContext());
        if (bundle != null) {
            String date = bundle.getString(AppConstants.IntentKey.DATE, "");
            String dateFormat = bundle.getString(AppConstants.IntentKey.DATE_FORMAT, "");
            String currentDate = bundle.getString(AppConstants.IntentKey.CURRENT_DATE, "");
            long minDate = bundle.getLong(AppConstants.IntentKey.MIN_DATE, 0);
            long maxDate = bundle.getLong(AppConstants.IntentKey.MAX_DATE, 0);
            String identifier = bundle.getString(AppConstants.IntentKey.DATE_PICKER_IDENTIFIER, "");

            Calendar c = Calendar.getInstance(Locale.ENGLISH);
            if (!ValidationUtil.isNullOrEmpty(date) && !ValidationUtil.isNullOrEmpty(dateFormat)) {
                try {
                    c.setTime(Utilities.stringToDate(date, dateFormat));
                } catch (Exception e) {
                    Timber.e(DatePickerFragment.this.getClass().getSimpleName(), "error in onCreateDialog(): " + e.getMessage());
                }
            }
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
            dialog.getDatePicker().setTag(identifier);
            if (!ValidationUtil.isNullOrEmpty(currentDate)) {
                dialog.updateDate(Integer.parseInt(currentDate), new Date().getMonth() + 1, new Date().getDate());
            }
            // if (StringHelper.isEmpty(date)){
            if (minDate != 0) {
                dialog.getDatePicker().setMinDate(minDate);
            }

            if (maxDate != 0) {
                dialog.getDatePicker().setMaxDate(maxDate);
            }
            //}
            return dialog;

        }

        return super.onCreateDialog(savedInstanceState);
    }

    /**
     * TODO : If multiple date picker in screen then manage using identifier parameter
     */
    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        AppConstants.setLocalLanguageEng(getContext());
        onDateSetCallback.onDateSet(datePicker, year, month, day);
    }

    @Override
    public void dismiss() {
        if (App.appComponent.getDataManager().getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ENG)) {
            Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ENG);
            getResources().getConfiguration().setLocale(localeStartDate);
            Locale.setDefault(getResources().getConfiguration().locale);
        } else {
            Locale localeStartDate = new Locale(AppConstants.AppLanguage.ISO_CODE_ARABIC);
            getResources().getConfiguration().setLocale(localeStartDate);
            Locale.setDefault(getResources().getConfiguration().locale);
        }
        super.dismiss();

    }
}