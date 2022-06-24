package com.aecb.ui.addrecipients.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.data.api.models.emailrecipients.EmailRecipients;
import com.aecb.databinding.ItemEmailRecipientsBinding;
import com.aecb.ui.addrecipients.view.AddRecipientsActivity;
import com.aecb.ui.updaterecipients.view.UpdateRecipientsFragment;

import java.util.ArrayList;

import static com.aecb.AppConstants.IntentKey.EMAIL_RECIPIENTS;

public class EmailRecipientsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    UpdateRecipientsFragment updateRecipientsFragment;
    private Context mContext;
    private ArrayList<EmailRecipients> emailList = new ArrayList<>();

    public EmailRecipientsListAdapter(Context context, ArrayList<EmailRecipients> emailList) {
        this.mContext = context;
        this.emailList = emailList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_email_recipients, viewGroup, false);
        return new ItemViewHolder(itemView);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        ItemViewHolder itemViewHolder = (ItemViewHolder) holder;
        EmailRecipients emailRecipients = emailList.get(position);
        itemViewHolder.binding.tvNameRecipient.setText(emailRecipients.getFullName());
        itemViewHolder.binding.ivDelete.setOnClickListener(v -> {
            ((AddRecipientsActivity) mContext).showDeleteConfirmation(position);
        });
        itemViewHolder.binding.ivEdit.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString(AppConstants.IntentKey.EMAIL, emailRecipients.getEmail());
            bundle.putString(AppConstants.IntentKey.FULL_NAME, emailRecipients.getFullName());
            bundle.putInt(AppConstants.IntentKey.POSITION, position);
            bundle.putSerializable(EMAIL_RECIPIENTS, emailList);
            updateRecipientsFragment = UpdateRecipientsFragment.newInstance(bundle);
            if (mContext instanceof AddRecipientsActivity) {
                updateRecipientsFragment.show(
                        ((AddRecipientsActivity) mContext).getSupportFragmentManager(),
                        updateRecipientsFragment.getTag());
            }
        });
    }

    public void deleteItem(int position) {
        emailList.remove(position);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return emailList.size();
    }

    @Override
    public void setHasStableIds(boolean hasStableIds) {
        super.setHasStableIds(hasStableIds);
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {
        ItemEmailRecipientsBinding binding;

        public ItemViewHolder(View itemView) {
            super(itemView);
            binding = DataBindingUtil.bind(itemView);
        }
    }

}