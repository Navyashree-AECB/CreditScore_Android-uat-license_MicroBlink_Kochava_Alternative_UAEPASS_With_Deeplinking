package com.aecb.ui.scorehistory.view;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.aecb.AppConstants;
import com.aecb.R;
import com.aecb.base.mvp.BaseFragment;
import com.aecb.base.mvp.BlurDialogBaseFragment;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.databinding.FragmentScoreHistoryBinding;
import com.aecb.presentation.recycleswipe.SnapHelper;
import com.aecb.ui.noscorefragment.view.NoScoreFragment;
import com.aecb.ui.scorehistory.adapter.ScoreListAdapter;
import com.aecb.ui.scorehistory.presenter.ScoreHistoryContract;
import com.aecb.util.ItemOffsetDecoration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import timber.log.Timber;

public class ScoreHistoryFragment extends BaseFragment<ScoreHistoryContract.View,
        ScoreHistoryContract.Presenter> implements ScoreHistoryContract.View, View.OnClickListener {

    @Inject
    public ScoreHistoryContract.Presenter mPresenter;
    FragmentScoreHistoryBinding scoreHistoryBinding;
    List<HistoryItem> filteredScoresOnly = new ArrayList<>();
    private BlurDialogBaseFragment noScoreFragment;
    private LinearLayoutManager scoreLinearLayoutManager;
    private ScoreListAdapter scoreListAdapter;
    private String currentLanguage;

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_score_history;
    }

    @Override
    public ScoreHistoryContract.Presenter createPresenter() {
        return mPresenter;
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        scoreHistoryBinding = DataBindingUtil.inflate(inflater, getLayoutRes(), container, false);
        View view = scoreHistoryBinding.getRoot();
        initialSetup();
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                currentLanguage = mPresenter.getCurrentLanguage();
                mPresenter.getPurchaseHistory();
            }
        });
    }

    private void initialSetup() {
        scoreHistoryBinding.ivNext.setOnClickListener(this);
        scoreHistoryBinding.ivPrevious.setOnClickListener(this);
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
            scoreHistoryBinding.ivPrevious.setImageDrawable(getResources().getDrawable(R.drawable.ic_right_arrrow));
            scoreHistoryBinding.ivNext.setImageDrawable(getResources().getDrawable(R.drawable.ic_left_arrow));
        }
    }

    private void handleArrowVisibility() {
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
            if (scoreLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                scoreHistoryBinding.ivPrevious.setImageResource(R.drawable.ic_right_arrrow);
            } else {
                scoreHistoryBinding.ivPrevious.setImageResource(R.drawable.ic_right_arrow_blue);
            }
            if (scoreLinearLayoutManager.findFirstVisibleItemPosition() == filteredScoresOnly.size() - 1) {
                scoreHistoryBinding.ivNext.setImageResource(R.drawable.ic_left_arrow);
            } else {
                scoreHistoryBinding.ivNext.setImageResource(R.drawable.ic_left_arrow_blue);
            }
        } else {
            if (scoreLinearLayoutManager.findFirstVisibleItemPosition() == 0) {
                scoreHistoryBinding.ivPrevious.setImageResource(R.drawable.ic_left_arrow);
            } else {
                scoreHistoryBinding.ivPrevious.setImageResource(R.drawable.ic_left_arrow_blue);
            }
            if (scoreLinearLayoutManager.findFirstVisibleItemPosition() == filteredScoresOnly.size() - 1) {
                scoreHistoryBinding.ivNext.setImageResource(R.drawable.ic_right_arrrow);
            } else {
                scoreHistoryBinding.ivNext.setImageResource(R.drawable.ic_right_arrow_blue);
            }
        }

    }

    @Override
    public LifecycleOwner getLifeCycleOwner() {
        return this;
    }


    @Override
    public void setupScoreList(List<HistoryItem> filteredScoresOnly, List<ProductsItem> allProductList) {
        scoreHistoryBinding.llTopView.setVisibility(View.VISIBLE);
        scoreHistoryBinding.tvNoPurchaseFound.setVisibility(View.GONE);
        Collections.reverse(filteredScoresOnly);
        this.filteredScoresOnly.addAll(filteredScoresOnly);
        scoreHistoryBinding.rvScoreHistory.setHasFixedSize(true);
        if (mPresenter.getCurrentLanguage().equals(AppConstants.AppLanguage.ISO_CODE_ARABIC)) {
            scoreLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, true) {
                @Override
                public boolean canScrollHorizontally() {
                    return true;
                }
            };
        } else {
            scoreLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false) {
                @Override
                public boolean canScrollHorizontally() {
                    return true;
                }
            };
        }
        scoreHistoryBinding.rvScoreHistory.setLayoutManager(scoreLinearLayoutManager);
        try {
            LinearSnapHelper linearSnapHelper = new SnapHelper();
            linearSnapHelper.attachToRecyclerView(scoreHistoryBinding.rvScoreHistory);
            scoreListAdapter = new ScoreListAdapter(getActivity(), filteredScoresOnly, this::openNoScoreSheet, allProductList);
            scoreHistoryBinding.rvScoreHistory.addItemDecoration(new ItemOffsetDecoration(getActivity(), R.dimen._6sdp));
            scoreHistoryBinding.rvScoreHistory.setAdapter(scoreListAdapter);
        } catch (Exception e) {
            Timber.d("Exception : " + e.toString());
        }
        scoreHistoryBinding.rvScoreHistory.scrollToPosition(filteredScoresOnly.size() - 1);
        scoreHistoryBinding.rvScoreHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }


            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                handleArrowVisibility();
            }
        });
    }

    @Override
    public void noScoreAvailable() {
        scoreHistoryBinding.llTopView.setVisibility(View.GONE);
        scoreHistoryBinding.tvNoPurchaseFound.setVisibility(View.VISIBLE);
    }

    private void openNoScoreSheet() {
        noScoreFragment = new NoScoreFragment();
        noScoreFragment.show(getFragmentManager(), noScoreFragment.getTag());
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivNext:
                try {
                    scoreHistoryBinding.rvScoreHistory.smoothScrollToPosition(scoreLinearLayoutManager.findFirstVisibleItemPosition() + 1);
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }

                break;
            case R.id.ivPrevious:
                try {
                    if (scoreLinearLayoutManager.findFirstVisibleItemPosition() != 0) {
                        scoreHistoryBinding.rvScoreHistory.smoothScrollToPosition(scoreLinearLayoutManager.findFirstVisibleItemPosition() - 1);
                    }
                } catch (Exception e) {
                    Timber.e(e.getMessage());
                }

                break;
        }
    }
}
