package com.bteasda.tipledger.Chart;

import android.graphics.Color;
import android.os.Bundle;

import androidx.lifecycle.Observer;

import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import com.bteasda.tipledger.BaseActivity;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.LedgerFilter.Filter;
import com.bteasda.tipledger.LedgerFilter.FilterFactory;
import com.bteasda.tipledger.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class BarChartActivity extends BaseActivity implements OnChartValueSelectedListener {
    /**
     * Whether or not the list of job entities have been fetched
     */
    private boolean mJobEntitiesFetched = false;

    /**
     * Whether or not the list of ledger entities have been fetched
     */
    private boolean mLedgerEntryEntitiesFetched = false;

    /**
     * The list of job entities
     */
    private List<JobEntity> mJobEntities;

    /**
     * List of ledger entries
     */
    private List<LedgerEntryEntity> mLedgerEntryEntities;

    /**
     * The bar chart we will be populating with data
     */
    private BarChart chart;

    /**
     * The date for the first piece of chart data, used to work around the fact the chart data object accepts a float
     */
    long referenceDate;

    /**
     * Initialize the BarChartActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_barchart);

        loadJobViewModel();
        loadLedgerEntryViewModel();

        setTitle("Date vs. Tip Amount");

        chart = findViewById(R.id.chart1);
        chart.setOnChartValueSelectedListener(this);

        chart.getDescription().setEnabled(false);

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(40);

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false);

        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        chart.setDrawValueAboveBar(false);
        chart.setHighlightFullBarEnabled(false);

        // change the position of the y-labels
        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setValueFormatter(new XAxisFormatter());
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
        chart.getAxisRight().setEnabled(false);

        XAxis xLabels = chart.getXAxis();
        xLabels.setPosition(XAxis.XAxisPosition.TOP);
        //xLabels.setValueFormatter(new XAxisFormatter());

        // chart.setDrawXLabels(false);
        // chart.setDrawYLabels(false);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setFormSize(8f);
        l.setFormToTextSpace(4f);
        l.setXEntrySpace(6f);

        fetchData();

        // chart.setDrawLegend(false);
    }


    public void fetchData() {
        mJobViewModel.getAllJobs().observe(this, new Observer<List<JobEntity>>() {
            @Override
            public void onChanged(List<JobEntity> jobEntities) {
                mJobEntities = jobEntities;
                mJobEntitiesFetched = true;
                if(isDataFetchCompleted()) {
                    populateChartData();
                }
            }
        });

        mLedgerEntryViewModel.getAllLedgerEntries().observe(this, new Observer<List<LedgerEntryEntity>>() {
            @Override
            public void onChanged(List<LedgerEntryEntity> ledgerEntryEntities) {
                mLedgerEntryEntities = ledgerEntryEntities;
                mLedgerEntryEntitiesFetched = true;
                if(isDataFetchCompleted()) {
                    populateChartData();
                }
            }
        });
    }

    public boolean isDataFetchCompleted() {
        return mJobEntitiesFetched && mLedgerEntryEntitiesFetched;
    }

    public void populateChartData() {
        filterData();

        ArrayList<BarEntry> values = new ArrayList<BarEntry>();

        Calendar calendar = Calendar.getInstance();

        referenceDate = mLedgerEntryEntities.get(0).getAddedDateTime().toInstant().toEpochMilli();

        int y = 0;
        int i = 0;
        long currentEpochMilli = 0;
        long nextEpochMilli = 0;
        long previousEpochMilli = 0;
        List<LedgerEntryEntity> tempLedger;
        float[] currentValues = new float[mJobEntities.size()];

        for (LedgerEntryEntity ledgerEntryEntity : mLedgerEntryEntities) {
            calendar.setTime(ledgerEntryEntity.getAddedDateTime());
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
            calendar.set(Calendar.MILLISECOND, 0);

            previousEpochMilli = currentEpochMilli;
            currentEpochMilli = calendar.toInstant().toEpochMilli();
            // if it is a new day
            if(currentEpochMilli > nextEpochMilli) {
                // add values to dataset
                values.add(new BarEntry((float) (previousEpochMilli - (y == 0 ? 0 : referenceDate)) / 60000000, currentValues));

                currentValues = new float[mJobEntities.size()];
                nextEpochMilli = currentEpochMilli + 86400000;
            }

            i = 0;
            for (JobEntity currentJob : mJobEntities) {
                if(currentJob.getJobId() == ledgerEntryEntity.getJobId()) {
                    currentValues[i] += ledgerEntryEntity.getCredit();
                }
                i++;
            }

            if(y == mLedgerEntryEntities.size() - 1) { //currentEpochMilli > nextEpochMilli) {
                values.add(new BarEntry((float) (currentEpochMilli - referenceDate) / 60000000, currentValues));
            }

            y++;
        }


        BarDataSet set1;

        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) chart.getData().getDataSetByIndex(0);
            set1.setValues(values);
            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            set1 = new BarDataSet(values, "Date vs. Tip Amount");
            set1.setDrawIcons(false);
            set1.setColors(getColors());

            String[] labels = new String[mJobEntities.size()];
            for(int w = 0; w < mJobEntities.size(); w++) {
                labels[w] = mJobEntities.get(w).getName();
            }
            set1.setStackLabels(labels);

            ArrayList<IBarDataSet> dataSets = new ArrayList<>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            //data.setValueFormatter(new XAxisFormatter());
            data.setValueTextColor(Color.BLACK);

            chart.setData(data);
        }

            chart.setFitBars(true);
            chart.invalidate();
    }

    /**
     * Filter the chart data
     */
    public void filterData() {
        FilterFactory.FactoryConfig factoryConfig = new FilterFactory.FactoryConfig();

        Bundle extras = getIntent().getExtras();

        factoryConfig.setEnableDateAboveFilter(extras.getBoolean("enableDateAboveFilter"));
        factoryConfig.setEnableDateBelowFilter(extras.getBoolean("enableDateBelowFilter"));
        factoryConfig.setEnableTipAboveFilter(extras.getBoolean("enableTipAboveFilter"));
        factoryConfig.setEnableTipBelowFilter(extras.getBoolean("enableTipBelowFilter"));
        factoryConfig.setEnableTipEqualsFilter(extras.getBoolean("enableTipEqualsFilter"));

        factoryConfig.setDateAboveFilterValue(new Date(extras.getLong("dateAboveFilterValue")));
        factoryConfig.setDateBelowFilterValue(new Date(extras.getLong("dateBelowFilterValue")));
        factoryConfig.setTipAboveFilterValue(extras.getFloat("tipAboveFilterValue"));
        factoryConfig.setTipBelowFilterValue(extras.getFloat("tipBelowFilterValue"));
        factoryConfig.setTipEqualsFilterValue(extras.getFloat("tipEqualsFilterValue"));

        Filter filter = FilterFactory.make(mLedgerEntryEntities, factoryConfig);
        mLedgerEntryEntities = filter.execute();
    }


    public void onValueSelected(Entry e, Highlight h) {
        Log.i("Activity", "Selected: " + e.toString() + ", dataSet: " + h.getDataSetIndex());
    }

    public void onNothingSelected() {
        Log.i("Activity", "Nothing selected.");
    }

    private int[] getColors() {

        // have as many colors as stack-values per entry
        int[] colors = new int[3];

        System.arraycopy(ColorTemplate.MATERIAL_COLORS, 0, colors, 0, 3);

        return colors;
    }

    public class XAxisFormatter extends ValueFormatter
    {
        private SimpleDateFormat mSDF;

        public XAxisFormatter() {
            mSDF = new SimpleDateFormat("M/d/y");
        }

        public String getFormattedValue(float value) {
            Float flo = new Float(value);
            return mSDF.format(new Date((flo.longValue() + referenceDate) * 60000000));
        }
    }
}
