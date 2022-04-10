package com.bteasda.tipledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteasda.tipledger.BaseActivity;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;
import com.bteasda.tipledger.R;
import com.bteasda.tipledger.UI.JobListAdapter;
import com.bteasda.tipledger.UI.SavedReportListAdapter;

import java.util.List;


public class TextReportSavedListActivity extends BaseActivity {

    private final int NEW_SAVED_REPORT_VIEW_ACTIVITY_REQUEST_CODE = 40;

    /**
     * Initialize the TextReportSavedListActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_report_list);

        loadTextReportViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView savedReportListRecycleView = findViewById(R.id.savedReportListRecycleView);

        final SavedReportListAdapter adapter = new SavedReportListAdapter(this);
        savedReportListRecycleView.setAdapter(adapter);
        savedReportListRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mTextReportViewModel.getAllTextReports().observe(this, new Observer<List<TextReportEntity>>() {
            @Override
            public void onChanged(List<TextReportEntity> textReportEntities) {
                adapter.setTextReportEntities(textReportEntities);
            }
        });
    }

}
