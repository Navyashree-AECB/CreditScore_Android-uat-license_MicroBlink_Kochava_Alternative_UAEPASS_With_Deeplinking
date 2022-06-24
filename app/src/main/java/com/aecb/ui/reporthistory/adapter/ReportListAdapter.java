package com.aecb.ui.reporthistory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.ItemCreditReportBinding;
import com.aecb.util.Utilities;

import java.util.ArrayList;
import java.util.List;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.ProductIds.REPORT_ID;
import static com.aecb.AppConstants.ProductIds.REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;

public class ReportListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private List<HistoryItem> reportList = new ArrayList<>();
    private OnClickListener listener;
    private String currentLang;

    public ReportListAdapter(Context context, String currentLang, List<HistoryItem> list, OnClickListener listener) {
        this.mContext = context;
        this.reportList = list;
        this.listener = listener;
        this.currentLang = currentLang;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_credit_report, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint({"SetTextI18n", "NewApi"})
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        onBindView(reportList.get(position), itemViewHolder, position);
    }

    @SuppressLint("NewApi")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void onBindView(HistoryItem item, ItemViewHolder view, int position) {
      //  if (item.isIsPdfAvailable()) {
            if (currentLang.equalsIgnoreCase(ISO_CODE_ARABIC)) {
                if (item.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                    view.binding.tvCreditReportTitle.setText(SCORE_WITH_REPORT_PRODUCT.getTitleAr());
                } else if (item.getReportTypeId().equals(SCORE_ID)) {
                    view.binding.tvCreditReportTitle.setText(SCORE_PRODUCT.getTitleAr());
                } else if (item.getReportTypeId().equals(REPORT_ID)) {
                    view.binding.tvCreditReportTitle.setText(REPORT_PRODUCT.getTitleAr());
                }
                for (ProductsItem productsItem : isDisplayFalseProduct) {
                    if (item.getReportTypeId().equals(productsItem.getId())) {
                        view.binding.tvCreditReportTitle.setText(productsItem.getTitleAr());
                    }
                }
            } else {
                if (item.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                    view.binding.tvCreditReportTitle.setText(SCORE_WITH_REPORT_PRODUCT.getTitleEn());
                } else if (item.getReportTypeId().equals(SCORE_ID)) {
                    view.binding.tvCreditReportTitle.setText(SCORE_PRODUCT.getTitleEn());
                } else if (item.getReportTypeId().equals(REPORT_ID)) {
                    view.binding.tvCreditReportTitle.setText(REPORT_PRODUCT.getTitleEn());
                }

                for (ProductsItem productsItem : isDisplayFalseProduct) {
                    if (item.getReportTypeId().equals(productsItem.getId())) {
                        view.binding.tvCreditReportTitle.setText(productsItem.getTitleEn());
                    }
                }
            }
            if (currentLang.equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                view.binding.llViewPDf.setBackground(mContext.getDrawable(R.drawable.bg_view_report_ar));
            } else {
                view.binding.llViewPDf.setBackground(mContext.getDrawable(R.drawable.bg_view_report));
            }
            view.binding.tvDate.setText(Utilities.convertServerDateToDisplayFormat(item.getCreatedon()));
            view.binding.llViewPDf.setOnClickListener(v -> listener.openPDF(item));
            view.binding.ivInfo.setOnClickListener(v -> listener.infoClick(item.getReportTypeId(), item));
     //   }

    }

    @Override
    public int getItemCount() {
        return reportList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void setList(List<HistoryItem> history) {
        this.reportList.clear();
        reportList.addAll(history);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void infoClick(String id, HistoryItem historyItem);

        void openPDF(HistoryItem item);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemCreditReportBinding binding;


        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}