package com.bteasda.tipledger.Chart;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.bteasda.tipledger.BaseActivity;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.Entities.TextReportDataEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;
import com.bteasda.tipledger.LedgerFilter.Filter;
import com.bteasda.tipledger.LedgerFilter.FilterFactory;
import com.bteasda.tipledger.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class TextReportMonthly extends BaseActivity {
    private boolean mJobEntitiesFetched = false;
    private boolean mLedgerEntryEntitiesFetched = false;
    private ArrayList<ArrayList<Entry>> values = new ArrayList<ArrayList<Entry>>();

    private List<JobEntity> mJobEntities;
    private List<LedgerEntryEntity> mLedgerEntryEntities;

    private Button saveButton;

    /**
     * Initialize the TextReportMonthly activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_text_report_monthly);

        loadJobViewModel();
        loadLedgerEntryViewModel();
        loadTextReportViewModel();
        loadTextReportDataViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        saveButton = new Button(this);
        saveButton.setText("Save Report");
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                promptForTitle();

            }
        });

        fetchData();
    }

    /**
     * Prompt the user for a title for the report we are saving
     */
    public void promptForTitle() {
        EditText titleEditText = new EditText(this);

        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Choose a title for the report:");
        dialog.setView(titleEditText);
        dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    saveReport(titleEditText.getText().toString());
            }
        });
        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {}
        });
        dialog.show();
    }

    /**
     * Create a text_reports entry
     * @param title
     */
    public void saveReport(String title) {
        TextReportEntity textReportEntity = new TextReportEntity(new Date(), title);
        mTextReportViewModel.insert(textReportEntity);

        mTextReportViewModel.getLastTextReport().observe(this, new Observer<TextReportEntity>() {
            @Override
            public void onChanged(TextReportEntity textReportEntity) {
                saveReportData(textReportEntity);
            }
        });

    }

    /**
     * Save the report data to the text_report_data table
     * @param textReportEntity
     */
    public void saveReportData(TextReportEntity textReportEntity) {
        for(int i = 0; i < mJobEntities.size(); i++) {
            for(Entry entry : values.get(i)) {
                TextReportDataEntity textReportDataEntity = new TextReportDataEntity(entry.getFormattedDate(), textReportEntity.getTextReportId(), mJobEntities.get(i).getJobId(), mJobEntities.get(i).getName(), entry.getTotalCredit(), entry.getMeanCredit(), entry.getLowestCredit(), entry.getHighestCredit());
                mTextReportDataViewModel.insert(textReportDataEntity);
            }
        }

        Toast.makeText(getApplicationContext(), "Report was saved.", Toast.LENGTH_SHORT).show();
    }

    /**
     * Fetch the data for the report
     */
    public void fetchData() {
        mJobViewModel.getAllJobs().observe(this, new Observer<List<JobEntity>>() {
            @Override
            public void onChanged(List<JobEntity> jobEntities) {
                mJobEntities = jobEntities;
                mJobEntitiesFetched = true;
                if(isDataFetchCompleted()) {
                    processReportData();
                }
            }
        });

        mLedgerEntryViewModel.getAllLedgerEntries().observe(this, new Observer<List<LedgerEntryEntity>>() {
            @Override
            public void onChanged(List<LedgerEntryEntity> ledgerEntryEntities) {
                mLedgerEntryEntities = ledgerEntryEntities;
                mLedgerEntryEntitiesFetched = true;
                if(isDataFetchCompleted()) {
                    processReportData();
                }
            }
        });
    }

    /**
     * Check whether or not the data has been fetched
     * @return
     */
    public boolean isDataFetchCompleted() {
        return mJobEntitiesFetched && mLedgerEntryEntitiesFetched;
    }

    /**
     * Process the report data
     */
    public void processReportData() {
        filterData();

        Calendar calendar = Calendar.getInstance();

        int i = 0; // the current ledger index we are on
        int x = 0; // the number of times we have looped through the current ledger
        int j = 0; // the current count in the set
        int y = 0; // the number of times the ledger has added an entry
        long currentEpochMilli = 0;
        long nextEpochMilli = 0;
        long nextNextEpochMilli = 0;
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
            y = 0;
            j = 0;
            previousEpochMilli = 0;
            currentEpochMilli = 0;
            currentValue = 0;
            nextItemEpochMilli = 0;
            endOfCurrentLedger = false;
            float meanTotal = 0; // the total of all the values of the set, used for the mean average
            float lowestCredit = 0; // the lowest credit of the set
            float highestCredit = 0; // the highest credit of the set
            values.add(new ArrayList<Entry>());
            for (LedgerEntryEntity ledgerEntryEntity : tempLedger) {
                calendar.setTime(ledgerEntryEntity.getAddedDateTime());
                calendar.set(Calendar.DAY_OF_MONTH, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 0);
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);

                meanTotal += ledgerEntryEntity.getCredit();
                if(ledgerEntryEntity.getCredit() < lowestCredit || j == 0) {
                    lowestCredit = ledgerEntryEntity.getCredit();
                }
                if(ledgerEntryEntity.getCredit() > highestCredit) {
                    highestCredit = ledgerEntryEntity.getCredit();
                }

                if(x > 0) {
                    previousEpochMilli = currentEpochMilli;
                }
                currentEpochMilli = calendar.toInstant().toEpochMilli();

                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                nextEpochMilli = calendar.toInstant().toEpochMilli();
                calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
                nextNextEpochMilli = calendar.toInstant().toEpochMilli();
                if(x < tempLedger.size() - 1) {
                    nextItemEpochMilli = tempLedger.get(x + 1).getAddedDateTime().toInstant().toEpochMilli();
                } else {
                    endOfCurrentLedger = true;
                }

                currentValue += ledgerEntryEntity.getCredit();

                if (nextItemEpochMilli >= nextEpochMilli || endOfCurrentLedger) { // add values to dataset
                    values.get(i).add(new Entry(currentEpochMilli, currentValue, meanTotal / (j + 1), lowestCredit,highestCredit));

                    // zero the values for the next aggregate set
                    currentValue = 0;
                    meanTotal = 0;
                    lowestCredit = 0;
                    highestCredit = 0;
                    j = 0;
                    y++;

                } else {
                    j++;
                }

                x++;
            }
            i++;
        }

        buildLayout();
    }

    /**
     * Filter the data
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

    /**
     * Build the report activity layout
     */
    private void buildLayout() {

        LinearLayout.LayoutParams linearLayoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        LinearLayout linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);

        Toolbar toolbar = new Toolbar(this);
        toolbar.setBackgroundColor(getResources().getColor(R.color.design_default_color_primary));
        toolbar.setTitle("Text Report");

        linearLayout.addView(toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        ScrollView scrollView = new ScrollView(this);
        linearLayout.addView(scrollView);

        LinearLayout linearLayout2 = new LinearLayout(this);
        linearLayout2.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setLayoutParams(linearLayoutParams);
        scrollView.addView(linearLayout2);

        RelativeLayout.LayoutParams textViewLayoutParam = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        textViewLayoutParam.setMargins(20, 10, 20, 10);

        int i = 0;
        for (JobEntity jobEntity : mJobEntities) {
            TextView jobTitle = new TextView(this);
            jobTitle.setText(jobEntity.getName());
            jobTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
            linearLayout2.addView(jobTitle);

            GridLayout gridLayout = new GridLayout(this);
            gridLayout.setColumnCount(5);

            /*
            GridLayout.LayoutParams gridlayoutParams = new GridLayout.LayoutParams();
            gridlayoutParams.width = GridLayout.LayoutParams.MATCH_PARENT;
            gridlayoutParams.height = GridLayout.LayoutParams.WRAP_CONTENT;
            gridlayoutParams.setGravity(Gravity.CENTER);
            gridLayout.setLayoutParams(gridlayoutParams);
             */


            TextView dateColTitle = new TextView(this);
            dateColTitle.setText("Month/Year");
            dateColTitle.setLayoutParams(textViewLayoutParam);
            dateColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            gridLayout.addView(dateColTitle);

            TextView totalCreditColTitle = new TextView(this);
            totalCreditColTitle.setText("Total");
            totalCreditColTitle.setLayoutParams(textViewLayoutParam);
            totalCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            gridLayout.addView(totalCreditColTitle);

            TextView meanCreditColTitle = new TextView(this);
            meanCreditColTitle.setText("Mean");
            meanCreditColTitle.setLayoutParams(textViewLayoutParam);
            meanCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            gridLayout.addView(meanCreditColTitle);

            TextView lowestCreditColTitle = new TextView(this);
            lowestCreditColTitle.setText("Lowest");
            lowestCreditColTitle.setLayoutParams(textViewLayoutParam);
            lowestCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            gridLayout.addView(lowestCreditColTitle);

            TextView highestCreditColTitle = new TextView(this);
            highestCreditColTitle.setText("Highest");
            highestCreditColTitle.setLayoutParams(textViewLayoutParam);
            highestCreditColTitle.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
            gridLayout.addView(highestCreditColTitle);

            linearLayout2.addView(gridLayout);

            for (Entry entry : values.get(i)) {
                //@todo calculate averages
                TextView dateColumnTextView = new TextView(this);
                dateColumnTextView.setText(entry.getFormattedDate());
                dateColumnTextView.setLayoutParams(textViewLayoutParam);
                gridLayout.addView(dateColumnTextView);

                TextView totalColumnTextView = new TextView(this);
                totalColumnTextView.setText(String.valueOf(entry.getTotalCredit()));
                totalColumnTextView.setLayoutParams(textViewLayoutParam);
                gridLayout.addView(totalColumnTextView);

                TextView meanColumnTextView = new TextView(this);
                meanColumnTextView.setText(String.valueOf(entry.getMeanCredit()));
                meanColumnTextView.setLayoutParams(textViewLayoutParam);
                gridLayout.addView(meanColumnTextView);

                TextView lowestColumnTextView = new TextView(this);
                lowestColumnTextView.setText(String.valueOf(entry.getLowestCredit()));
                lowestColumnTextView.setLayoutParams(textViewLayoutParam);
                gridLayout.addView(lowestColumnTextView);

                TextView highestColumnTextView = new TextView(this);
                highestColumnTextView.setText(String.valueOf(entry.getHighestCredit()));
                highestColumnTextView.setLayoutParams(textViewLayoutParam);
                gridLayout.addView(highestColumnTextView);
            }

            i++;
        }

        linearLayout.addView(saveButton);

        this.addContentView(linearLayout, linearLayoutParams);
    }

    /**
     * Entry used to save the report data
     */
    public class Entry {
        private long date;
        private float totalCredit;
        private float meanCredit;
        private float lowestCredit;
        private float highestCredit;
        SimpleDateFormat sdf;

        public Entry(long date, float totalCredit, float meanCredit, float lowestCredit, float highestCredit) {
            this.date = date;
            this.totalCredit = totalCredit;
            this.meanCredit = meanCredit;
            this.lowestCredit = lowestCredit;
            this.highestCredit = highestCredit;

            sdf = new SimpleDateFormat("M/y");
        }

        public Date getDate() {
            return new Date(date);
        }

        public String getFormattedDate() {
            return sdf.format(this.getDate());
        }

        public float getTotalCredit() {
            return totalCredit;
        }

        public float getMeanCredit() {
            return meanCredit;
        }

        public float getLowestCredit() {
            return lowestCredit;
        }

        public float getHighestCredit() {
            return highestCredit;
        }
    }
}
