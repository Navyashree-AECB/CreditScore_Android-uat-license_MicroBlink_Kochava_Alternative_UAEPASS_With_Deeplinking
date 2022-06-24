package com.aecb.ui.home.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.home.ReadMoreItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentHomeBinding;
import com.aecb.presentation.recycleswipe.SnapHelper;
import com.aecb.ui.creditreport.creditreport.view.CreditReportFragment;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.home.adapter.ProductsAdapter;
import com.aecb.ui.home.adapter.ReadMoreAdapter;
import com.aecb.ui.home.presenter.HomeContract;
import com.aecb.ui.notification.view.NotificationFragment;
import com.aecb.util.ItemOffsetDecoration;
import com.aecb.util.Utilities;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.load.resource.gif.GifDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.stetho.server.http.ExactPathMatcher;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ARABIC;
import static com.aecb.AppConstants.DISPLAY_DATE_FORMAT;
import static com.aecb.AppConstants.DateFormats.DEFAULT_API_FORMAT;
import static com.aecb.AppConstants.IntentKey.API_CONFIGURATION;
import static com.aecb.AppConstants.IntentKey.API_PRODUCTS;
import static com.aecb.AppConstants.IntentKey.CALL_API_TYPE;
import static com.aecb.AppConstants.IntentKey.PRODUCT_ID;
import static com.aecb.AppConstants.IntentKey.SELECTED_PRODUCT;
import static com.aecb.AppConstants.ProductIds.REPORT_ID;
import static com.aecb.AppConstants.ProductIds.REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_PRODUCT;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_PRODUCT;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;
import static com.aecb.AppConstants.ReadMoreUrls.readMoreLinkOneAr;
import static com.aecb.AppConstants.ReadMoreUrls.readMoreLinkOneEn;
import static com.aecb.AppConstants.ReadMoreUrls.readMoreLinkThreeAr;
import static com.aecb.AppConstants.ReadMoreUrls.readMoreLinkThreeEn;
import static com.aecb.AppConstants.ReadMoreUrls.readMoreLinkTwoEn;
import static com.aecb.AppConstants.ReadMoreUrls.readMoreLinkTwoEnAr;
import static com.aecb.AppConstants.ScoreLimits.HIGH;
import static com.aecb.AppConstants.ScoreLimits.LOW;
import static com.aecb.AppConstants.ScoreLimits.MEDIUM;
import static com.aecb.AppConstants.ScoreLimits.VERY_HIGH;
import static com.aecb.AppConstants.ScoreLimits.VERY_LOW;
import static com.aecb.ui.dashboard.view.DashboardActivity.notificationCount;
import static com.aecb.util.Utilities.stringToDate;

