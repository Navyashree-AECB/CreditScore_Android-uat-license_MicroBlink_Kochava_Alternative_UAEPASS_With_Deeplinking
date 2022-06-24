package com.aecb.ui.dashboard.view;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.R;
import com.aecb.base.mvp.BaseActivity;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.ActivityDashboardBinding;
import com.aecb.presentation.spacenavigation.SpaceOnClickListener;
import com.aecb.ui.dashboard.presenter.DashboardContract;
import com.aecb.ui.downloadpdf.view.DownloadPdfFragment;
import com.aecb.ui.helpandsupport.view.HelpSupportFragment;
import com.aecb.ui.history.view.HistoryFragment;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.ui.menu.view.MenuActivity;
import com.aecb.ui.notification.view.NotificationFragment;
import com.aecb.ui.productsmenu.adapter.ProductsMenuAdapter;
import com.aecb.ui.productsmenu.view.ProductsMenuFragment;
import com.aecb.util.ItemOffsetDecoration;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.FIREBASE_TOPIC;
import static com.aecb.AppConstants.IntentKey.HISTORY_ITEM;
import static com.aecb.AppConstants.IntentKey.OPERATION_TYPE;
import static com.aecb.AppConstants.IntentKey.SHOW_SCORE_LOADING;
import static com.aecb.util.FirebaseLogging.visitedHelpSupport;

