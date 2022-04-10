package com.bteasda.tipledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;
import com.bteasda.tipledger.UI.JobListAdapter;
import com.bteasda.tipledger.UI.SavedReportListAdapter;
import com.bteasda.tipledger.ViewModels.JobViewModel;

import java.util.List;

public class JobListActivity extends BaseActivity {
    private final int NEW_JOB_ADD_ACTIVITY_REQUEST_CODE = 50;

    /**
     * Initialize the JobListActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_list);

        loadJobViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView jobListRecycleViewer = findViewById(R.id.jobListRecycleView);

        final JobListAdapter adapter = new JobListAdapter(this);
        jobListRecycleViewer.setAdapter(adapter);
        jobListRecycleViewer.setLayoutManager(new LinearLayoutManager(this));

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
        getMenuInflater().inflate(R.menu.job_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_job_add:
                Intent intent = new Intent(JobListActivity.this, JobAddActivity.class);
                startActivityForResult(intent, NEW_JOB_ADD_ACTIVITY_REQUEST_CODE);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
