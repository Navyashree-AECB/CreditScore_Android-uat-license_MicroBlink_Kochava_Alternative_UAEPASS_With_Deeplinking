package com.aecb.ui.purchaseoptions.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.databinding.FragmentPurchaseOptionsBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.home.adapter.ProductsAdapter;
import com.aecb.ui.purchaseoptions.presenter.PurchaseOptionsContract;
import com.aecb.util.ItemOffsetDecoration;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.PRODUCT_UPGRADABLE_TO;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT_NUMBER;
import static com.aecb.util.varibles.StringConstants.SPACE;

public class PurchaseOptionsFragment extends BlurDialogBaseFragment<PurchaseOptionsContract.View,
        PurchaseOptionsContract.Presenter> implements PurchaseOptionsContract.View {

    @Inject
    public PurchaseOptionsContract.Presenter mPresenter;
    FragmentPurchaseOptionsBinding purchaseOptionsBinding;
    String selectedProductNumber, productUpgradableTo;
    private Context mContext;
    private ProductsAdapter productsAdapter;
    private LinearLayoutManager linearLayoutManager;
    private List<ProductsItem> productList = new ArrayList<>();
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback =
            new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                        dismiss();
                    }
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                }
            };

    public static PurchaseOptionsFragment newInstance(Bundle bundle) {
        PurchaseOptionsFragment fragment = new PurchaseOptionsFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_purchase_options;
    }

    @Override
    public PurchaseOptionsContract.Presenter createPresenter() {
        return mPresenter;
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if (getActivity() != null) mContext = getActivity();
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            selectedProductNumber = bundle.getString(SELECTED_PRODUCT_NUMBER);
            productUpgradableTo = bundle.getString(PRODUCT_UPGRADABLE_TO);
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        purchaseOptionsBinding = DataBindingUtil.bind(contentView);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams)
                ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(getActivity());
        mBehavior.setPeekHeight(screenUtils.getHeight());
        mBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
        purchaseOptionsBinding.rvProductList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        purchaseOptionsBinding.rvProductList.setLayoutManager(linearLayoutManager);
        purchaseOptionsBinding.rvProductList.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen._6sdp));
        productsAdapter = new ProductsAdapter(getActivity(), new ArrayList<>(), mPresenter.getAppLanguage(), false);
        purchaseOptionsBinding.rvProductList.setAdapter(productsAdapter);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getProductList();
    }


    @Override
    public void setProductList(List<ProductsItem> products, String currentLanguage) {
        Collections.sort(products, (lhs, rhs) -> Integer.valueOf(lhs.getOrder()).compareTo(rhs.getOrder()));
        for (ProductsItem productsItem : products) {
            if (productsItem.getProductNumber().equals(selectedProductNumber)) {
                String productTitleEn = productsItem.getTitleEn() + SPACE + getString(R.string.only);
                String productTitleAr = productsItem.getTitleAr() + SPACE + getString(R.string.only);
                productsItem.setTitleEn(productTitleEn);
                productsItem.setTitleAr(productTitleAr);
                this.productList.add(productsItem);
            }
            if (productUpgradableTo.equals(productsItem.getProductNumber())) {
                this.productList.add(productsItem);
            }
        }
        productsAdapter.setList(this.productList);
    }
}