public class HomeFragment extends BaseFragment<HomeContract.View, HomeContract.Presenter> implements
        HomeContract.View, View.OnClickListener {

    public static Context context;

    @Inject
    public HomeContract.Presenter mPresenter;
    public boolean showScoreLoadingScreen = false;
    FragmentHomeBinding homeBinding;
    List<ProductsItem> allProductList = new ArrayList<>();
    ProductsItem selectedCreditScore, selectedCreditReport;
    private BlurDialogBaseFragment creditReportFragment;
    private ProductsAdapter productsAdapter;
    private ReadMoreAdapter readMoreAdapter;
    private LinearLayoutManager readMoreLinearLayoutManager;
    private LinearLayoutManager linearLayoutManager;
    private HistoryItem reportHistoryItem, scoreHistoryItem;
    private String currentLanguage;
    private long lastClickTime = 0;
    private int currentPosition = -1;
    private int currentPositionForTimer = -1;
    private int currentPositionForFirstTimeUser = -1;
    private Timer timer;
    private Timer timerForFirstTimeUser;
    private Timer loadingTimerForIncreasing;
    private Timer loadingTimerForDecreasing;
    private int DEFAULT_SCORE = 302;
    private int MAXIMUM_SCORE = 903;
    private boolean loopFirstTime = true;
    private ImageView[] ivArrayDots;
    private Handler loopAgainHandler = new Handler();
    private ArrayList<ReadMoreItem> readMoreList = new ArrayList<>();


    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        homeBinding = DataBindingUtil.inflate(
                inflater, getLayoutRes(), container, false);
        View view = homeBinding.getRoot();
        enableBinding();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (reportHistoryItem != null) {
            findSelectedCreditReportFromList(reportHistoryItem.getReportTypeId());
            /*if (reportHistoryItem.isAlternativeScore()) {
                homeBinding.tvAlternativeData.setVisibility(View.VISIBLE);
                homeBinding.tvNonCreditData.setVisibility(View.VISIBLE);
            }*/
        }
        if (scoreHistoryItem != null) {
            findSelectedCreditScoreFromList(scoreHistoryItem.getReportTypeId());
            /*if (reportHistoryItem.isAlternativeScore()) {
                homeBinding.tvAlternativeData.setVisibility(View.VISIBLE);
                homeBinding.tvNonCreditData.setVisibility(View.VISIBLE);
            }*/
        }
    }

    private void enableBinding() {
        try {
            context = getActivity();
            setupReadMoreIndicatorDots();
            homeBinding.rvProductList.setHasFixedSize(true);
            homeBinding.rvProductList.setNestedScrollingEnabled(false);
            linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
            homeBinding.rvProductList.setLayoutManager(linearLayoutManager);
            homeBinding.rvProductList.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen._6sdp));
            productsAdapter = new ProductsAdapter(getActivity(), new ArrayList<>(), mPresenter.getAppLanguage(), true);
            homeBinding.rvProductList.setAdapter(productsAdapter);
            homeBinding.ivInfo.setOnClickListener(this);
            homeBinding.ivInfoNoScore.setOnClickListener(this);
            homeBinding.ivReportInfo.setOnClickListener(this);
            if (mPresenter.getAppLanguage().equals(ISO_CODE_ARABIC)) {
                homeBinding.llReport.setBackground(getResources().getDrawable(R.drawable.bg_view_report_ar));
            } else {
                homeBinding.llReport.setBackground(getResources().getDrawable(R.drawable.bg_view_report));
            }

            homeBinding.llReport.setOnClickListener(this);
            homeBinding.loadingCreditScoreArc.setEnabled(true);
            homeBinding.loadingCreditScoreArc.setClickable(false);
            homeBinding.loadingCreditScoreArc.setFocusable(false);
            homeBinding.loadingCreditScoreArc.setProgressColor(getResources().getColor(R.color.transparent));
            homeBinding.loadingCreditScoreArc.setOnTouchListener((v, event) -> true);
            homeBinding.creditScoreArcFirstUser.setEnabled(true);
            homeBinding.creditScoreArcFirstUser.setClickable(false);
            homeBinding.creditScoreArcFirstUser.setFocusable(false);
            homeBinding.creditScoreArcFirstUser.setArcColor(getResources().getColor(R.color.transparent));
            homeBinding.creditScoreArcFirstUser.setOnTouchListener((v, event) -> true);
            homeBinding.tvCreditScore.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                    "font/Roboto-Light.ttf"));
            homeBinding.loadingTvCreditScore.setTypeface(Typeface.createFromAsset(getActivity().getAssets(),
                    "font/Roboto-Light.ttf"));
            if (notificationCount == 0) {
                homeBinding.ivHomeFragmentNotification.setImageResource(R.drawable.ic_notification);
            }
            homeBinding.ivHomeFragmentNotification.setOnClickListener(this);
            showScoreLoadingScreen = ((DashboardActivity) Objects.requireNonNull(getActivity())).showScoreLoadingScreen;
            if (showScoreLoadingScreen) {
                homeBinding.mainLayout.setVisibility(View.GONE);
                homeBinding.loadingLayout.setVisibility(View.VISIBLE);
                showLoadingAnimationForIncreasing();
                showStarLoadingAfterDelay();
                startLoopAgain(9000);
                showFirstMessage();
                showSecondMessage();
                showThirdMessage();
                callPurchaseApi();
                setDefaultScore();
                setDefaultScoreAgain();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setupScoreProgress(HistoryItem historyItem) {
        displayDashboardDetails();
        scoreHistoryItem = historyItem;
        findSelectedCreditScoreFromList(scoreHistoryItem.getReportTypeId());
        if (historyItem.isNoScore()) {
            Timber.e("Visibility Called: : " + " noScore Called");
            homeBinding.rlCreditScore.setVisibility(View.GONE);
            homeBinding.rlCreditScoreFirstUSer.setVisibility(View.GONE);
            homeBinding.llNoScore.setVisibility(View.VISIBLE);
            Date createdDate = stringToDate(historyItem.getCreatedon(), DEFAULT_API_FORMAT);
            homeBinding.tvCreditScoreDate.setText(DISPLAY_DATE_FORMAT.format(createdDate));
        } else {
            homeBinding.rlCreditScoreFirstUSer.setVisibility(View.GONE);
            homeBinding.rlCreditScore.setVisibility(View.VISIBLE);
            homeBinding.llNoScore.setVisibility(View.GONE);
            if (historyItem.getScore() == DEFAULT_SCORE) {
                animateScoreForDefaultScore(historyItem);
            } else {
                animateScore(historyItem);
            }
            displayStars(historyItem.getScore());
            homeBinding.creditScoreArc.setEnabled(true);
            homeBinding.creditScoreArc.setClickable(false);
            homeBinding.creditScoreArc.setFocusable(false);
            homeBinding.creditScoreArc.setOnTouchListener((v, event) -> true);
            homeBinding.tvCreditScore.setText(String.valueOf(historyItem.getScore()));
            Date createdDate = stringToDate(historyItem.getCreatedon(), DEFAULT_API_FORMAT);
            homeBinding.tvCreditScoreDate.setText(DISPLAY_DATE_FORMAT.format(createdDate));
        }
    }

    private void setUpReadMoreView() {
        try {
            ivArrayDots[0].setImageResource(R.drawable.ic_read_more_selected_dot);
            homeBinding.rvReadItems.setHasFixedSize(true);
            if (currentLanguage.equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
                readMoreLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return true;
                    }
                };
                readMoreLinearLayoutManager.setStackFromEnd(true);
            } else {
                readMoreLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
                    @Override
                    public boolean canScrollHorizontally() {
                        return true;
                    }
                };
            }
            homeBinding.rvReadItems.setLayoutManager(readMoreLinearLayoutManager);
            try {
                LinearSnapHelper linearSnapHelper = new SnapHelper();
                linearSnapHelper.attachToRecyclerView(homeBinding.rvReadItems);
                readMoreAdapter = new ReadMoreAdapter(getActivity(), readMoreList);
                homeBinding.rvReadItems.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen._6sdp));
                homeBinding.rvReadItems.setAdapter(readMoreAdapter);
            } catch (Exception e) {
                Timber.d("Exception : " + e.toString());
            }
            homeBinding.rvReadItems.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    for (int i = 0; i < ivArrayDots.length; i++) {
                        ivArrayDots[i].setImageResource(R.drawable.ic_read_more_unselected_dot);
                    }
                    ivArrayDots[readMoreLinearLayoutManager.findFirstVisibleItemPosition()].setImageResource(R.drawable.ic_read_more_selected_dot);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addReadMoreData() {
        if (currentLanguage.equalsIgnoreCase(ISO_CODE_ARABIC)) {
            ReadMoreItem readMoreItem1 = new ReadMoreItem(getString(R.string.read_more_desc_one), readMoreLinkOneAr);
            readMoreList.add(readMoreItem1);
            ReadMoreItem readMoreItem2 = new ReadMoreItem(getString(R.string.read_more_desc_two), readMoreLinkTwoEnAr);
            readMoreList.add(readMoreItem2);
            ReadMoreItem readMoreItem3 = new ReadMoreItem(getString(R.string.read_more_desc_three), readMoreLinkThreeAr);
            readMoreList.add(readMoreItem3);
        } else {
            ReadMoreItem readMoreItem1 = new ReadMoreItem(getString(R.string.read_more_desc_one), readMoreLinkOneEn);
            readMoreList.add(readMoreItem1);
            ReadMoreItem readMoreItem2 = new ReadMoreItem(getString(R.string.read_more_desc_two), readMoreLinkTwoEn);
            readMoreList.add(readMoreItem2);
            ReadMoreItem readMoreItem3 = new ReadMoreItem(getString(R.string.read_more_desc_three), readMoreLinkThreeEn);
            readMoreList.add(readMoreItem3);
        }
    }

    private void animateScore(HistoryItem historyItem) {
        try {
            timer = new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    currentPosition = currentPosition + 1;
                    if (currentPosition <= historyItem.getScore() - DEFAULT_SCORE) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            try {
                                homeBinding.creditScoreArc.setProgress(currentPosition);
                                homeBinding.tvCreditScore.setText(String.valueOf(currentPosition + DEFAULT_SCORE));
                                displayStarAndProgressColor(currentPosition + DEFAULT_SCORE);
                            } catch (Exception e) {
                                Timber.d("Exception : " + e.toString());
                            }

                        });
                    } else {
                        if (timer != null) {
                            timer.cancel();
                            timer.purge();
                        }
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            homeBinding.tvCreditScore.setText(String.valueOf(historyItem.getScore()));
                            displayStarAndProgressColor(historyItem.getScore());
                        });
                    }
                }
            }, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void animateScoreForDefaultScore(HistoryItem historyItem) {
        try{
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                currentPosition = currentPosition + 1;
                if (currentPosition != historyItem.getScore() - 280) {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        homeBinding.creditScoreArc.setProgress(currentPosition);
                        homeBinding.tvCreditScore.setText(String.valueOf(historyItem.getScore()));
                        displayStarAndProgressColor(currentPosition + DEFAULT_SCORE);
                    });
                } else {
                    if (getActivity() == null) return;
                    getActivity().runOnUiThread(() -> {
                        homeBinding.tvCreditScore.setText(String.valueOf(historyItem.getScore()));
                    });
                    if (timer != null) {
                        timer.cancel();
                        timer.purge();
                    }
                }
            }
        }, 0, 50);
    }catch(Exception e){
         e.printStackTrace();
        }
    }

    @Override
    public void displayNoCreditScoreAndReportView() {
        Timber.d("Visibility Called: :" + "displayNoCreditScoreAndReportView");
        homeBinding.rlCreditScore.setVisibility(View.GONE);
        homeBinding.tvCreditScoreDate.setVisibility(View.GONE);
        homeBinding.rlCreditScoreFirstUSer.setVisibility(View.VISIBLE);
        homeBinding.llProductOptions.setVisibility(View.VISIBLE);
        homeBinding.llCreditReport.setVisibility(View.GONE);
        homeBinding.llReadView.setVisibility(View.GONE);
        showLoadingAnimationForFirstTimeUser();
        homeBinding.line.setVisibility(View.GONE);
        homeBinding.tvCreditScoreFirstTimeUser.setVisibility(View.VISIBLE);
        homeBinding.tvCreditScoreFirstTimeUser.setCharacterDelay(40);
        homeBinding.tvCreditScoreFirstTimeUser.animateText(getString(R.string.get_you_credit_score_now));
    }


    @Override
    public void displayNoReportWithCreditScoreView(List<ProductsItem> productList) {
        Timber.e("Visibility Called: : " + " displayNoReportWithCreditScoreView");
        homeBinding.rlCreditScore.setVisibility(View.VISIBLE);
        homeBinding.rlCreditScoreFirstUSer.setVisibility(View.GONE);
        homeBinding.llProductOptions.setVisibility(View.VISIBLE);
        homeBinding.llCreditReport.setVisibility(View.VISIBLE);
        homeBinding.llReadView.setVisibility(View.GONE);
        List<ProductsItem> items = new ArrayList<>();
        Collections.sort(productList, (lhs, rhs) -> Integer.valueOf(lhs.getOrder()).compareTo(rhs.getOrder()));
        for (ProductsItem productsItem : productList) {
            if (productsItem.isIsdisplay() && productsItem.getOrder() == 1) {
                items.add(productsItem);
            }
        }
        productsAdapter.setList(items);
    }


    @Override
    public void displayNoScoreWithReportView(HistoryItem historyItem, List<ProductsItem> productList) {
        homeBinding.rlCreditScore.setVisibility(View.GONE);
        Timber.e("Visibility Called: : " + " displayNoScoreWithReportView");
        homeBinding.rlCreditScoreFirstUSer.setVisibility(View.VISIBLE);
        homeBinding.llProductOptions.setVisibility(View.VISIBLE);
        homeBinding.llCreditReport.setVisibility(View.VISIBLE);
        homeBinding.llReadView.setVisibility(View.GONE);
        showLoadingAnimationForFirstTimeUser();
        homeBinding.tvCreditScoreFirstTimeUser.setVisibility(View.VISIBLE);
        homeBinding.tvCreditScoreFirstTimeUser.setCharacterDelay(20);
        homeBinding.tvCreditScoreFirstTimeUser.animateText(getString(R.string.get_you_credit_score_now));
        displayReportDetails(historyItem);
        List<ProductsItem> items = new ArrayList<>();
        Collections.sort(productList, (lhs, rhs) -> Integer.valueOf(lhs.getOrder()).compareTo(rhs.getOrder()));
        for (ProductsItem productsItem : productList) {
            if (productsItem.isIsdisplay() && productsItem.getOrder() == 3) {
                items.add(productsItem);
            }
        }
        productsAdapter.setList(items);
    }

    @Override
    public void displayScoreWithReportView(HistoryItem historyItem) {
        setUpReadMoreView();
        homeBinding.llReadView.setVisibility(View.VISIBLE);
        homeBinding.rlCreditScore.setVisibility(View.VISIBLE);
        Timber.e("Visibility Called: : " + " displayScoreWithReportView");
        homeBinding.rlCreditScoreFirstUSer.setVisibility(View.GONE);
        homeBinding.llProductOptions.setVisibility(View.GONE);
        homeBinding.llCreditReport.setVisibility(View.VISIBLE);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) homeBinding.llCreditReport.getLayoutParams();
        params.topMargin = 50;
        homeBinding.llCreditReport.setLayoutParams(params);
        if (historyItem.isNoScore()) {
            homeBinding.rlCreditScore.setVisibility(View.GONE);
            homeBinding.rlCreditScoreFirstUSer.setVisibility(View.GONE);
            homeBinding.llNoScore.setVisibility(View.VISIBLE);
        }
    }

    @SuppressLint("SetTextI18n")
    public void displayReportDetails(HistoryItem historyItem) {
        //Navya crashlytics
        if (historyItem != null) {
            reportHistoryItem = historyItem;
            if (currentLanguage.equalsIgnoreCase(ISO_CODE_ARABIC)) {
                if (historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                    homeBinding.tvCreditReportTitle.setText(SCORE_WITH_REPORT_PRODUCT.getTitleAr());
                } else if (historyItem.getReportTypeId().equals(SCORE_ID)) {
                    homeBinding.tvCreditReportTitle.setText(SCORE_PRODUCT.getTitleAr());
                } else if (historyItem.getReportTypeId().equals(REPORT_ID)) {
                    homeBinding.tvCreditReportTitle.setText(REPORT_PRODUCT.getTitleAr());
                }
                for (ProductsItem productsItem : isDisplayFalseProduct) {
                    if (historyItem.getReportTypeId().equals(productsItem.getId())) {
                        homeBinding.tvCreditReportTitle.setText(productsItem.getTitleAr());
                    }
                }
            } else {
                if (historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                    homeBinding.tvCreditReportTitle.setText(SCORE_WITH_REPORT_PRODUCT.getTitleEn());
                } else if (historyItem.getReportTypeId().equals(SCORE_ID)) {
                    homeBinding.tvCreditReportTitle.setText(SCORE_PRODUCT.getTitleEn());
                } else if (historyItem.getReportTypeId().equals(REPORT_ID)) {
                    homeBinding.tvCreditReportTitle.setText(REPORT_PRODUCT.getTitleEn());
                }
                for (ProductsItem productsItem : isDisplayFalseProduct) {
                    if (historyItem.getReportTypeId().equals(productsItem.getId())) {
                        homeBinding.tvCreditReportTitle.setText(productsItem.getTitleEn());
                    }
                }
            }

            homeBinding.tvDate.setText(Utilities.convertServerDateToDisplayFormat(historyItem.getCreatedon()));
            findSelectedCreditReportFromList(reportHistoryItem.getReportTypeId());
        }
    }

    private void displayStarAndProgressColor(int score) {
        try {
            if (getActivity() == null) return;
            if (score <= VERY_LOW) {
                homeBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.red_progress_color));
            } else if (score <= LOW) {
                homeBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.orange_progress_color));
            } else if (score <= MEDIUM) {
                homeBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.yellow_progress_color));
            } else if (score <= HIGH) {
                homeBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.light_green_progress_color));
            } else if (score <= VERY_HIGH) {
                homeBinding.creditScoreArc.setProgressColor(getResources().getColor(R.color.green_progress_color));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void displayStars(int score) {
        if (score <= VERY_LOW) {
            displayStarWithAnimation(R.drawable.star_one);
        } else if (score <= LOW) {
            displayStarWithAnimation(R.drawable.star_two);
        } else if (score <= MEDIUM) {
            displayStarWithAnimation(R.drawable.star_three);
        } else if (score <= HIGH) {
            displayStarWithAnimation(R.drawable.star_four);
        } else if (score <= VERY_HIGH) {
            displayStarWithAnimation(R.drawable.star_five);
        }
    }

    private void displayStarWithAnimation(int starGif) {
        try {
            Glide.with(this).asGif().load(starGif).listener(new RequestListener<GifDrawable>() {
                @Override
                public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                    resource.setLoopCount(1);
                    return false;
                }
            }).into(homeBinding.starView);
        }catch (Exception e) {
            e.printStackTrace();
        }
}


    private void displayStarWithAnimationForFirstTimeUser() {
        try{
        Glide.with(this).asGif().load(R.drawable.grey_stars).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(1);
                return false;
            }
        }).into(homeBinding.starViewFirstTimeUser);
    }catch (Exception e) {
            e.printStackTrace();
        }
}

    private void displayStarWithAnimationForLoadingScreen() {
        try{
        Glide.with(this).asGif().load(R.drawable.grey_stars).listener(new RequestListener<GifDrawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<GifDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GifDrawable resource, Object model, Target<GifDrawable> target, DataSource dataSource, boolean isFirstResource) {
                resource.setLoopCount(1);
                return false;
            }
        }).into(homeBinding.loadingStarView);
    }catch (Exception e) {
            e.printStackTrace();
        }
}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        currentLanguage = mPresenter.getAppLanguage();
        addReadMoreData();
        if (!showScoreLoadingScreen) {
            mPresenter.getProductList(false);
        }
    }

    @Override
    public HomeContract.Presenter createPresenter() {
        return this.mPresenter;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void setProductList(List<ProductsItem> productList, String currentLanguage) {
        List<ProductsItem> items = new ArrayList<>();
        Collections.sort(productList, (lhs, rhs) -> Integer.valueOf(lhs.getOrder()).compareTo(rhs.getOrder()));
        for (ProductsItem productsItem : productList) {
            if (productsItem.isIsdisplay() && (productsItem.getOrder() == 1 || productsItem.getOrder() == 3)) {
                items.add(productsItem);
            }
        }
        productsAdapter.setList(items);
    }

    @Override
    public Context getActivityContext() {
        return getContext();
    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivInfo:
            case R.id.ivInfoNoScore:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                openCreditScoreScreen();
                break;
            case R.id.ivReportInfo:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                //navya crashlytics
                if (reportHistoryItem != null && reportHistoryItem.getReportId() != null) {
                    openCreditReportScreen(reportHistoryItem.getReportTypeId());
                }
                break;
            case R.id.llReport:
                if (reportHistoryItem != null && reportHistoryItem.getReportId() != null) {
                    ((DashboardActivity) getActivityContext()).openDownloadFragment(reportHistoryItem);
                }
                break;
            case R.id.ivHomeFragmentNotification:
                if (SystemClock.elapsedRealtime() - lastClickTime < 2000) return;
                lastClickTime = SystemClock.elapsedRealtime();
                DashboardActivity.currentTabPosition = 4;
                Fragment someFragment = new NotificationFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.add(R.id.flScreensContainer, someFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    private void openCreditReportScreen(String id) {
        Bundle bundle = new Bundle();
        bundle.putString(CALL_API_TYPE, API_PRODUCTS);
        bundle.putString(PRODUCT_ID, id);
        bundle.putSerializable(SELECTED_PRODUCT, selectedCreditReport);
        creditReportFragment = CreditReportFragment.newInstance(bundle);
        creditReportFragment.show(getFragmentManager(), creditReportFragment.getTag());
    }

    private void openCreditScoreScreen() {
        Bundle bundle = new Bundle();
        bundle.putString(CALL_API_TYPE, API_CONFIGURATION);
        bundle.putSerializable(SELECTED_PRODUCT, selectedCreditScore);
        creditReportFragment = CreditReportFragment.newInstance(bundle);
        creditReportFragment.show(getFragmentManager(), creditReportFragment.getTag());
    }


    public void findSelectedCreditScoreFromList(String reportTypeId) {

        for (ProductsItem productsItem : allProductList) {
            if (reportTypeId.equals(productsItem.getId())) {
                selectedCreditScore = productsItem;
            }
        }
    }


    public void findSelectedCreditReportFromList(String reportTypeId) {
        for (ProductsItem productsItem : allProductList) {
            if (reportTypeId.equals(productsItem.getId())) {
                selectedCreditReport = productsItem;
            }
        }
    }

    @Override
    public void loadAllProductList(List<ProductsItem> allProductList) {
        this.allProductList.clear();
        this.allProductList.addAll(allProductList);
    }

    @Override
    public void onPause() {
        super.onPause();
        if (timer != null) {
            timer.cancel();
            timer.purge();
        }
        if (loadingTimerForIncreasing != null) {
            loadingTimerForIncreasing.cancel();
            loadingTimerForIncreasing.purge();
        }
        if (loadingTimerForDecreasing != null) {
            loadingTimerForDecreasing.cancel();
            loadingTimerForDecreasing.purge();
        }
        if (timerForFirstTimeUser != null) {
            timerForFirstTimeUser.cancel();
            timerForFirstTimeUser.purge();
        }
        if (loopAgainHandler != null) {
            loopAgainHandler.removeCallbacksAndMessages(null);
        }
    }

    private void showLoadingAnimationForFirstTimeUser() {
        try {
            displayStarWithAnimationForFirstTimeUser();
            timerForFirstTimeUser = new Timer();
            timerForFirstTimeUser.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    currentPositionForFirstTimeUser = currentPositionForFirstTimeUser + 1;
                    if (currentPositionForFirstTimeUser != MAXIMUM_SCORE - DEFAULT_SCORE) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            try {
                                homeBinding.creditScoreArcFirstUser.setProgress(currentPositionForFirstTimeUser);
                                homeBinding.creditScoreArcFirstUser.setProgressColor(getResources().getColor(R.color.star_unfilled_color));
                            } catch (Exception e) {
                                Timber.d("Exception : " + e.toString());
                            }

                        });
                    } else {
                        if (timerForFirstTimeUser != null) {
                            timerForFirstTimeUser.cancel();
                            timerForFirstTimeUser.purge();
                            if (getActivity() == null) return;
                            getActivity().runOnUiThread(() -> {
                                try {
                                    homeBinding.creditScoreArcFirstUser.setmHideThumb(false);
                                } catch (Exception e) {
                                    Timber.d("Exception : " + e.toString());
                                }
                            });
                        }
                    }
                }
            }, 0, 2);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingAnimationForIncreasing() {
        try {
            loadingTimerForIncreasing = new Timer();
            ((DashboardActivity) getActivityContext()).hideBottomSheet();
            loadingTimerForIncreasing.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    currentPositionForTimer = currentPositionForTimer + 1;
                    if (currentPositionForTimer != MAXIMUM_SCORE - DEFAULT_SCORE) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            try {
                                homeBinding.loadingCreditScoreArc.setProgress(currentPositionForTimer);
                                homeBinding.loadingCreditScoreArc.setProgressColor(getResources().getColor(R.color.star_unfilled_color));
                                homeBinding.loadingTvCreditScore.setText(String.valueOf(currentPositionForTimer + DEFAULT_SCORE));
                                homeBinding.loadingTvCreditScoreTitle.setTextColor(getResources().getColor(R.color.star_unfilled_color));
                            } catch (Exception e) {
                                Timber.d("Exception : " + e.toString());
                            }

                        });
                    } else {
                        if (loadingTimerForIncreasing != null) {
                            loadingTimerForIncreasing.cancel();
                            loadingTimerForIncreasing.purge();
                        }
                        showLoadingAnimationForDecreasing();
                    }
                }
            }, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showLoadingAnimationForDecreasing() {
        try {
            loadingTimerForDecreasing = new Timer();
            loadingTimerForDecreasing.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    currentPositionForTimer = currentPositionForTimer - 1;
                    if (currentPosition <= MAXIMUM_SCORE - DEFAULT_SCORE) {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            try {
                                homeBinding.loadingCreditScoreArc.setProgress(currentPositionForTimer);
                                homeBinding.loadingCreditScoreArc.setProgressColor(getResources().getColor(R.color.star_unfilled_color));
                                homeBinding.loadingTvCreditScore.setText(String.valueOf(currentPositionForTimer + DEFAULT_SCORE));
                                homeBinding.loadingTvCreditScoreTitle.setTextColor(getResources().getColor(R.color.star_unfilled_color));
                                if (currentPositionForTimer < 0) {
                                    if (loadingTimerForDecreasing != null) {
                                        loadingTimerForDecreasing.cancel();
                                        loadingTimerForDecreasing.purge();
                                    }
                                }
                            } catch (Exception e) {
                                Timber.d("Exception : " + e.toString());
                            }
                        });
                    } else {
                        if (getActivity() == null) return;
                        getActivity().runOnUiThread(() -> {
                            homeBinding.loadingTvCreditScore.setText("300");
                        });
                    }
                }
            }, 0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showFirstMessage() {
        //        navya crashlytivs
        if (getActivity() == null) return;
        try {
            getActivity().runOnUiThread(() -> {
                try {
                    homeBinding.loadingDotView.setVisibility(View.VISIBLE);
                    Glide.with(getActivity()).load(R.drawable.loading_dot).into(homeBinding.loadingDotView);
                    homeBinding.loadingTextView.setCharacterDelay(40);
                    homeBinding.loadingTextView.animateText(getString(R.string.your_request_is_being_processed));
                }catch (Exception e ){}
            });
        }catch (Exception e){}
    }

    private void showSecondMessage() {
        if (getActivity() == null) return;
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            try{
                getActivity().runOnUiThread(() -> {
                    //homeBinding.loadingTvCreditScore.setText(String.valueOf(DEFAULT_SCORE));
                    try{
                        homeBinding.loadingTextView.setCharacterDelay(40);
                        homeBinding.loadingTextView.animateText(getString(R.string.pdf_copy_sent));
                    }catch (Exception e){}

                });

            }catch (Exception e){
                e.printStackTrace();
            }
        }, 9000);
    }

    private void showThirdMessage() {
        try{
            if (getActivity() == null) return;
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    getActivity().runOnUiThread(() -> {
                        homeBinding.loadingTvCreditScore.setText(String.valueOf(300));
                        homeBinding.loadingTextView.setCharacterDelay(40);
                        homeBinding.loadingTextView.animateText(getString(R.string.transaction_completed));
                    });
                }catch (Exception e){
                e.printStackTrace();
            }
            }, 18000);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    private void callPurchaseApi() {
        try {
            if (getActivity() == null) return;
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    getActivity().runOnUiThread(() -> {
                        homeBinding.loadingTvCreditScore.setText(String.valueOf(300));
                        mPresenter.getProductList(true);
                        ((DashboardActivity) getActivity()).mPresenter.getProducts(showScoreLoadingScreen);
                    });
                } catch (Exception e){
                        e.printStackTrace();
                    }
            }, 31000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaultScore() {
        try {
            if (getActivity() == null) return;
            final Handler handler = new Handler();
            handler.postDelayed(() -> {
                try {
                    getActivity().runOnUiThread(() -> {
                        homeBinding.loadingTvCreditScore.setText(String.valueOf(300));
                    });
                }catch (Exception e){
                e.printStackTrace();
            }
            }, 8800);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setDefaultScoreAgain() {
        try{
        if (getActivity() == null) return;
        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            try {
                getActivity().runOnUiThread(() -> {
                    homeBinding.loadingTvCreditScore.setText(String.valueOf(300));
                    homeBinding.loadingTvCreditScore.setTextColor(getResources().getColor(R.color.loading_text_color));
                });
            }catch (Exception e){
                e.printStackTrace();
            }
        }, 17600);
    } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startLoopAgain(int delay) {
        loopAgainHandler.postDelayed(() -> {
            loopFirstTime = false;
            showLoadingAnimationForIncreasing();
        }, delay);
    }

    private void showStarLoadingAfterDelay() {
        final Handler handler = new Handler();
        handler.postDelayed(this::displayStarWithAnimationForLoadingScreen, 1000);
    }

    @Override
    public void displayDashboardDetails() {
        showScoreLoadingScreen = false;
        ((DashboardActivity) Objects.requireNonNull(getActivity())).showScoreLoadingScreen = false;
        if (loadingTimerForDecreasing != null) {
            loadingTimerForDecreasing.cancel();
            loadingTimerForDecreasing.purge();
        }
        if (loadingTimerForIncreasing != null) {
            loadingTimerForIncreasing.cancel();
            loadingTimerForIncreasing.purge();
        }
        homeBinding.loadingLayout.setVisibility(View.GONE);
        homeBinding.mainLayout.setVisibility(View.VISIBLE);
        ((DashboardActivity) Objects.requireNonNull(getActivity())).showBottomSheet();
    }

    private void setupReadMoreIndicatorDots() {
        ivArrayDots = new ImageView[3];
        for (int i = 0; i < ivArrayDots.length; i++) {
            ivArrayDots[i] = new ImageView(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, 50,
                    Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
            params.setMargins(5, 0, 5, 0);
            ivArrayDots[i].setLayoutParams(params);
            ivArrayDots[i].setImageResource(R.drawable.ic_read_more_unselected_dot);
            ivArrayDots[i].setOnClickListener(view -> view.setAlpha(1));
            homeBinding.layoutDots.addView(ivArrayDots[i]);
        }
    }
}