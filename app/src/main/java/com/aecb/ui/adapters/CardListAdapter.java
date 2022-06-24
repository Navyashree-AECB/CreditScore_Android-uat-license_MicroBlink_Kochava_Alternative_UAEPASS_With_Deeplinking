package com.aecb.ui.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.data.api.models.cards.CreditCardData;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.ItemCardListBinding;
import com.aecb.ui.purchasejourney.cardlist.view.CardListActivity;

import java.util.ArrayList;
import java.util.List;

import static com.aecb.AppConstants.CARD_PREFIX;
import static com.aecb.AppConstants.CardTypes.CIRRUS;
import static com.aecb.AppConstants.CardTypes.MASTER_CARD;
import static com.aecb.AppConstants.CardTypes.VISA;
import static com.aecb.AppConstants.PaymentMethods.E_DIRHAM;
import static com.aecb.AppConstants.PaymentMethods.PAYMENT_GW;

public class CardListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<CreditCardData> cardDataList = new ArrayList<>();
    public int selectedPosition = -1;
    private boolean showThreeDots = false;
    DBUserTC dbUserTC;
    public boolean borderForFirstTimeEDirham = true;
    public boolean borderForFirstTimeNI = true;
    ItemViewHolder itemViewHolder;

    public CardListAdapter(Context context, List<CreditCardData> list, boolean showThreeDots) {
        this.mContext = context;
        this.cardDataList = list;
        this.showThreeDots = showThreeDots;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_card_list, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        this.itemViewHolder = itemViewHolder;
        onBind(cardDataList.get(position), itemViewHolder, position);
    }

    private void onBind(CreditCardData item, ItemViewHolder view, int position) {
        if (showThreeDots) {
            view.binding.ivThreeDots.setVisibility(View.VISIBLE);
        }

        switch (item.getCardType()) {
            case MASTER_CARD:
                view.binding.ivCardLogo.setImageResource(R.drawable.ic_master_card);
                break;
            case VISA:
                view.binding.ivCardLogo.setImageResource(R.drawable.ic_visa);
                break;
            case CIRRUS:
                view.binding.ivCardLogo.setImageResource(R.drawable.ic_cirrus);
                break;
        }

        if (item.getPymtCardIsDefault().equalsIgnoreCase(AppConstants.YES)) {
            if (dbUserTC != null) {
                if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(E_DIRHAM)) {
                    view.binding.tvCardIsDefault.setVisibility(View.INVISIBLE);
                } else if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(PAYMENT_GW)) {
                    view.binding.tvCardIsDefault.setVisibility(View.VISIBLE);
                } else {
                    view.binding.tvCardIsDefault.setVisibility(View.VISIBLE);
                }
            }
            if (selectedPosition == -1) {
                selectedPosition = position;
                if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(PAYMENT_GW)) {
                    view.binding.cvCardList.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_card_backgraound));
                }
            }
        } else {
            view.binding.tvCardIsDefault.setVisibility(View.INVISIBLE);
        }
        view.binding.tvCardName.setText(item.getCardholdername());
        String last4NumberOfCard = item.getCardnumber().substring(item.getCardnumber().length() - 4);
        view.binding.tvCardNumber.setText(CARD_PREFIX + last4NumberOfCard);
        view.binding.ivThreeDots.setOnClickListener(v -> ((CardListActivity) mContext).openCardUpdateDeleteView(item));

        if (selectedPosition == position) {
            // do your stuff here like
            //Change selected item background color and Show sub item views
            view.binding.cvCardList.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_card_backgraound));
            ((CardListActivity) mContext).setSelectedCard(item);
        } else {
            // do your stuff here like
            //Change  unselected item background color and Hide sub item views
            view.binding.cvCardList.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_card_backgraound_white));
        }
        if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(E_DIRHAM)) {
            if (borderForFirstTimeEDirham) {
                view.binding.cvCardList.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_card_backgraound_white));
            }
        }

        if (dbUserTC.getPreferredPaymentMethod().equalsIgnoreCase(PAYMENT_GW)) {
            if (!borderForFirstTimeNI) {
                view.binding.cvCardList.setBackground(ContextCompat.getDrawable(mContext, R.drawable.selected_card_backgraound_white));
            }
        }

        view.binding.cvCardList.setOnClickListener(v -> {
            borderForFirstTimeEDirham = false;
            borderForFirstTimeNI = true;
            ((CardListActivity) mContext).disSelectEdirham();
            if (selectedPosition == position) {
                selectedPosition = -1;
                notifyDataSetChanged();
                return;
            }
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return cardDataList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public void setList(List<CreditCardData> cardList, DBUserTC dbUserTC) {
        cardDataList.clear();
        cardDataList.addAll(cardList);
        this.dbUserTC = dbUserTC;
        notifyDataSetChanged();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemCardListBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}