package com.aecb.ui.productsmenu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.R;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.databinding.ItemProductMenuBinding;
import com.aecb.ui.checkout.view.CheckoutActivity;
import com.aecb.ui.dashboard.view.DashboardActivity;

import java.util.ArrayList;

import static android.view.Gravity.END;
import static android.view.Gravity.START;
import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.arabicToDecimal;
import static com.aecb.AppConstants.doubleDecimalValue;

public class ProductsMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    String currentLanguage;
    private Context mContext;
    private ArrayList<ProductsItem> productList = new ArrayList<>();

    public ProductsMenuAdapter(Context context, ArrayList<ProductsItem> productList,
                               String currentLanguage) {
        this.mContext = context;
        this.productList = productList;
        this.currentLanguage = currentLanguage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_product_menu,
                viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ProductsItem productsItem = productList.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        if (currentLanguage.equals(ISO_CODE_ARABIC)) {
            itemViewHolder.binding.tvProductTitle.setText(productsItem.getTitleAr());
            itemViewHolder.binding.ll.setGravity(END);
        } else {
            itemViewHolder.binding.tvProductTitle.setText(productsItem.getTitleEn());
            itemViewHolder.binding.ll.setGravity(START);
        }
        double priceWithVAT = (productsItem.getPrice() * productsItem.getVat()) / 100;
        itemViewHolder.binding.tvProductPrice.setText(arabicToDecimal(doubleDecimalValue(2,
                (productsItem.getPrice() + priceWithVAT))));
        itemViewHolder.binding.llProduct.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, CheckoutActivity.class);
            intent.putExtra(SELECTED_PRODUCT, productsItem);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(intent);
            ((DashboardActivity) mContext).hideProductsMenu();
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

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemProductMenuBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}