package com.aecb.ui.home.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.R;
import com.aecb.data.api.models.home.ReadMoreItem;
import com.aecb.databinding.ItemProductBinding;
import com.aecb.databinding.ItemReadMoreBinding;
import com.aecb.databinding.ItemReadMoreBindingImpl;

import java.util.ArrayList;

public class ReadMoreAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mContext;
    private ArrayList<ReadMoreItem> readMoreList = new ArrayList<>();

    public ReadMoreAdapter(Context context, ArrayList<ReadMoreItem> readMoreList) {
        this.mContext = context;
        this.readMoreList = readMoreList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_read_more, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        onBindView(readMoreList.get(position), itemViewHolder, position);
    }

    @SuppressLint("NewApi")
    public void onBindView(ReadMoreItem item,
                           ItemViewHolder view, int position) {
        view.binding.tvProductTitle.setText(R.string.did_you_know);
        view.binding.tvProductDesc.setText(item.getReadMoreDesc());
        view.binding.tvProductPrice.setVisibility(View.GONE);
        view.binding.tvVat.setVisibility(View.GONE);
        view.binding.tvAED.setText(R.string.for_more_info);
        view.binding.tvAED.setTextColor(mContext.getResources().getColor(R.color.border_color));
        view.binding.tvAED.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setData(Uri.parse(item.getReadMoreLink()));
            mContext.startActivity(i);
        });
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemReadMoreBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}
