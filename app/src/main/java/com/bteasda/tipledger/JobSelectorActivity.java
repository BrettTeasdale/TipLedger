package com.bteasda.tipledger;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.UI.JobListAdapter;
import com.bteasda.tipledger.UI.JobSelectorAdapter;
import com.bteasda.tipledger.ViewModels.JobViewModel;

import java.util.List;

public class JobSelectorActivity extends BaseActivity {
    /**
     * Initialize the JobSelectorActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_selector);

        loadJobViewModel();

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        RecyclerView jobListRecycleView = findViewById(R.id.jobListRecycleView);

        Bundle extras = getIntent().getExtras();

        Button allJobsButton = findViewById(R.id.allJobsButton);
        if(!extras.getBoolean("allowAll")) {
            allJobsButton.setVisibility(View.GONE);
        } else {
            allJobsButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.putExtra("jobId", 0);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            });
        }

        final JobSelectorAdapter adapter = new JobSelectorAdapter(this);
        jobListRecycleView.setAdapter(adapter);
        jobListRecycleView.setLayoutManager(new LinearLayoutManager(this));

        mJobViewModel.getAllJobs().observe(this, new Observer<List<JobEntity>>() {
            @Override
            public void onChanged(List<JobEntity> jobEntities) {
                adapter.setJobEntities(jobEntities);
            }
        });

    }
}
