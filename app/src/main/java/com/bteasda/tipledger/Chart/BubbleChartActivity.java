
package com.bteasda.tipledger.Chart;

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
import com.github.mikephil.charting.charts.BubbleChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BubbleData;
import com.github.mikephil.charting.data.BubbleDataSet;
import com.github.mikephil.charting.data.BubbleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBubbleDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class BubbleChartActivity extends BaseActivity implements OnChartValueSelectedListener {
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
     * The bubble chart
     */
    private BubbleChart chart;

    //@// TODO: 4/16/21
    private TextView tvX, tvY;

    /**
     * Initialize the BubbleChartActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_bubblechart);

        loadJobViewModel();
        loadLedgerEntryViewModel();
        
        setTitle("BubbleChartActivity");

        tvX = findViewById(R.id.tvXMax);
        tvY = findViewById(R.id.tvYMax);

        chart = findViewById(R.id.chart1);
        chart.getDescription().setEnabled(false);

        chart.setOnChartValueSelectedListener(this);

        chart.setDrawGridBackground(false);

        chart.setTouchEnabled(true);

        // enable scaling and dragging
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);

        //chart.setMaxVisibleValueCount(24);
        chart.setPinchZoom(true);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        //l.setTypeface(tfLight);

        YAxis yl = chart.getAxisLeft();
        //yl.setTypeface(tfLight);
        //yl.setSpaceTop(30f);
        //yl.setSpaceBottom(30f);
        yl.setDrawZeroLine(false);

        chart.getAxisRight().setEnabled(false);

        XAxis xl = chart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        //xl.setGranularity(86400000);
        //xl.setGranularityEnabled(true);
        xl.setValueFormatter(new XAxisFormatter());
        //xl.setTypeface(tfLight);

        chart.setAutoScaleMinMaxEnabled(true);

        fetchData();
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

        ArrayList<ArrayList<BubbleEntry>> values = new ArrayList<ArrayList<BubbleEntry>>();
        ArrayList<IBubbleDataSet> dataSets = new ArrayList<IBubbleDataSet>();

        Calendar calendar = Calendar.getInstance();

        int x = 0;
        int y = 0;
        List<LedgerEntryEntity> tempLedger;
        for (JobEntity jobEntity : mJobEntities) {
            tempLedger = mLedgerEntryEntities.stream()
                    .filter(ledgerEntryEntity -> ledgerEntryEntity.getJobId() == jobEntity.getJobId())
                    .collect(Collectors.toList());
            y = 0;
            values.add(new ArrayList<BubbleEntry>());
            for (LedgerEntryEntity ledgerEntryEntity : tempLedger) {

                calendar.setTime(ledgerEntryEntity.getAddedDateTime());
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                values.get(x).add(new BubbleEntry(
                        calendar.getTimeInMillis() / 86400000,
                        ledgerEntryEntity.getAddedDateTime().getHours(),
                        ledgerEntryEntity.getCredit()
                ));
                y++;
            }
            BubbleDataSet bubbleDataSet = new BubbleDataSet(values.get(x), jobEntity.getName());
            bubbleDataSet.setColor(ColorTemplate.COLORFUL_COLORS[x], 130);
            bubbleDataSet.setDrawValues(true);
            dataSets.add(bubbleDataSet);
            x++;
        }

        for(ArrayList<BubbleEntry> valueSet : values) {
        }

        BubbleData data = new BubbleData(dataSets);
        chart.setData(data);
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

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        Log.i("VAL SELECTED",
                "Value: " + e.getY() + ", xIndex: " + e.getX()
                        + ", DataSet index: " + h.getDataSetIndex());
    }

    @Override
    public void onNothingSelected() {}


    protected float getRandom(float range, float start) {
        return (float) (Math.random() * range) + start;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        // overridePendingTransition(R.anim.move_left_in_activity, R.anim.move_right_out_activity);
    }

    /**
     * Format the X Axis value to a human readable date
     */
    public class XAxisFormatter extends ValueFormatter
    {
        private SimpleDateFormat mSDF;

        public XAxisFormatter() {
            mSDF = new SimpleDateFormat("M/d");
        }

        /**
         * Get the formatted value
         * @param value the epoch milli of the date divided by 60000000 and subtracted the reference date
         * @return String human readable date
         */
        public String getFormattedValue(float value) {
            Float flo = new Float(value * 86400000);
            return mSDF.format(new Date(flo.longValue()));
        }
    }
}
