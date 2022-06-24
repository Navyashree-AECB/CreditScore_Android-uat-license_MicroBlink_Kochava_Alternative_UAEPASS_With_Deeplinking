package com.aecb.ui.notification.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.R;
import com.aecb.data.api.models.notification.NotificationData;
import com.aecb.databinding.NotificationRowViewBinding;
import com.aecb.listeners.ReadNotification;
import com.aecb.presentation.recycleswipe.Extension;
import com.aecb.presentation.recycleswipe.ItemTouchHelperExtension;
import com.aecb.ui.notification.Listner.OnDeleteListener;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder> {

    private Context mContext;
    private List<NotificationData> prepareNotificationData;
    private ReadNotification readNotification;
    private OnDeleteListener listener;
    private ItemTouchHelperExtension mItemTouchHelperExtension;

    public NotificationAdapter(Context mContext, ReadNotification readNotification,
                               List<NotificationData> prepareNotificationData, OnDeleteListener listener) {
        this.mContext = mContext;
        this.readNotification = readNotification;
        this.prepareNotificationData = prepareNotificationData;
        this.listener = listener;
    }

    public void setItemTouchHelperExtension(ItemTouchHelperExtension itemTouchHelperExtension) {
        mItemTouchHelperExtension = itemTouchHelperExtension;
    }

    public void move(int from, int to) {
        NotificationData prev = prepareNotificationData.remove(from);
        prepareNotificationData.add(to > from ? to - 1 : to, prev);
        notifyItemMoved(from, to);
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_row_view, parent, false);
        return new ItemSwipeWithActionWidthNoSpringViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {

        holder.notificationRowViewBinding.llNotification.setOnClickListener(v -> {
            readNotification.notificationRead(prepareNotificationData.get(position).getId());
            if (mItemTouchHelperExtension != null) {
                mItemTouchHelperExtension.closeOpened();
            }
        });
        if (holder instanceof ItemSwipeWithActionWidthViewHolder) {
            ItemSwipeWithActionWidthViewHolder viewHolder = (ItemSwipeWithActionWidthViewHolder) holder;
            viewHolder.mActionViewDelete.setOnClickListener(view -> listener.onDelete(prepareNotificationData.get(position).getId()));
        }
        if (prepareNotificationData.get(position).isRead()) {
            holder.notificationRowViewBinding.llNotification.setBackgroundColor(ContextCompat.getColor(mContext, R.color.white));
        }
        holder.notificationRowViewBinding.llNotification.setOnClickListener(v ->
                readNotification.notificationRead(prepareNotificationData.get(position).getId()));
        holder.notificationRowViewBinding.txtNotification.setText(
                prepareNotificationData.get(position).getNotificationBody());
        holder.notificationRowViewBinding.txtNotificationDate.setText(
                prepareNotificationData.get(position).getDate());
    }

    @Override
    public int getItemCount() {
        return prepareNotificationData.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder {
        public View mActionContainer;
        public View mViewContent;
        NotificationRowViewBinding notificationRowViewBinding;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            notificationRowViewBinding = DataBindingUtil.bind(itemView);
            mActionContainer = itemView.findViewById(R.id.loutDeleteView);
            mViewContent = itemView.findViewById(R.id.llNotification);
        }
    }

    public class ItemSwipeWithActionWidthNoSpringViewHolder extends ItemSwipeWithActionWidthViewHolder
            implements Extension {

        public ItemSwipeWithActionWidthNoSpringViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        public float getActionWidth() {
            try {
                return mActionContainer.getWidth();
            } catch (Exception e) {
                e.printStackTrace();
                return 0f;
            }
        }
    }

    public class ItemSwipeWithActionWidthViewHolder extends NotificationViewHolder implements Extension {

        View mActionViewDelete;

        public ItemSwipeWithActionWidthViewHolder(View itemView) {
            super(itemView);
            mActionViewDelete = itemView.findViewById(R.id.imageDelete);
        }

        @Override
        public float getActionWidth() {
            return mActionContainer.getWidth();
        }
    }

}