package com.bteasda.tipledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.LedgerFilter.Filter;
import com.bteasda.tipledger.LedgerFilter.FilterFactory;
import com.bteasda.tipledger.UI.LedgerAdapter;

import java.util.Date;
import java.util.List;

public class LedgerViewActivity extends BaseActivity {
    private static final int NEW_FILTER_DIALOGUE_ACTIVITY_REQUEST_CODE = 21;
    LedgerAdapter adapter;

    private boolean filterEnabled;

    private boolean enableDateAboveFilter;
    private boolean enableDateBelowFilter;
    private boolean enableTipAboveFilter;
    private boolean enableTipBelowFilter;
    private boolean enableTipEqualsFilter;

    private Date dateAboveFilterValue;
    private Date dateBelowFilterValue;
    private float tipAboveFilterValue;
    private float tipBelowFilterValue;
    private float tipEqualsFilterValue;

    /**
     * Initialize the LedgerViewActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_view);

        loadJobViewModel();
        loadLedgerEntryViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView ledgerRecycleView = findViewById(R.id.ledgerRecycleView);

        adapter = new LedgerAdapter(this);

        ledgerRecycleView.setAdapter(adapter);
        ledgerRecycleView.setLayoutManager(new LinearLayoutManager(this));

        fetchData();
    }

    /**
     * Fetch the ledger data
     */
    public void fetchData() {
        Bundle extras = getIntent().getExtras();

        LiveData query;

        if(extras.getInt("jobId") == 0) {
            query = mLedgerEntryViewModel.getAllLedgerEntries();
        } else {
            query = mLedgerEntryViewModel.getLedgerEntriesByJobId(extras.getInt("jobId"));
        }

        query.observe(this, new Observer<List<LedgerEntryEntity>>() {
            @Override
            public void onChanged(List<LedgerEntryEntity> ledgerEntryEntities) {
                if(filterEnabled) {
                    FilterFactory.FactoryConfig factoryConfig = new FilterFactory.FactoryConfig();

                    factoryConfig.setEnableDateAboveFilter(enableDateAboveFilter);
                    factoryConfig.setEnableDateBelowFilter(enableDateBelowFilter);
                    factoryConfig.setEnableTipAboveFilter(enableTipAboveFilter);
                    factoryConfig.setEnableTipBelowFilter(enableTipBelowFilter);
                    factoryConfig.setEnableTipEqualsFilter(enableTipEqualsFilter);

                    factoryConfig.setDateAboveFilterValue(dateAboveFilterValue);
                    factoryConfig.setDateBelowFilterValue(dateBelowFilterValue);
                    factoryConfig.setTipAboveFilterValue(tipAboveFilterValue);
                    factoryConfig.setTipBelowFilterValue(tipBelowFilterValue);
                    factoryConfig.setTipEqualsFilterValue(tipEqualsFilterValue);

                    Filter filter = FilterFactory.make(ledgerEntryEntities, factoryConfig);
                    ledgerEntryEntities = filter.execute();
                }

                adapter.setLedgerEntries(ledgerEntryEntities);
            }
        });

        mJobViewModel.getAllJobs().observe(this, new Observer<List<JobEntity>>() {
            @Override
            public void onChanged(List<JobEntity> jobEntities) {
                adapter.setJobEntities(jobEntities);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.ledger_view_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_edit_filters:
                Intent intent = new Intent(LedgerViewActivity.this, FilterDialogueActivity.class);
                intent.putExtra("enableDateAboveFilter", enableDateAboveFilter);
                intent.putExtra("enableDateBelowFilter", enableDateBelowFilter);
                intent.putExtra("enableTipAboveFilter", enableTipAboveFilter);
                intent.putExtra("enableTipBelowFilter", enableTipBelowFilter);
                intent.putExtra("enableTipEqualsFilter", enableTipEqualsFilter);

                intent.putExtra("dateAboveFilterValue", dateAboveFilterValue != null ? dateAboveFilterValue.toInstant().toEpochMilli() : 0);
                intent.putExtra("dateBelowFilterValue", dateBelowFilterValue != null ? dateBelowFilterValue.toInstant().toEpochMilli() : 0);
                intent.putExtra("tipAboveFilterValue", tipAboveFilterValue);
                intent.putExtra("tipBelowFilterValue", tipBelowFilterValue);
                intent.putExtra("tipEqualsFilterValue", tipEqualsFilterValue);
                startActivityForResult(intent, NEW_FILTER_DIALOGUE_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == NEW_FILTER_DIALOGUE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            filterEnabled = true;

            enableDateAboveFilter = data.getBooleanExtra("enableDateAboveFilter", false);
            enableDateBelowFilter = data.getBooleanExtra("enableDateBelowFilter", false);
            enableTipAboveFilter = data.getBooleanExtra("enableTipAboveFilter", false);
            enableTipBelowFilter = data.getBooleanExtra("enableTipBelowFilter", false);
            enableTipEqualsFilter = data.getBooleanExtra("enableTipEqualsFilter", false);

            dateAboveFilterValue = new Date(data.getLongExtra("dateAboveFilterValue", 0));
            dateBelowFilterValue = new Date(data.getLongExtra("dateBelowFilterValue", 0));
            tipAboveFilterValue =  data.getFloatExtra("tipAboveFilterValue", 0);
            tipBelowFilterValue =  data.getFloatExtra("tipBelowFilterValue", 0);
            tipEqualsFilterValue =  data.getFloatExtra("tipEqualsFilterValue", 0);

            fetchData();
        }
    }
}
