package com.aecb.ui.scorehistory.presenter;

import com.aecb.AppConstants;
import com.aecb.base.mvp.MvpBasePresenterImpl;
import com.aecb.data.DataManager;
import com.aecb.data.api.models.getproducts.GetProductResponse;
import com.aecb.data.api.models.getproducts.ProductsItem;
import com.aecb.data.api.models.purchasehistory.HistoryItem;
import com.aecb.data.api.models.purchasehistory.PurchaseHistoryResponse;
import com.aecb.ui.dashboard.view.DashboardActivity;
import com.aecb.ui.home.view.HomeFragment;
import com.aecb.util.SharedPreferenceStringLiveData;
import com.aecb.util.Utilities;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import timber.log.Timber;

import static com.aecb.AppConstants.AppLanguage.ISO_CODE_ENG;
import static com.aecb.AppConstants.DateFormats.DEFAULT_API_FORMAT;
import static com.aecb.AppConstants.ProductIds.SCORE_ID;
import static com.aecb.AppConstants.ProductIds.SCORE_WITH_REPORT_ID;
import static com.aecb.AppConstants.ProductIds.isDisplayFalseProduct;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PRODUCTS_LIST;
import static com.aecb.data.preference.PrefHelperImpl.KEY_PURCHASE_HISTORY;
import static com.aecb.util.Utilities.stringToDate;

