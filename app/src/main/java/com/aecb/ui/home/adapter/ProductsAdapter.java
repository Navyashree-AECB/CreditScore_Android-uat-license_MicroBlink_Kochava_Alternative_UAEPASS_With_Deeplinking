package com.aecb.ui.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
import com.aecb.databinding.ItemProductBinding;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.purchaseoptions.view.PurchaseOptionsFragment;

import java.util.ArrayList;
import java.util.List;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.IntentKey.PRODUCT_UPGRADABLE_TO;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT_NUMBER;
import static com.aecb.AppConstants.ProductIds.D2C_PRODUCT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.AppConstants.doubleDecimalValue;
import static com.aecb.util.varibles.StringConstants.SPACE;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    String currentLanguage;
    PurchaseOptionsFragment purchaseOptionsFragment;
    boolean isHomeScreen;
    private BlurDialogBaseFragment creditReportFragment;
    private Context mContext;
    private ArrayList<ProductsItem> productList = new ArrayList<>();
    private long lastClickTime = 0;

    public ProductsAdapter(Context context, ArrayList<ProductsItem> productList, String currentLanguage,
                           boolean isHomeScreen) {
        this.mContext = context;
        this.productList = productList;
        this.currentLanguage = currentLanguage;
        this.isHomeScreen = isHomeScreen;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductsItem productsItem = productList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (currentLanguage.equals(ISO_CODE_ARABIC)) {
            if (isHomeScreen) {
                itemViewHolder.binding.tvProductTitle.setText(mContext.getString(R.string.get_your_first) + SPACE +
                        productsItem.getTitleAr());
            } else {
                itemViewHolder.binding.tvProductTitle.setText(productsItem.getTitleAr());
            }
            itemViewHolder.binding.tvProductDesc.setText(productsItem.getShortDescAr());
        } else {
            if (isHomeScreen) {
                itemViewHolder.binding.tvProductTitle.setText(mContext.getString(R.string.get_your_first) + SPACE +
                        productsItem.getTitleEn());
            } else {
                itemViewHolder.binding.tvProductTitle.setText(productsItem.getTitleEn());
            }
            itemViewHolder.binding.tvProductDesc.setText(productsItem.getShortdescEn());
        }
        double priceWithVAT = (productsItem.getPrice() * productsItem.getVat()) / 100;
        itemViewHolder.binding.tvProductPrice.setText(arabicToDecimal(doubleDecimalValue(2,
                (productsItem.getPrice() + priceWithVAT))));
        itemViewHolder.binding.llProduct.setOnClickListener(v -> {
            if (isHomeScreen && !productsItem.getId().equals(SCORE_WITH_REPORT_ID)) {
                Bundle b = new Bundle();
                b.putString(SELECTED_PRODUCT_NUMBER, productsItem.getProductNumber());
                b.putString(PRODUCT_UPGRADABLE_TO, productsItem.getUpgradeableTo());
                purchaseOptionsFragment = PurchaseOptionsFragment.newInstance(b);
                purchaseOptionsFragment.show(((DashboardActivity) mContext).getSupportFragmentManager(), purchaseOptionsFragment.getTag());
            } else {
                Intent intent = new Intent(mContext, CheckoutActivity.class);
                intent.putExtra(SELECTED_PRODUCT, productsItem);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        });
        itemViewHolder.binding.ivProductInfo.setOnClickListener(v -> {
            if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
            lastClickTime = SystemClock.elapsedRealtime();
            Bundle bundle = new Bundle();
            bundle.putString(CALL_API_TYPE, API_PRODUCTS);
            bundle.putString(PRODUCT_ID, productsItem.getId());
            bundle.putSerializable(SELECTED_PRODUCT, productsItem);
            creditReportFragment = CreditReportFragment.newInstance(bundle);
            creditReportFragment.show(((DashboardActivity) mContext).getSupportFragmentManager(), creditReportFragment.getTag());
        });
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void setList(List<ProductsItem> productList) {
        this.productList.clear();
        this.productList.addAll(productList);
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemProductBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}