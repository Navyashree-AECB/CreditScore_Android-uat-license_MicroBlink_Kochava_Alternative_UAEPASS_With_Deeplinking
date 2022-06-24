package com.aecb.presentation.recycleswipe;

import android.graphics.Canvas;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.ui.notification.Adapter.NotificationAdapter;

public class ItemTouchHelperCallback extends ItemTouchHelperExtension.Callback {

    @Override
    public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
        return makeMovementFlags(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.START);
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        if (recyclerView.getAdapter() instanceof NotificationAdapter) {
            NotificationAdapter adapter = (NotificationAdapter) recyclerView.getAdapter();
            adapter.move(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        }
        return true;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

    }

    @Override
    public boolean isLongPressDragEnabled() {
        return true;
    }

    @Override
    public void onChildDraw(Canvas c, RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder,
                            float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if (dY != 0 && dX == 0)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);

        NotificationAdapter.NotificationViewHolder notificationHolder = null;
        if (viewHolder instanceof NotificationAdapter.NotificationViewHolder) {
            notificationHolder = (NotificationAdapter.NotificationViewHolder) viewHolder;
        }
        if (viewHolder instanceof NotificationAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) {
            try {
                if (dX < -notificationHolder.mActionContainer.getWidth()) {
                    dX = -notificationHolder.mActionContainer.getWidth();
                }
                notificationHolder.mViewContent.setTranslationX(dX);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }
        if (viewHolder instanceof NotificationAdapter.NotificationViewHolder) {
            notificationHolder.mViewContent.setTranslationX(dX);
        }
        if (dY != 0 && dX == 0)
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
        if (viewHolder instanceof NotificationAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) {
            if (dX < -((NotificationAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) viewHolder).mActionContainer.getWidth()) {
                dX = -((NotificationAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) viewHolder).mActionContainer.getWidth();
            }
            ((NotificationAdapter.ItemSwipeWithActionWidthNoSpringViewHolder) viewHolder).mActionContainer.setTranslationX(dX);
            return;
        }
        if (viewHolder instanceof NotificationAdapter.NotificationViewHolder) {
            ((NotificationAdapter.NotificationViewHolder) viewHolder).mViewContent.setTranslationX(dX);
        }
    }
}