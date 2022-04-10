package com.bteasda.tipledger.Chart;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.bteasda.tipledger.BaseActivity;
import com.bteasda.tipledger.R;

public class ChartListActivity extends BaseActivity {
    private static final int NEW_BUBBLE_CHART_ACTIVITY_REQUEST_CODE = 30;
    private static final int NEW_BAR_CHART_DAILY_ACTIVITY_REQUEST_CODE = 31;
    private static final int NEW_BAR_CHART_MONTHLY_ACTIVITY_REQUEST_CODE = 32;
    private static final int NEW_LINE_CHART_DAILY_ACTIVITY_REQUEST_CODE = 33;
    private static final int NEW_LINE_CHART_MONTHLY_ACTIVITY_REQUEST_CODE = 34;
    private static final int NEW_TEXT_REPORT_MONTHLY_ACTIVITY_REQUEST_CODE = 35;

    /**
     * The button used to view the bubble chart
     */
    private Button viewBubbleChartButton;

    /**
     * The button used to view the daily bar chart
     */
    private Button viewBarChartDailyButton;

    /**
     * The button used to view the monthly bar chart
     */
    private Button viewBarChartMonthlyButton;

    /**
     * The button used to view daily line chart
     */
    private Button viewLineChartDailyButton;

    /**
     * The button used to view the monthly line chart
     */
    private Button viewLineChartMonthlyButton;

    /**
     * The button used to view the monthly text report
     */
    private Button viewTextReportMonthlyButton;

    /**
     * Initialize the ChartListActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_chart_list);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewBubbleChartButton = findViewById(R.id.viewBubbleChartButton);
        viewBubbleChartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartListActivity.this, BubbleChartActivity.class);
                attachFilterDataToIntent(intent);
                startActivityForResult(intent, NEW_BUBBLE_CHART_ACTIVITY_REQUEST_CODE);
            }
        });

        viewBarChartDailyButton = findViewById(R.id.viewBarChartDailyButton);
        viewBarChartDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartListActivity.this, BarChartActivity.class);
                attachFilterDataToIntent(intent);
                startActivityForResult(intent, NEW_BAR_CHART_DAILY_ACTIVITY_REQUEST_CODE);
            }
        });

        viewBarChartMonthlyButton = findViewById(R.id.viewBarChartMonthlyButton);
        viewBarChartMonthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartListActivity.this, BarChartMonthlyActivity.class);
                attachFilterDataToIntent(intent);
                startActivityForResult(intent, NEW_BAR_CHART_MONTHLY_ACTIVITY_REQUEST_CODE);
            }
        });

        viewLineChartDailyButton = findViewById(R.id.viewLineChartDailyButton);
        viewLineChartDailyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartListActivity.this, LineChartActivity.class);
                attachFilterDataToIntent(intent);
                startActivityForResult(intent, NEW_LINE_CHART_DAILY_ACTIVITY_REQUEST_CODE);
            }
        });

        viewLineChartMonthlyButton = findViewById(R.id.viewLineChartMonthlyButton);
        viewLineChartMonthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartListActivity.this, LineChartMonthlyActivity.class);
                attachFilterDataToIntent(intent);
                startActivityForResult(intent, NEW_LINE_CHART_MONTHLY_ACTIVITY_REQUEST_CODE);
            }
        });

        viewTextReportMonthlyButton = findViewById(R.id.viewTextReportMonthlyButton);
        viewTextReportMonthlyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChartListActivity.this, TextReportMonthly.class);
                attachFilterDataToIntent(intent);
                startActivityForResult(intent, NEW_TEXT_REPORT_MONTHLY_ACTIVITY_REQUEST_CODE);
            }
        });

    }

    /**
     * Pass the filter data passed into this view to another view
     *
     * @param intent the intent we are passing the data to
     */
    public void attachFilterDataToIntent(Intent intent) {
        Bundle extras = getIntent().getExtras();

        intent.putExtra("enableDateAboveFilter", extras.getBoolean("enableDateAboveFilter", false));
        intent.putExtra("enableDateBelowFilter", extras.getBoolean("enableDateBelowFilter", false));
        intent.putExtra("enableTipAboveFilter", extras.getBoolean("enableTipAboveFilter", false));
        intent.putExtra("enableTipBelowFilter", extras.getBoolean("enableTipBelowFilter", false));
        intent.putExtra("enableTipEqualsFilter", extras.getBoolean("enableTipEqualsFilter", false));

        intent.putExtra("dateAboveFilterValue", extras.getLong("dateAboveFilterValue", 0));
        intent.putExtra("dateBelowFilterValue", extras.getLong("dateBelowFilterValue", 0));
        intent.putExtra("tipAboveFilterValue", extras.getFloat("tipAboveFilterValue", 0));
        intent.putExtra("tipBelowFilterValue", extras.getFloat("tipBelowFilterValue", 0));
        intent.putExtra("tipEqualsFilterValue", extras.getFloat("tipEqualsFilterValue", 0));
    }
}
