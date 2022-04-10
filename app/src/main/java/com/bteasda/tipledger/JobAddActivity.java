package com.bteasda.tipledger;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.bteasda.tipledger.Entities.JobEntity;

public class JobAddActivity extends BaseActivity {
    private EditText mNameEditText;
    private Button mAddJobButton;
    private JobEntity mJobEntity;

    /**
     * Initialize the JobAddActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_add);

        loadLedgerEntryViewModel();
        loadJobViewModel();

        mNameEditText = findViewById(R.id.nameEditText);
        mAddJobButton = findViewById(R.id.addJobButton);

        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            mJobViewModel.getJobByJobId(extras.getInt("jobId")).observe(this, new Observer<JobEntity>() {
                @Override
                public void onChanged(JobEntity jobEntity) {
                    if(jobEntity != null) {
                        mJobEntity = jobEntity;
                        mNameEditText.setText(mJobEntity.getName());
                        mAddJobButton.setText("Save Job");
                    }
                }
            });
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        mAddJobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mJobEntity == null) {
                    JobEntity newJob = new JobEntity(mNameEditText.getText().toString());
                    mJobViewModel.insert(newJob);
                } else {
                    mJobEntity.setName(mNameEditText.getText().toString());
                    mJobViewModel.insert(mJobEntity);
                }

                setResult(RESULT_OK);
                finish();
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        Bundle extras = getIntent().getExtras();

        if(extras != null) {
            getMenuInflater().inflate(R.menu.job_edit, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_job_delete:
                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                dialog.setMessage("Deleting this job will delete all it's data");
                dialog.setTitle("Are you sure you want to continue?");
                dialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mLedgerEntryViewModel.deleteLedgerEntryByJobId(mJobEntity.getJobId());
                        mJobViewModel.deleteJobByJobId(mJobEntity.getJobId());

                        Toast.makeText(getApplicationContext(), "The job and its related tip data was deleted.", Toast.LENGTH_SHORT).show();

                        setResult(RESULT_OK);
                        finish();
                    }
                });
                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {}
                });
                dialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
