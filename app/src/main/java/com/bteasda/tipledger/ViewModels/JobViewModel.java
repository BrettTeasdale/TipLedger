package com.bteasda.tipledger.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bteasda.tipledger.Database.TipRepository;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.List;

public class JobViewModel extends AndroidViewModel {
    private TipRepository mRepository;

    public JobViewModel(Application application) {
        super(application);

        mRepository = new TipRepository(application);
    }

    public void insert(JobEntity jobEntity) { mRepository.insert(jobEntity); }

    public LiveData<List<JobEntity>> getAllJobs(){
        return mRepository.getAllJobs();
    }

    public LiveData<JobEntity> getJobByJobId(int jobId){
        return mRepository.getJobByJobId(jobId);
    }

    public void deleteJobByJobId(int jobId){
        mRepository.deleteJobByJobId(jobId);
    }
}
