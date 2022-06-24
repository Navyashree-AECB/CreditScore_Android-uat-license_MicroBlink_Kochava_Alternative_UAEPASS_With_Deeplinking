package com.aecb.ui.creditreport.creditreport.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;

import com.aecb.R;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.databinding.FragmentCreditReportBinding;
import com.aecb.presentation.blurcustomview.BlurConfig;
import com.aecb.presentation.blurcustomview.SmartAsyncPolicyHolder;
import com.aecb.ui.creditreport.CreditReportPagerAdapter;
import com.aecb.ui.creditreport.creditreport.presenter.CreditReportContract;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.tabs.TabLayout;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.API_CONFIGURATION;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;

public class CreditReportFragment extends BlurDialogBaseFragment<CreditReportContract.View,
        CreditReportContract.Presenter> implements CreditReportContract.View {

    @Inject
    public CreditReportContract.Presenter mPresenter;
    CreditReportPagerAdapter creditReportPagerAdapter;
    FragmentCreditReportBinding creditReportBinding;
    Bundle bundle;

    public static CreditReportFragment newInstance(Bundle bundle) {
        CreditReportFragment fragment = new CreditReportFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_credit_report;
    }

    @Override
    public CreditReportContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        creditReportBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        return creditReportBinding.getRoot();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getBundle();
    }


    private void getBundle() {
        bundle = this.getArguments();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NO_TITLE, R.style.CustomBottomSheetStyle);
        if (bundle != null) {
            if (bundle.getString(CALL_API_TYPE).equals(API_CONFIGURATION)) {
                creditReportBinding.tvText.setText(getString(R.string.credit_score));
            } else {
                mPresenter.getTitleFromProduct(bundle.getString(PRODUCT_ID));
            }
        }
        enableBinding();
    }

    private void enableBinding() {
        creditReportBinding.tabLayout.addTab(creditReportBinding.tabLayout.newTab().setText(getString(R.string.intro)));
        //TODO Remove faq and glossary for sometime
       // creditReportBinding.tabLayout.addTab(creditReportBinding.tabLayout.newTab().setText(getString(R.string.faqs)));
       // creditReportBinding.tabLayout.addTab(creditReportBinding.tabLayout.newTab().setText(getString(R.string.glossary)));
        creditReportBinding.tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        creditReportBinding.tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(creditReportBinding.viewPager));
        creditReportPagerAdapter = new CreditReportPagerAdapter(getChildFragmentManager(), creditReportBinding.tabLayout.getTabCount(), bundle);
        creditReportBinding.viewPager.setAdapter(creditReportPagerAdapter);
        creditReportBinding.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(creditReportBinding.tabLayout));
    }

    @NonNull
    protected BlurConfig blurConfig() {
        return new BlurConfig.Builder().overlayColor(R.color.blur_bg_color)
                .asyncPolicy(SmartAsyncPolicyHolder.INSTANCE.smartAsyncPolicy())
                .debug(true).build();
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        creditReportBinding = DataBindingUtil.bind(contentView);
        dialog.setContentView(contentView);
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) dialogInterface;
            setupFullHeight(bottomSheetDialog);
        });
    }

    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void setTitle(String title) {
        creditReportBinding.tvText.setText(title);
    }

    public void dismissDialog() {
        dismiss();
    }
}