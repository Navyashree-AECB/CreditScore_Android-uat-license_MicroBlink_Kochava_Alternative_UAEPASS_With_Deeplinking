package com.aecb.ui.scorehistory.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.ItemScoreHistoryBinding;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.aecb.AppConstants.CREDIT_REPORT_DASHBOARD_FORMAT;
import static com.aecb.AppConstants.DateFormats.DEFAULT_API_FORMAT;
import static com.aecb.AppConstants.IntentKey.API_CONFIGURATION;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.ScoreLimits.HIGH;
import static com.aecb.AppConstants.ScoreLimits.LOW;
import static com.aecb.AppConstants.ScoreLimits.MEDIUM;
import static com.aecb.AppConstants.ScoreLimits.VERY_HIGH;
import static com.aecb.AppConstants.ScoreLimits.VERY_LOW;
import static com.aecb.util.Utilities.stringToDate;

public class ScoreListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<ProductsItem> allProductList = new ArrayList<>();
    ProductsItem selectedProduct;
    private Context mContext;
    private List<HistoryItem> scoreList = new ArrayList<>();
    private BlurDialogBaseFragment creditReportFragment;
    private OnClickListener onClickListener;
    private long lastClickTime = 0;
    private int DEFAULT_SCORE = 300;

    public ScoreListAdapter(Context context, List<HistoryItem> scoreList, OnClickListener onClickListener,
                            List<ProductsItem> allProductList) {
        this.mContext = context;
        this.scoreList = scoreList;
        this.onClickListener = onClickListener;
        this.allProductList = allProductList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_score_history, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        onBindView(scoreList.get(position), itemViewHolder, position);
    }

    @SuppressLint("NewApi")
    public void onBindView(HistoryItem historyItem, ItemViewHolder view, int position) {
        if (historyItem.isNoScore()) {
            view.binding.ivNoCreditScoreAndReport.setVisibility(View.GONE);
            view.binding.llNoScore.setVisibility(View.VISIBLE);
            view.binding.rlCreditScore.setVisibility(View.GONE);
            Date createdDate = stringToDate(historyItem.getCreatedon(), DEFAULT_API_FORMAT);
            view.binding.tvCreditScoreDate.setText(CREDIT_REPORT_DASHBOARD_FORMAT.format(createdDate));
            /*if(historyItem.isAlternativeScore()) {
                view.binding.tvAlternativeData.setVisibility(View.VISIBLE);
                view.binding.tvNonCreditData.setVisibility(View.VISIBLE);
            }*/
            view.binding.ivInfoNoScore.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                findProductFromList(historyItem.getReportTypeId());
                Bundle bundle = new Bundle();
                bundle.putString(CALL_API_TYPE, API_CONFIGURATION);
                bundle.putSerializable(SELECTED_PRODUCT, selectedProduct);
                creditReportFragment = CreditReportFragment.newInstance(bundle);
                creditReportFragment.show(((DashboardActivity) mContext).getSupportFragmentManager(),
                        creditReportFragment.getTag());
            });
        } else {
            view.binding.ivNoCreditScoreAndReport.setVisibility(View.GONE);
            view.binding.llNoScore.setVisibility(View.GONE);
            view.binding.rlCreditScore.setVisibility(View.VISIBLE);
            view.binding.tvCreditScore.setTypeface(Typeface.createFromAsset(((DashboardActivity) mContext).getAssets(),
                    "font/Roboto-Light.ttf"));
            view.binding.tvCreditScore.setText(String.valueOf(historyItem.getScore()));
            Date createdDate = stringToDate(historyItem.getCreatedon(), DEFAULT_API_FORMAT);
            view.binding.tvCreditScoreDate.setText(CREDIT_REPORT_DASHBOARD_FORMAT.format(createdDate));
            /*if(historyItem.isAlternativeScore()) {
                view.binding.tvAlternativeData.setVisibility(View.VISIBLE);
                view.binding.tvNonCreditData.setVisibility(View.VISIBLE);
            }*/
            view.binding.creditScoreArc.setProgress(historyItem.getScore() - 300);
            displayStarAndProgressColor(historyItem.getScore(), view);
            view.binding.creditScoreArc.setEnabled(true);
            view.binding.creditScoreArc.setClickable(false);
            view.binding.creditScoreArc.setFocusable(false);
            view.binding.creditScoreArc.setOnTouchListener((v, event) -> true);
            view.binding.starView.setClickable(false);
            view.binding.starView.setFocusable(false);
            view.binding.starView.setOnTouchListener((v, event) -> true);
            view.binding.ivInfo.setOnClickListener(v -> {
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                findProductFromList(historyItem.getReportTypeId());
                Bundle bundle = new Bundle();
                bundle.putString(CALL_API_TYPE, API_CONFIGURATION);
                bundle.putSerializable(SELECTED_PRODUCT, selectedProduct);
                creditReportFragment = CreditReportFragment.newInstance(bundle);
                creditReportFragment.show(((DashboardActivity) mContext).getSupportFragmentManager(),
                        creditReportFragment.getTag());
            });
        }
    }


    public void findProductFromList(String reportTypeId) {
        for (ProductsItem productsItem : allProductList) {
            if (reportTypeId.equals(productsItem.getId())) {
                selectedProduct = productsItem;
            }
        }

    }

    private void displayStarAndProgressColor(int score, ItemViewHolder view) {
        if (score <= VERY_LOW) {
            displayStars(1, view);
            view.binding.creditScoreArc.setProgressColor(mContext.getResources().getColor(R.color.red_progress_color));
        } else if (score <= LOW) {
            displayStars(2, view);
            view.binding.creditScoreArc.setProgressColor(mContext.getResources().getColor(R.color.orange_progress_color));
        } else if (score <= MEDIUM) {
            displayStars(3, view);
            view.binding.creditScoreArc.setProgressColor(mContext.getResources().getColor(R.color.yellow_progress_color));
        } else if (score <= HIGH) {
            displayStars(4, view);
            view.binding.creditScoreArc.setProgressColor(mContext.getResources().getColor(R.color.light_green_progress_color));
        } else if (score <= VERY_HIGH) {
            displayStars(5, view);
            view.binding.creditScoreArc.setProgressColor(mContext.getResources().getColor(R.color.green_progress_color));
        }
    }

    private void displayStars(int rating, ItemViewHolder view) {
        if (rating == 1) {
            view.binding.starView.setRating(rating);
        } else if (rating == 2) {
            view.binding.starView.setRating(rating);
        } else if (rating == 3) {
            view.binding.starView.setRating(rating);
        } else if (rating == 4) {
            view.binding.starView.setRating(rating);
        } else if (rating == 5) {
            view.binding.starView.setRating(rating);
        }
    }

    @Override
    public int getItemCount() {
        return scoreList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void setList(List<HistoryItem> history) {
        this.scoreList.clear();
        scoreList.addAll(history);
        notifyDataSetChanged();
    }

    public interface OnClickListener {
        void onClick();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemScoreHistoryBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}