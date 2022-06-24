package com.aecb.presentation.monthpicker;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.R;
import com.aecb.util.Utilities;

import java.text.DateFormatSymbols;
import java.util.Calendar;
import java.util.Locale;

public class MonthAdapter extends RecyclerView.Adapter<MonthAdapter.MonthHolder> {

    private String[] months;
    private OnSelectedListener listener;
    private int selectedItem = -1;
    private Context context;
    private int color;

    public MonthAdapter(Context context, OnSelectedListener listener) {
        this.context = context;
        this.listener = listener;
        months = new DateFormatSymbols(Utilities.getCurrentLocal()).getShortMonths();
    }

    @Override
    public MonthHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MonthHolder monthHolder = new MonthHolder(LayoutInflater.from(context).inflate(R.layout.item_view_month, parent, false));
        return monthHolder;
    }

    @Override
    public void onBindViewHolder(MonthHolder holder, int position) {
        holder.textViewMonth.setText(months[position]);
        holder.itemView.setSelected(selectedItem == position);
    }

    @Override
    public int getItemCount() {
        return months.length;
    }

    /**
     * change format by localization
     *
     * @param locale
     */
    public void setLocale(Locale locale) {
        months = new DateFormatSymbols(locale).getShortMonths();
        notifyDataSetChanged();
    }

    public void setSelectedItem(int index) {
        if (index < 12 || index > -1) {
            selectedItem = index;
            notifyItemChanged(selectedItem);
        }
    }

    public void setBackgroundMonth(int color) {
        this.color = color;
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    public int getMonth() {
        return selectedItem + 1;
    }

    public int getStartDate() {
        return 1;
    }

    public int getEndDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.MONTH, selectedItem + 1);
        cal.set(Calendar.DAY_OF_MONTH, selectedItem + 1);
        int maxDay = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        return maxDay;
    }

    public String getShortMonth() {
        return months[selectedItem];
    }

    public interface OnSelectedListener {
        void onContentSelected();
    }

    class MonthHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout layoutMain;
        TextView textViewMonth;

        public MonthHolder(View itemView) {
            super(itemView);
            layoutMain = (LinearLayout) itemView.findViewById(R.id.main_layout);
            textViewMonth = (TextView) itemView.findViewById(R.id.text_month);
            if (color != 0)
                setMonthBackgroundSelected(color);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            selectedItem = getAdapterPosition();
            notifyDataSetChanged();
            listener.onContentSelected();
        }

        private void setMonthBackgroundSelected(int color) {
            LayerDrawable layerDrawable = (LayerDrawable) ContextCompat.getDrawable(context, R.drawable.month_selected);
            GradientDrawable gradientDrawable = (GradientDrawable) layerDrawable.getDrawable(1);
            gradientDrawable.setColor(color);
            layerDrawable.setDrawableByLayerId(1, gradientDrawable);

            StateListDrawable states = new StateListDrawable();
            states.addState(new int[]{android.R.attr.state_selected}, gradientDrawable);
            states.addState(new int[]{android.R.attr.state_pressed}, gradientDrawable);
            layoutMain.setBackground(states);
        }
    }
}