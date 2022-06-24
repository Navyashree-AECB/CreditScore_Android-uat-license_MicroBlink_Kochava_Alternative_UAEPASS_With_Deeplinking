package com.aecb.ui.reporthistory.view;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentReportHistoryBinding;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.reporthistory.adapter.ReportListAdapter;
import com.aecb.ui.reporthistory.presenter.ReportHistoryContract;
import com.aecb.util.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;

public class ReportHistoryFragment extends BaseFragment<ReportHistoryContract.View,
        ReportHistoryContract.Presenter> implements ReportHistoryContract.View {

    @Inject
    public ReportHistoryContract.Presenter mPresenter;
    List<ProductsItem> allProductList = new ArrayList<>();
    ProductsItem selectedProduct;
    private FragmentReportHistoryBinding reportHistoryBinding;
    private ReportListAdapter reportListAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<HistoryItem> reportList = new ArrayList<>();
    private BlurDialogBaseFragment creditReportFragment;
    private long lastClickTime = 0;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_report_history;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().runOnUiThread(() -> mPresenter.getProductHistory());
    }

    @Override
    public ReportHistoryContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        reportHistoryBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = reportHistoryBinding.getRoot();
        enableBinding();
        return view;
    }


    private void enableBinding() {
        initRecyclerView();
    }


    public void findProductFromList(String reportTypeId) {
        for (ProductsItem productsItem : allProductList) {
            if (reportTypeId.equals(productsItem.getId())) {
                selectedProduct = productsItem;
            }
        }
    }

    private void initRecyclerView() {
        reportHistoryBinding.rvReportList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        reportHistoryBinding.rvReportList.setLayoutManager(linearLayoutManager);


        reportListAdapter = new ReportListAdapter(getActivity(), mPresenter.getCurrentLanguage(), reportList, new ReportListAdapter.OnClickListener() {
            @Override
            public void infoClick(String id, HistoryItem historyItem) {
                findProductFromList(historyItem.getReportTypeId());
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                openCreditScoreScreen(id);
            }

            @Override
            public void openPDF(HistoryItem item) {
                if (item != null && item.getReportId() != null) {
                    ((DashboardActivity) getContext()).openDownloadFragment(item);
                }
            }
        });
        reportHistoryBinding.rvReportList.addItemDecoration(new ItemOffsetDecoration(Objects.requireNonNull(getActivity()), R.dimen._6sdp));
        reportHistoryBinding.rvReportList.setAdapter(reportListAdapter);
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void setPurchaseList(List<HistoryItem> history, List<ProductsItem> productsItemList,
                                String currentLanguage) {
        if (history.isEmpty()) {
            reportHistoryBinding.tvNoPurchaseFound.setVisibility(View.VISIBLE);
            reportHistoryBinding.rvReportList.setVisibility(View.GONE);
        } else {
            reportHistoryBinding.tvNoPurchaseFound.setVisibility(View.GONE);
            reportHistoryBinding.rvReportList.setVisibility(View.VISIBLE);
            reportListAdapter.setList(history);
            allProductList.clear();
            allProductList.addAll(productsItemList);
        }
    }

    private void openCreditScoreScreen(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(CALL_API_TYPE, API_PRODUCTS);
        bundle.putString(PRODUCT_ID, id);
        bundle.putSerializable(SELECTED_PRODUCT, selectedProduct);
        creditReportFragment = CreditReportFragment.newInstance(bundle);
        creditReportFragment.show(getFragmentManager(), creditReportFragment.getTag());
    }

}