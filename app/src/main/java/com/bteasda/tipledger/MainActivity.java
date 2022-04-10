package com.bteasda.tipledger;

import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.bteasda.tipledger.Chart.ChartListActivity;

public class MainActivity extends BaseActivity {
    private Button mAddTipsButton;
    private Button mViewLedgerButton;
    private Button mManageJobsButton;
    private Button mViewSavedReportButton;
    private final int NEW_TIP_ADD_ACTIVITY_REQUEST_CODE = 1;
    private final int NEW_LEDGER_VIEW_ACTIVITY_REQUEST_CODE = 2;
    private final int NEW_JOB_LIST_ACTIVITY_REQUEST_CODE = 3;
    private final int NEW_JOB_SELECTOR_ACTIVITY_LEDGER_VIEW_REQUEST_CODE = 4;
    private final int NEW_JOB_SELECTOR_ACTIVITY_TIP_ADD_REQUEST_CODE = 5;
    private final int NEW_FILTER_DIALOGUE_FOR_CHART_LIST_REQUEST_CODE = 6;
    private final int NEW_CHART_LIST_WITH_FILTER_REQUEST_CODE = 7;
    private final int NEW_SAVED_REPORT_LIST_ACTIVITY_REQUEST_CODE = 8;

    /**
     * Initialize the MainActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mAddTipsButton = findViewById(R.id.addTipsButton);

        mAddTipsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobSelectorActivity.class);
                intent.putExtra("allowAll", false);
                startActivityForResult(intent, NEW_JOB_SELECTOR_ACTIVITY_TIP_ADD_REQUEST_CODE);
            }
        });

        mViewLedgerButton = findViewById(R.id.viewLedgerButton);

        mViewLedgerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobSelectorActivity.class);
                intent.putExtra("allowAll", true);
                startActivityForResult(intent, NEW_JOB_SELECTOR_ACTIVITY_LEDGER_VIEW_REQUEST_CODE);
            }
        });

        mManageJobsButton = findViewById(R.id.manageJobsButton);
        mManageJobsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, JobListActivity.class);
                startActivityForResult(intent, NEW_JOB_LIST_ACTIVITY_REQUEST_CODE);
            }
        });

        Button mViewChartButton = findViewById(R.id.viewChartButton);
        mViewChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FilterDialogueActivity.class);
                intent.putExtra("enableDateAboveFilter", false);
                intent.putExtra("enableDateBelowFilter", false);
                intent.putExtra("enableTipAboveFilter", false);
                intent.putExtra("enableTipBelowFilter", false);
                intent.putExtra("enableTipEqualsFilter", false);

                intent.putExtra("dateAboveFilterValue", 0);
                intent.putExtra("dateBelowFilterValue", 0);
                intent.putExtra("tipAboveFilterValue", 0);
                intent.putExtra("tipBelowFilterValue", 0);
                intent.putExtra("tipEqualsFilterValue", 0);

                startActivityForResult(intent, NEW_FILTER_DIALOGUE_FOR_CHART_LIST_REQUEST_CODE);
            }
        });

        mViewSavedReportButton = findViewById(R.id.viewSavedReportsButton);
        mViewSavedReportButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, TextReportSavedListActivity.class);
                startActivityForResult(intent, NEW_SAVED_REPORT_LIST_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if((requestCode == NEW_TIP_ADD_ACTIVITY_REQUEST_CODE
                || requestCode == NEW_JOB_SELECTOR_ACTIVITY_TIP_ADD_REQUEST_CODE)
                && resultCode == RESULT_OK) {
            if(!data.getBooleanExtra("exit", true)) {
                Intent intent = new Intent(MainActivity.this, TipAddActivity.class);
                intent.putExtra("jobId", data.getIntExtra("jobId", 0));
                startActivityForResult(intent, NEW_TIP_ADD_ACTIVITY_REQUEST_CODE);
            }
        }

        if(requestCode == NEW_JOB_SELECTOR_ACTIVITY_LEDGER_VIEW_REQUEST_CODE && resultCode == RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, LedgerViewActivity.class);
            intent.putExtra("jobId", data.getIntExtra("jobId", 0));
            startActivityForResult(intent, NEW_LEDGER_VIEW_ACTIVITY_REQUEST_CODE);
        }

        if(requestCode == NEW_FILTER_DIALOGUE_FOR_CHART_LIST_REQUEST_CODE && resultCode ==  RESULT_OK) {
            Intent intent = new Intent(MainActivity.this, ChartListActivity.class);

            intent.putExtra("enableDateAboveFilter", data.getBooleanExtra("enableDateAboveFilter", false));
            intent.putExtra("enableDateBelowFilter", data.getBooleanExtra("enableDateBelowFilter", false));
            intent.putExtra("enableTipAboveFilter", data.getBooleanExtra("enableTipAboveFilter", false));
            intent.putExtra("enableTipBelowFilter", data.getBooleanExtra("enableTipBelowFilter", false));
            intent.putExtra("enableTipEqualsFilter", data.getBooleanExtra("enableTipEqualsFilter", false));

            intent.putExtra("dateAboveFilterValue", data.getLongExtra("dateAboveFilterValue", 0));
            intent.putExtra("dateBelowFilterValue", data.getLongExtra("dateBelowFilterValue", 0));
            intent.putExtra("tipAboveFilterValue", data.getFloatExtra("tipAboveFilterValue", 0));
            intent.putExtra("tipBelowFilterValue", data.getFloatExtra("tipBelowFilterValue", 0));
            intent.putExtra("tipEqualsFilterValue", data.getFloatExtra("tipEqualsFilterValue", 0));

            startActivityForResult(intent, NEW_CHART_LIST_WITH_FILTER_REQUEST_CODE);
        }
    }
}