public class ScoreHistoryPresenterImpl extends MvpBasePresenterImpl<ScoreHistoryContract.View>
        implements ScoreHistoryContract.Presenter {

    ArrayList<Integer> positionList = new ArrayList<>();
    List<String> months = new ArrayList<>();
    List<Date> monthsWithDate = new ArrayList<>();
    private DataManager mDataManager;
    private List<HistoryItem> filteredScoresOnly = new ArrayList<>();
    private HashMap<Integer, HistoryItem> graphPointsWithData;
    private List<ProductsItem> allProductList = new ArrayList<>();

    @Inject
    public ScoreHistoryPresenterImpl(DataManager mDataManager) {
        this.mDataManager = mDataManager;
    }

    @Override
    public void getPurchaseHistory() {
        getProductList();
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PURCHASE_HISTORY, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PURCHASE_HISTORY, "").observe(view.getLifeCycleOwner(), value -> {
                String data;
                try {
                    Gson gson = new Gson();
                    try {
                        data = loadJSONFromAsset();
                    } catch (Exception e) {
                        data = value;
                        Timber.d("Exception : " + e.toString());
                    }
                    PurchaseHistoryResponse purchaseHistoryResponse = gson.fromJson(data, PurchaseHistoryResponse.class);
                    Timber.d("purchaseHistory==" + purchaseHistoryResponse.toString());
                    if (purchaseHistoryResponse != null) {
                        List<HistoryItem> purchaseHistory = purchaseHistoryResponse.getData().getHistory();
                        filteredScoresOnly.clear();
                        for (HistoryItem historyItem : purchaseHistory) {
                            if (historyItem.getReportTypeId().equals(SCORE_ID) || historyItem.getReportTypeId().equals(SCORE_WITH_REPORT_ID)) {
                                filteredScoresOnly.add(historyItem);
                            }
                            for (ProductsItem productsItem : isDisplayFalseProduct) {
                                if (historyItem.getReportTypeId().equals(productsItem.getId())) {
                                    filteredScoresOnly.add(historyItem);
                                }
                            }
                        }
                        Collections.sort(filteredScoresOnly, (m1, m2) -> m1.getCreatedon().compareTo(m2.getCreatedon()));
                        Collections.reverse(filteredScoresOnly);
                        checkIfScoreAreThere();
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }
            });
        });
    }

    public void getProductList() {
        ifViewAttached(view -> {
            SharedPreferenceStringLiveData sharedPreferenceStringLiveData = new SharedPreferenceStringLiveData(mDataManager.getPrefReference(), KEY_PRODUCTS_LIST, "");
            sharedPreferenceStringLiveData.getStringLiveData(KEY_PRODUCTS_LIST, "").observe(view.getLifeCycleOwner(), value -> {
                try {
                    GetProductResponse productResponse = new Gson().fromJson(value, GetProductResponse.class);
                    if (productResponse != null) {
                        allProductList.clear();
                        allProductList.addAll(productResponse.getData().getProducts());
                    }
                } catch (Exception e) {
                    Timber.d("Exception : " + e.toString());
                }

            });
        });
    }

    private void checkIfScoreAreThere() {
        ifViewAttached(view -> {
            if (!filteredScoresOnly.isEmpty()) {
                loadAppropriateMonths(filteredScoresOnly);
            } else {
                view.noScoreAvailable();
            }
        });
    }

    private void loadAppropriateMonths(List<HistoryItem> purchaseHistory) {
        months.clear();
        monthsWithDate.clear();
        Locale locale = new Locale(dataManager.getCurrentLanguage());
        SimpleDateFormat MONTH_SIMPLE_DATE_FORMAT = new SimpleDateFormat(AppConstants.DateFormats.MMM, locale);
        Date firstDate = stringToDate(purchaseHistory.get(0).getCreatedon(), DEFAULT_API_FORMAT);
        Date lastDate = stringToDate(purchaseHistory.get(filteredScoresOnly.size() - 1).getCreatedon(), DEFAULT_API_FORMAT);
        //get all past months list
        Calendar c = new GregorianCalendar();
        c.setTime(firstDate);
        int totalPastMonths = Utilities.monthsBetweenDates(lastDate, firstDate);
        months.add(MONTH_SIMPLE_DATE_FORMAT.format(c.getTime()));
        monthsWithDate.add(c.getTime());
        for (int i = 1; i < totalPastMonths; i++) {
            c.add(Calendar.MONTH, -1);
            months.add(MONTH_SIMPLE_DATE_FORMAT.format(c.getTime()));
            monthsWithDate.add(c.getTime());
        }

        // if list of months size less than 12 than add last 12 months
        if (months.size() < 12) {
            months.clear();
            monthsWithDate.clear();
            c.setTime(firstDate);
            months.add(MONTH_SIMPLE_DATE_FORMAT.format(c.getTime()));
            monthsWithDate.add(c.getTime());
            for (int i = 0; i < 11; i++) {
                c.add(Calendar.MONTH, -1);
                months.add(MONTH_SIMPLE_DATE_FORMAT.format(c.getTime()));
                monthsWithDate.add(c.getTime());
            }
        }
        if (dataManager.getCurrentLanguage().equals(ISO_CODE_ENG)) {
            Collections.reverse(months);
            Collections.reverse(monthsWithDate);
        }
        setGraphPoints(months);
    }

    private void setGraphPoints(List<String> months) {
        graphPointsWithData = new HashMap<>();
        Calendar purchaseDateCalendar = Calendar.getInstance();
        Calendar monthDateCalendar = Calendar.getInstance();
        for (int i = 0; i < filteredScoresOnly.size(); i++) {
            Date purchaseDate = stringToDate(filteredScoresOnly.get(i).getCreatedon(), DEFAULT_API_FORMAT);
            purchaseDateCalendar.setTime(purchaseDate);
            for (Date date : monthsWithDate) {
                monthDateCalendar.setTime(date);
                if (purchaseDateCalendar.get(Calendar.YEAR) == monthDateCalendar.get(Calendar.YEAR)) {
                    if (purchaseDateCalendar.get(Calendar.MONTH) == monthDateCalendar.get(Calendar.MONTH)) {
                        int monthDatePosition = monthsWithDate.indexOf(date);
                        if (!positionList.contains(monthDatePosition)) {
                            positionList.add(monthDatePosition);
                            graphPointsWithData.put(monthDatePosition, filteredScoresOnly.get(i));
                        }
                    }
                }
            }
        }
        ifViewAttached(view -> {
            view.setupScoreList(filteredScoresOnly, allProductList);
        });
    }

    private String loadJSONFromAsset() {
        String json = null;
        InputStream is = null;
        try {
            if (DashboardActivity.scenario == 1) {
                is = HomeFragment.context.getAssets().open("NoScore_NoReport.json");
            } else if (DashboardActivity.scenario == 2) {
                is = HomeFragment.context.getAssets().open("SingleScore_NoReport.json");
            } else if (DashboardActivity.scenario == 3) {
                is = HomeFragment.context.getAssets().open("MultipleScore_NoReport.json");
            } else if (DashboardActivity.scenario == 4) {
                is = HomeFragment.context.getAssets().open("NoScore_SingleReport.json");
            } else if (DashboardActivity.scenario == 5) {
                is = HomeFragment.context.getAssets().open("NoScore_MultipleReport.json");
            } else if (DashboardActivity.scenario == 6) {
                is = HomeFragment.context.getAssets().open("MultipleScore_MultipleReport.json");
            } else if (DashboardActivity.scenario == 7) {
                is = HomeFragment.context.getAssets().open("MultipleScore_MultipleReport_No_Last_year.json");
            }
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, StandardCharsets.UTF_8);
        } catch (IOException ex) {
            Timber.d("Error : " + ex.toString());
            return null;
        }
        return json;
    }

    @Override
    public String getCurrentLanguage() {
        return dataManager.getCurrentLanguage();
    }
}