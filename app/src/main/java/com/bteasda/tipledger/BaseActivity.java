package com.bteasda.tipledger;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bteasda.tipledger.ViewModels.JobViewModel;
import com.bteasda.tipledger.ViewModels.LedgerEntryViewModel;
import com.bteasda.tipledger.ViewModels.TextReportDataViewModel;
import com.bteasda.tipledger.ViewModels.TextReportViewModel;

public class BaseActivity extends AppCompatActivity {
    protected JobViewModel mJobViewModel;
    protected LedgerEntryViewModel mLedgerEntryViewModel;
    protected TextReportViewModel mTextReportViewModel;
    protected TextReportDataViewModel mTextReportDataViewModel;

    /**
     * Initialize the BaseActivity activity
     *
     * @param savedInstanceState saved state Bundle for restoring activity state
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Load the job view model
     */
    protected void loadJobViewModel() {
        mJobViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(JobViewModel.class);
    }

    /**
     * Load the ledger entry view model
     */
    protected void loadLedgerEntryViewModel() {
        mLedgerEntryViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(LedgerEntryViewModel.class);
    }

    /**
     * Load the text report view model
     */
    protected void loadTextReportViewModel() {
        mTextReportViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(TextReportViewModel.class);
    }

    /**
     * Load the text report data view model
     */
    protected void loadTextReportDataViewModel() {
        mTextReportDataViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getApplication())).get(TextReportDataViewModel.class);
    }

}