public class DashboardActivity extends BaseActivity<DashboardContract.View, DashboardContract.Presenter>
        implements DashboardContract.View, SpaceOnClickListener, View.OnClickListener {

    public static int scenario;
    public static int notificationCount = 0;
    public static int currentTabPosition = 0;
    @Inject
    public DashboardContract.Presenter mPresenter;
    public boolean showScoreLoadingScreen = false;
    ActivityDashboardBinding dashboardBinding;
    ProductsMenuFragment productsMenuFragment;
    boolean doubleBackToExitPressedOnce = false;
    private ProductsMenuAdapter productsMenuAdapter;
    private LinearLayoutManager linearLayoutManager;
    private ArrayList<ProductsItem> productList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseMessaging.getInstance().subscribeToTopic(FIREBASE_TOPIC);
        enableBinding();
        scenario = getIntent().getIntExtra("KEY", 0);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(SHOW_SCORE_LOADING)) {
                showScoreLoadingScreen = bundle.getBoolean(SHOW_SCORE_LOADING);
            }
        }
        if (showScoreLoadingScreen) loadScreens(new HomeFragment(), 0);
        mPresenter.getProducts(showScoreLoadingScreen);
        mPresenter.updateLanguageIfRequired();
    }


    private void enableBinding() {
        dashboardBinding = DataBindingUtil.setContentView(this, getLayoutRes());
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        dashboardBinding.rvProductList.setLayoutManager(linearLayoutManager);
        dashboardBinding.rvProductList.addItemDecoration(new ItemOffsetDecoration(this, R.dimen._6sdp));
        dashboardBinding.btnHome.setOnClickListener(this);
        dashboardBinding.btnHistory.setOnClickListener(this);
        dashboardBinding.btnShowProducts.setOnClickListener(this);
        dashboardBinding.btnClose.setOnClickListener(this);
        dashboardBinding.btnHelpSupport.setOnClickListener(this);
        dashboardBinding.btnNavMenu.setOnClickListener(this);
        dashboardBinding.parentView.setOnClickListener(this);
    }

    private void showProductsMenu() {
        dashboardBinding.rvProductList.setVisibility(View.VISIBLE);
        dashboardBinding.rvProductList.post(() -> {
            dashboardBinding.flScreensContainer.animate()
                    .translationY(-(dashboardBinding.rvProductList.getMeasuredHeight()))
                    .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        });
        dashboardBinding.btnHome.setEnabled(false);
        dashboardBinding.btnHistory.setEnabled(false);
        dashboardBinding.btnHelpSupport.setEnabled(false);
        dashboardBinding.btnNavMenu.setEnabled(false);
        dashboardBinding.btnShowProducts.setVisibility(View.GONE);
        dashboardBinding.btnClose.setVisibility(View.VISIBLE);
        dashboardBinding.btnClose.setEnabled(true);
        dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
        dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history));
        dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support));
        dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
    }

    private void changeBottomIconColor() {
        switch (currentTabPosition) {
            case 0:
                dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home_selected));
                dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history));
                dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support));
                dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
                dashboardBinding.bottomSheet.setBackgroundColor(getResources().getColor(R.color.app_bg_color));
                break;
            case 1:
                dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
                dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support));
                dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
                dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history_selected));
                dashboardBinding.bottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                break;
            case 2:
                dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
                dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history));
                dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
                dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support_selected));
                dashboardBinding.bottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
                break;
        }
    }

    public void hideProductsMenu() {
        changeBottomIconColor();
        dashboardBinding.flScreensContainer.animate()
                .translationY(0)
                .setDuration(getResources().getInteger(android.R.integer.config_mediumAnimTime));
        dashboardBinding.btnHome.setEnabled(true);
        dashboardBinding.btnHistory.setEnabled(true);
        dashboardBinding.btnHelpSupport.setEnabled(true);
        dashboardBinding.btnNavMenu.setEnabled(true);
        dashboardBinding.btnClose.setVisibility(View.GONE);
        dashboardBinding.btnShowProducts.setVisibility(View.VISIBLE);
        dashboardBinding.rvProductList.setVisibility(View.GONE);
    }

    @NonNull
    @Override
    public DashboardContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void initToolbar() {

    }

    public void loadScreens(Fragment newFragment, int currentTabPosition) {
        DashboardActivity.currentTabPosition = currentTabPosition;
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.flScreensContainer, newFragment);
        transaction.commit();
        changeBottomIconColor();
    }

    @Override
    public void onCentreButtonClick() {
        Bundle b = new Bundle();
        productsMenuFragment = ProductsMenuFragment.newInstance(b);
        productsMenuFragment.show(getSupportFragmentManager(), productsMenuFragment.getTag());
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
        productList.clear();
        productList.addAll(products);
        productsMenuAdapter = new ProductsMenuAdapter(this, this.productList, currentLanguage);
        dashboardBinding.rvProductList.setAdapter(productsMenuAdapter);
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void refreshHomeFragment() {
        try {
            loadScreens(new HomeFragment(), 0);
           /* Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.flScreensContainer);
            if (currentFragment instanceof HomeFragment) {
                FragmentTransaction fragTransaction = getSupportFragmentManager().beginTransaction();
                fragTransaction.detach(currentFragment);
                fragTransaction.attach(currentFragment);
                fragTransaction.commit();
            }*/
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
    }

    public void openDownloadFragment(HistoryItem item) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(HISTORY_ITEM, item);
        dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
        dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history_selected));
        dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support));
        dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
        dashboardBinding.bottomSheet.setBackgroundColor(getResources().getColor(R.color.white));
        loadScreens(DownloadPdfFragment.newInstance(bundle), 3);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnHome:
                if (currentTabPosition != 0) {
                    dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home_selected));
                    dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history));
                    dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support));
                    dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
                    loadScreens(new HomeFragment(), 0);
                }
                break;
            case R.id.btnHistory:
                if (currentTabPosition != 1) {
                    dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
                    dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history_selected));
                    dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support));
                    dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
                    loadScreens(new HistoryFragment(), 1);
                }
                break;
            case R.id.btnHelpSupport:
                if (currentTabPosition != 2) {
                    visitedHelpSupport();
                    dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
                    dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history));
                    dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support_selected));
                    dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
                    loadScreens(new HelpSupportFragment(), 2);
                }
                break;

            case R.id.btnShowProducts:
                showProductsMenu();
                break;
            case R.id.btnNavMenu:
                moveActivity(this, MenuActivity.class, false, false);
                break;
            case R.id.btnClose:
            case R.id.parentView:
                hideProductsMenu();
                break;
        }
    }

    public void openHelpSupport(int type) {
        Bundle bundle = new Bundle();
        bundle.putInt(OPERATION_TYPE, type);
        dashboardBinding.btnHome.setBackground(getResources().getDrawable(R.drawable.ic_home));
        dashboardBinding.btnHistory.setBackground(getResources().getDrawable(R.drawable.ic_history));
        dashboardBinding.btnHelpSupport.setBackground(getResources().getDrawable(R.drawable.ic_help_support_selected));
        dashboardBinding.btnNavMenu.setBackground(getResources().getDrawable(R.drawable.ic_nav_menu));
        loadScreens(HelpSupportFragment.newInstance(bundle), 2);
    }

    @Override
    public void showUnreadNotification(int count) {
        notificationCount = count;
    }

    @Override
    public void onBackPressed() {
        if (currentTabPosition == 0) {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, getString(R.string.back_press_warning), Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
        } else {
            if (currentTabPosition == 4) {
                try {
                    NotificationFragment notificationFragment = (NotificationFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.flScreensContainer);
                    notificationFragment.backPressed();
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }

            } else {
                loadScreens(new HomeFragment(), 0);
            }
        }
    }

    public void hideBottomSheet() {
        dashboardBinding.bottomSheet.setVisibility(View.GONE);
    }

    public void showBottomSheet() {
        dashboardBinding.bottomSheet.setVisibility(View.VISIBLE);
    }
}