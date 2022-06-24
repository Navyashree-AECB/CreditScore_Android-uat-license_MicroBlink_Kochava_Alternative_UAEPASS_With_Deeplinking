package com.aecb.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.aecb.R;
import com.aecb.ui.dashboard.view.DashboardActivity;

public class DummyScreen extends AppCompatActivity implements View.OnClickListener {
    Button noReportNoScore, noReportSingleScore, noReportMultiScore, singleReportNoScore, multiReportNoScore,
            multiReportMultiScore, multiReportMultiScoreNoLastYear,onlyReportWithScore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dummy_screen);
        noReportNoScore = findViewById(R.id.noReportNoScore);
        noReportSingleScore = findViewById(R.id.noReportSingleScore);
        noReportMultiScore = findViewById(R.id.noReportMultiScore);
        singleReportNoScore = findViewById(R.id.singleReportNoScore);
        multiReportNoScore = findViewById(R.id.multiReportNoScore);
        multiReportMultiScore = findViewById(R.id.multiReportMultiScore);
        multiReportMultiScoreNoLastYear = findViewById(R.id.multiReportMultiScoreNoLastYear);
        onlyReportWithScore = findViewById(R.id.onlyReportWithScore);
        noReportNoScore.setOnClickListener(this);
        noReportSingleScore.setOnClickListener(this);
        noReportMultiScore.setOnClickListener(this);
        singleReportNoScore.setOnClickListener(this);
        multiReportNoScore.setOnClickListener(this);
        multiReportMultiScore.setOnClickListener(this);
        multiReportMultiScoreNoLastYear.setOnClickListener(this);
        onlyReportWithScore.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.noReportNoScore:
                Intent i1 = new Intent(this, DashboardActivity.class);
                i1.putExtra("KEY", 1);
                startActivity(i1);
                break;
            case R.id.noReportSingleScore:
                Intent i2 = new Intent(this, DashboardActivity.class);
                i2.putExtra("KEY", 2);
                startActivity(i2);
                break;
            case R.id.noReportMultiScore:
                Intent i3 = new Intent(this, DashboardActivity.class);
                i3.putExtra("KEY", 3);
                startActivity(i3);
                break;
            case R.id.singleReportNoScore:
                Intent i4 = new Intent(this, DashboardActivity.class);
                i4.putExtra("KEY", 4);
                startActivity(i4);
                break;
            case R.id.multiReportNoScore:
                Intent i5 = new Intent(this, DashboardActivity.class);
                i5.putExtra("KEY", 5);
                startActivity(i5);
                break;
            case R.id.multiReportMultiScore:
                Intent i6 = new Intent(this, DashboardActivity.class);
                i6.putExtra("KEY", 6);
                startActivity(i6);
                break;
            case R.id.onlyReportWithScore:
                Intent i7 = new Intent(this, DashboardActivity.class);
                i7.putExtra("KEY", 7);
                startActivity(i7);
                break;
        }
    }
}