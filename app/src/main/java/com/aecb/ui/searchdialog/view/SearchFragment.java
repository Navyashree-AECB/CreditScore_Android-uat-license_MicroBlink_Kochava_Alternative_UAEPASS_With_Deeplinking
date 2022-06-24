package com.aecb.ui.searchdialog.view;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseBottomSheetFragment;
import com.aecb.data.api.models.nationalities.NationalitiesItem;
import com.aecb.data.api.models.request.updateuserprofile.UpdateUserProfileRequest;
import com.aecb.data.db.repository.usertcversion.DBUserTC;
import com.aecb.databinding.FragmentSearchBinding;
import com.aecb.ui.profile.view.ProfileActivity;
import com.aecb.ui.registerflow.personaldetails.view.PersonalDetailsActivity;
import com.aecb.ui.searchdialog.adapters.NationalitySearchAdapter;
import com.aecb.ui.searchdialog.presenter.SearchContract;
import com.aecb.util.ScreenUtils;
import com.aecb.util.ToastUtil;
import com.aecb.util.ValidationUtil;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.aecb.AppConstants.IntentKey.EDIT_NATIONALITY;
import static com.aecb.util.FirebaseLogging.editedNationalitySaved;

public class SearchFragment extends BaseBottomSheetFragment implements SearchContract.View {

    @Inject
    public SearchContract.Presenter mPresenter;
    UpdateUserProfileRequest updateUserProfileRequest;
    private FragmentSearchBinding fragmentSearchBinding;
    private NationalitySearchAdapter nationalitySearchAdapter;
    private List<NationalitiesItem> nationalitiesList;
    private Context mContext;
    private DBUserTC dbUserTC;
    private BottomSheetBehavior.BottomSheetCallback mBottomSheetBehaviorCallback = new
            BottomSheetBehavior.BottomSheetCallback() {

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

    /**
     * instantiate bottom_line new fragment
     *
     * @return fragment
     */
    public static SearchFragment newInstance(Bundle bundle) {
        SearchFragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * get resource file of this fragment
     */
    public int getLayoutRes() {
        return R.layout.fragment_search;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.CustomBottomSheetDialogTheme);
        if (getActivity() != null) {
            mContext = getActivity();
        }
    }

    private void displayNationality() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false);
        fragmentSearchBinding.rvEmiratesData.setLayoutManager(linearLayoutManager);
        if (nationalitiesList != null && !nationalitiesList.isEmpty()) {
            nationalitySearchAdapter = new NationalitySearchAdapter(nationalitiesList, SearchFragment.this, mPresenter.getAppLanguage());
            fragmentSearchBinding.rvEmiratesData.setAdapter(nationalitySearchAdapter);
        }
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), getLayoutRes(), null);
        fragmentSearchBinding = DataBindingUtil.bind(contentView);
        setScreenDirection(dialog);
        fragmentSearchBinding.rvEmiratesData.setHasFixedSize(true);
        fragmentSearchBinding.edtEmirates.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nationalitySearchAdapter != null) {
                    nationalitySearchAdapter.filter(s.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        handleScreens();
        fragmentSearchBinding.imageCancelSearch.setOnClickListener(v -> fragmentSearchBinding.edtEmirates.setText(""));
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        BottomSheetBehavior mBehavior = BottomSheetBehavior.from((View) contentView.getParent());
        ScreenUtils screenUtils = new ScreenUtils(mContext);
        mBehavior.setPeekHeight(screenUtils.getHeight());
        mBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }
    }

    private void handleScreens() {
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            if (mContext instanceof PersonalDetailsActivity) {
                nationalitiesList = (ArrayList<NationalitiesItem>) bundle.
                        getSerializable(AppConstants.IntentKey.NATIONALITY_LIST);
                displayNationality();
            } else if (mContext instanceof ProfileActivity) {
                nationalitiesList = (ArrayList<NationalitiesItem>) bundle.getSerializable(AppConstants.IntentKey.NATIONALITY_LIST);
                dbUserTC = new Gson().fromJson(bundle.getString(EDIT_NATIONALITY), DBUserTC.class);
                displayNationality();
            }
        }
    }

    /**
     * this method use for set dialog layout direction(LTR and RTL)
     *
     * @param dialog
     */
    public void setScreenDirection(Dialog dialog) {
        if (dialog.getWindow() != null) {
            String mAppLang = mPresenter.getAppLanguage();
            if (!ValidationUtil.isNullOrEmpty(mAppLang)) {
                if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ENG)) {
                    dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
                } else if (mAppLang.equalsIgnoreCase(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                    dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
                }
            } else {
                dialog.getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
            }
        }
    }

    public void selectNationalityItem(NationalitiesItem nationalitiesItem) {
        if (mContext instanceof PersonalDetailsActivity) {
            this.dismiss();
            ((PersonalDetailsActivity) mContext).setSelectedNationality(nationalitiesItem);
        } else {
            editedNationalitySaved();
            updateUserProfileRequest = new UpdateUserProfileRequest();
            updateUserProfileRequest.setChannel(2);
            updateUserProfileRequest.setUpdateType(6);
            dbUserTC.setNationalityId(nationalitiesItem.getNationalityId());
            updateUserProfileRequest.setNationalityId(nationalitiesItem.getNationalityId());
            mPresenter.updateUserDetail(updateUserProfileRequest, dbUserTC, this);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        this.mContext = null;
    }

    @Override
    public void showMessage(String message) {
        ToastUtil.toast(getContext(), message);
    }

    @Override
    public void openProfileDetail(String id, String message, String status, String useCase) {
        this.dismiss();
        ((ProfileActivity) mContext).setSelectedNationality(id);
        ((ProfileActivity) mContext).showProfileUpdatedMessage(message, status, useCase);
        ((ProfileActivity) mContext).mPresenter.getLastUserDetail();
    }

}