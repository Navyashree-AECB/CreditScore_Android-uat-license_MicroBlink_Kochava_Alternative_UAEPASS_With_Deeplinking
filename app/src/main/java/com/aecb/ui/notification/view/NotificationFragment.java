package com.aecb.ui.notification.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.data.api.models.notification.NotificationData;
import com.aecb.databinding.FragmentNotificationBinding;
import com.aecb.listeners.ReadNotification;
import com.aecb.presentation.recycleswipe.ItemTouchHelperCallback;
import com.aecb.presentation.recycleswipe.ItemTouchHelperExtension;
import com.aecb.ui.notification.Adapter.NotificationAdapter;
import com.aecb.ui.notification.Listner.OnDeleteListener;
import com.aecb.ui.notification.presenter.NotificationContract;

import java.util.List;

import javax.inject.Inject;

public class NotificationFragment extends BaseFragment<NotificationContract.View,
        NotificationContract.Presenter> implements NotificationContract.View, View.OnClickListener {

    @Inject
    public NotificationContract.Presenter mPresenter;
    FragmentNotificationBinding notificationBinding;
    private ItemTouchHelperExtension mItemTouchHelper;
    private ItemTouchHelperExtension.Callback mCallback;
    private int days = 0;
    private ReadNotification notificationListener = new ReadNotification() {
        @Override
        public void notificationRead(int id) {
            mPresenter.readNotification(id);
        }
    };
    private OnDeleteListener deleteListener = new OnDeleteListener() {
        @Override
        public void onDelete(int id) {
            mPresenter.deleteNotification(id);
        }
    };
    private NotificationAdapter notificationAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        notificationBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        View view = notificationBinding.getRoot();
        enableBinding();
        return view;
    }

    private void enableBinding() {
        notificationBinding.ivBackNotification.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        notificationBinding.rvNotification.setLayoutManager(linearLayoutManager);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(notificationBinding
                .rvNotification.getContext(), linearLayoutManager.getOrientation());
        notificationBinding.rvNotification.addItemDecoration(dividerItemDecoration);
        notificationBinding.txtClearNotification.setOnClickListener(this);
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_notification;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getNotificationData();
    }

    @Override
    public void loadNotification(List<NotificationData> notificationList) {
        if (notificationList.size() != 0) {
            mCallback = new ItemTouchHelperCallback();
            mItemTouchHelper = new ItemTouchHelperExtension(mCallback);
            mItemTouchHelper.attachToRecyclerView(notificationBinding.rvNotification);
            notificationAdapter = new NotificationAdapter(getContext(), notificationListener,
                    notificationList, deleteListener);
            notificationAdapter.setItemTouchHelperExtension(mItemTouchHelper);
            notificationBinding.rvNotification.setAdapter(notificationAdapter);
            notificationBinding.txtClearNotification.setVisibility(View.VISIBLE);
            notificationBinding.tvNoNotification.setVisibility(View.GONE);
            notificationBinding.rvNotification.setVisibility(View.VISIBLE);

        } else {
            notificationBinding.txtClearNotification.setVisibility(View.GONE);
            notificationBinding.tvNoNotification.setVisibility(View.VISIBLE);
            notificationBinding.rvNotification.setVisibility(View.GONE);
        }

    }

    @Override
    public NotificationContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBackNotification:
                backPressed();
                break;
            case R.id.txtClearNotification:
                if (notificationAdapter != null && notificationAdapter.getItemCount() != 0) {
                    userConfirmationDialog();
                } else {
                    Toast.makeText(getContext(), getActivity().getString(R.string.no_notification_to_delete), Toast.LENGTH_LONG).show();
                }
                break;
        }
    }

    private void userConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(R.string.message_delete_all_notification).setCancelable(false)
                .setPositiveButton(getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mPresenter.deleteAllNotification();
                        dialog.cancel();
                    }
                })
                .setNegativeButton(getString(R.string.no), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    public void backPressed() {
        assert getFragmentManager() != null;
        getFragmentManager().beginTransaction().remove(NotificationFragment.this).commitAllowingStateLoss();
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }
}
