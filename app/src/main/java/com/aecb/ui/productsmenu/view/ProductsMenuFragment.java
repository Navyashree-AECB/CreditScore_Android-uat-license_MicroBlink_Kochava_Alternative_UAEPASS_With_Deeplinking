package com.aecb.ui.productsmenu.view;

import android.app.Dialog;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.databinding.FragmentProductsMenuBinding;
import com.aecb.presentation.spacenavigation.SpaceItem;
import com.aecb.presentation.spacenavigation.SpaceOnClickListener;
import com.aecb.ui.productsmenu.adapter.ProductsMenuAdapter;
import com.aecb.ui.productsmenu.presenter.ProductsMenuContract;
import com.aecb.util.ItemOffsetDecoration;
import com.aecb.util.ScreenUtils;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

public class ProductsMenuFragment extends BlurDialogBaseFragment<ProductsMenuContract.View,
        ProductsMenuContract.Presenter> implements ProductsMenuContract.View, SpaceOnClickListener {

    @Inject
    public ProductsMenuContract.Presenter mPresenter;
    FragmentProductsMenuBinding productsMenuBinding;
    private ProductsMenuAdapter productsMenuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ProductsItem> productList = new ArrayList<>();
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

    public static ProductsMenuFragment newInstance(Bundle bundle) {
        ProductsMenuFragment fragment = new ProductsMenuFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        Bundle bundle = this.getArguments();
        if (bundle != null) {
        }
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        productsMenuBinding = DataBindingUtil.bind(contentView);
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
        productsMenuBinding.spaceNavigationView.addSpaceItem(new SpaceItem("Home", R.drawable.ic_home));
        productsMenuBinding.spaceNavigationView.addSpaceItem(new SpaceItem("History", R.drawable.ic_history));
        productsMenuBinding.spaceNavigationView.addSpaceItem(new SpaceItem("Help", R.drawable.ic_help_support));
        productsMenuBinding.spaceNavigationView.addSpaceItem(new SpaceItem("Menu", R.drawable.ic_nav_menu));
        productsMenuBinding.spaceNavigationView.showIconOnly();
        productsMenuBinding.spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);
        productsMenuBinding.spaceNavigationView.setCentreButtonRippleColor(ContextCompat.getColor(getActivity(), R.color.transparent));
        productsMenuBinding.spaceNavigationView.setSpaceOnClickListener(this);
        productsMenuBinding.rvProductList.setHasFixedSize(true);
        linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        productsMenuBinding.rvProductList.setLayoutManager(linearLayoutManager);
        productsMenuBinding.rvProductList.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen._6sdp));
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.getProductList();
    }

    @Override
    public ProductsMenuContract.Presenter createPresenter() {
        return mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_products_menu;
    }

    @Override
    public void onCentreButtonClick() {
        dismiss();
    }

    @Override
    public void onItemClick(int itemIndex, String itemName) {

    }

    @Override
    public void onItemReselected(int itemIndex, String itemName) {

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setProductList(List<ProductsItem> products, String currentLanguage) {
        Collections.sort(products, (lhs, rhs) -> Integer.valueOf(lhs.getOrder()).compareTo(rhs.getOrder()));
        productList.addAll(products);
        productsMenuAdapter = new ProductsMenuAdapter(getActivity(), this.productList, currentLanguage);
        productsMenuBinding.rvProductList.setAdapter(productsMenuAdapter);
    }
}