package com.bteasda.tipledger.Chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.lifecycle.Observer;

import com.bteasda.tipledger.BaseActivity;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.LedgerFilter.Filter;
import com.bteasda.tipledger.LedgerFilter.FilterFactory;
import com.bteasda.tipledger.R;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
public class LineChartActivity extends BaseActivity {
    /**
     * Whether or not the job entities have been fetched
     */
    private boolean mJobEntitiesFetched = false;

    /**
     * Whether or not the ledger entries have been fetched
     */
    private boolean mLedgerEntryEntitiesFetched = false;

    /**
     * The list of job entities
     */
    private List<JobEntity> mJobEntities;

    /**
     * The list of ledger entries
     */
    private List<LedgerEntryEntity> mLedgerEntryEntities;

    /**
     * The line chart
     */
    private LineChart chart;

    //@// TODO: 4/16/21
    private TextView tvX, tvY;


    long referenceDate = 0;

    /**
     * Initialize the LineChartV2Activity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_linechart);

        loadJobViewModel();
        loadLedgerEntryViewModel();

        setTitle("LineChartActivity2");

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        //seekBarX = findViewById(R.id.seekBar1);
        //seekBarX.setOnSeekBarChangeListener(this);

        //seekBarY = findViewById(R.id.seekBar2);
        //seekBarY.setOnSeekBarChangeListener(this);

        chart = findViewById(R.id.chart1);
        //chart.setOnChartValueSelectedListener(this);

        // no description text
        chart.getDescription().setEnabled(false);

        // enable touch gestures
        chart.setTouchEnabled(true);

        chart.setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false);
        chart.setHighlightPerDragEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        chart.setPinchZoom(true);

        // set an alternative background color
        chart.setBackgroundColor(Color.LTGRAY);

        // add data
        //seekBarX.setProgress(20);
        //seekBarY.setProgress(30);

        chart.animateX(1500);

        // get the legend (only possible after setting data)
        Legend l = chart.getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.LINE);
        //l.setTypeface(tfLight);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);

        XAxis xAxis = chart.getXAxis();
        //xAxis.setTypeface(tfLight);
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setValueFormatter(new XAxisFormatter());

        YAxis leftAxis = chart.getAxisLeft();
        //leftAxis.setTypeface(tfLight);
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        //leftAxis.setAxisMaximum(200f);
        //leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularityEnabled(true);

        /*
        //YAxis rightAxis = chart.getAxisRight();
        //rightAxis.setTypeface(tfLight);
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaximum(900);
        rightAxis.setAxisMinimum(-200);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

         */

        // setData(100, 1000);
        fetchData();
    }

    /**
     * Fetch the chart data
     */
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

    /**
     * Check if the fetch is completed
     * @return  boolean whether or not the fetch is completed
     */
    public boolean isDataFetchCompleted() {
        return mJobEntitiesFetched && mLedgerEntryEntitiesFetched;
    }

    /**
     * Process and populate the chart data
     */
    public void populateChartData() {
        filterData();

        ArrayList<ArrayList<Entry>> values = new ArrayList<ArrayList<Entry>>();
        ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();

        int index = 0;
        if (chart.getData() != null && chart.getData().getDataSetCount() > 0) {
            for (ILineDataSet chartDataSet : chart.getData().getDataSets()) {
                LineDataSet lineDataSet = (LineDataSet) chartDataSet;
                lineDataSet.setValues(values.get(index));
                dataSets.add(lineDataSet);
                index++;
            }

            chart.getData().notifyDataChanged();
            chart.notifyDataSetChanged();
        } else {
            Calendar calendar = Calendar.getInstance();

            referenceDate = mLedgerEntryEntities.get(0).getAddedDateTime().toInstant().toEpochMilli();

            int i = 0; // the current ledger index we are on
            int x = 0; // the number of times we have looped through the current ledger
            int y = 0; // the number of times the ledger has added an entry
            long currentEpochMilli = 0;
            long nextEpochMilli = 0;
            long previousEpochMilli = 0;
            long previousItemEpochMilli = 0;
            long nextItemEpochMilli = 0;
            boolean endOfCurrentLedger = false;
            List<LedgerEntryEntity> tempLedger;
            float currentValue = 0;
            boolean addZeroEntry;

            for (JobEntity jobEntity : mJobEntities) {
                tempLedger = mLedgerEntryEntities.stream()
                        .filter(ledgerEntryEntity -> ledgerEntryEntity.getJobId() == jobEntity.getJobId())
                        .collect(Collectors.toList());
                x = 0;
                previousEpochMilli = 0;
                currentEpochMilli = 0;
                currentValue = 0;
                nextItemEpochMilli = 0;
                endOfCurrentLedger = false;
                y = 0;
                values.add(new ArrayList<Entry>());
                for (LedgerEntryEntity ledgerEntryEntity : tempLedger) {
                    calendar.setTime(ledgerEntryEntity.getAddedDateTime());
                    calendar.set(Calendar.HOUR_OF_DAY, 0);
                    calendar.set(Calendar.MINUTE, 0);
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);


                    if(x > 0) {
                        previousEpochMilli = currentEpochMilli;
                    }
                    currentEpochMilli = calendar.toInstant().toEpochMilli();

                    calendar.set(Calendar.DAY_OF_MONTH, calendar.get(Calendar.DAY_OF_MONTH) + 1);
                    nextEpochMilli = calendar.toInstant().toEpochMilli();
                    if(x < tempLedger.size() - 1) {
                        nextItemEpochMilli = tempLedger.get(x + 1).getAddedDateTime().toInstant().toEpochMilli();
                    } else {
                        endOfCurrentLedger = true;
                    }

                    currentValue += ledgerEntryEntity.getCredit();

                    if (nextItemEpochMilli >= nextEpochMilli || endOfCurrentLedger) { // add values to dataset
                        if(y == 0 || currentEpochMilli - previousEpochMilli >= 86400000) { // add zero value if day before has no values
                            values.get(i).add(new Entry((float) (currentEpochMilli - 86400000 - referenceDate) / 60000000, 0));
                        }

                        values.get(i).add(new Entry((float) (currentEpochMilli - referenceDate) / 60000000, currentValue));

                        if(nextItemEpochMilli >= nextEpochMilli + 86400000 || endOfCurrentLedger) { // add zero value if day after has no values
                            values.get(i).add(new Entry((float) (nextEpochMilli- referenceDate) / 60000000, 0));
                        }

                        currentValue = 0; // zero the value for the next aggregate set
                        y++;

                    }

                    x++;
                }
                LineDataSet lineDataSet = new LineDataSet(values.get(i), jobEntity.getName());
                lineDataSet.setColor(ColorTemplate.COLORFUL_COLORS[i], 130);
                lineDataSet.setDrawValues(true);
                dataSets.add(lineDataSet);
                i++;
            }

            LineData data = new LineData(dataSets);
            //data.setValueFormatter(new XAxisFormatter());
            data.setValueTextColor(Color.BLACK);
            chart.setData(data);
        }

        //chart.setFitBars(true);
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

    public class XAxisFormatter extends ValueFormatter
    {
        private SimpleDateFormat mSDF;

        public XAxisFormatter() {
            mSDF = new SimpleDateFormat("M/d");
        }

        public String getFormattedValue(float value) {
            Float flo = new Float(value);
            return mSDF.format(new Date((flo.longValue() * 60000000) + referenceDate));
        }
    }
}