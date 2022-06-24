package com.aecb.ui.searchdialog.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.R;
import com.aecb.data.api.models.nationalities.NationalitiesItem;
import com.aecb.databinding.ItemSearchBinding;
import com.aecb.ui.searchdialog.view.SearchFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;

public class NationalitySearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<NationalitiesItem> nationalitiesItems = new ArrayList<>();
    private List<NationalitiesItem> orgnationalitiesItems = new ArrayList<>();
    private SearchFragment searchFragment;
    private String currentLanguage;

    public NationalitySearchAdapter(List<NationalitiesItem> list, SearchFragment searchFragment,
                                    String currentLanguage) {
        this.nationalitiesItems.addAll(list);
        this.orgnationalitiesItems.addAll(list);
        this.searchFragment = searchFragment;
        this.currentLanguage = currentLanguage;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_search,
                viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        NationalitiesItem nationalitiesItem = nationalitiesItems.get(position);
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        itemViewHolder.setNationalityData(nationalitiesItem);
        itemViewHolder.binding.routParent.setOnClickListener(v -> {
            if (searchFragment != null) {
                searchFragment.selectNationalityItem(nationalitiesItems.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return nationalitiesItems.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        nationalitiesItems.clear();
        if (charText.length() == 0) {
            nationalitiesItems.addAll(orgnationalitiesItems);
        } else {
            for (NationalitiesItem wp : orgnationalitiesItems) {
                try {
                    if (String.valueOf(wp.getCountryName()).toLowerCase(Locale.getDefault()).contains(charText)) {
                        nationalitiesItems.add(wp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    //FirebaseCrashlytics.getInstance().recordException(e);
                    //Crashlytics.logException(e);
                }
            }
        }
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemSearchBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }

        public void setNationalityData(NationalitiesItem nationalitiesItem) {
            if (currentLanguage.equals(ISO_CODE_ENG)) {
                binding.tvSearchBank.setText(nationalitiesItem.getNameEn());
            } else {
                binding.tvSearchBank.setText(nationalitiesItem.getNameAr());
            }
        }
    }

}