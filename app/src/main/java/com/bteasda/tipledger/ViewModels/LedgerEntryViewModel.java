package com.bteasda.tipledger.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bteasda.tipledger.Database.TipRepository;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.List;

public class LedgerEntryViewModel extends AndroidViewModel {
    private TipRepository mRepository;

    public LedgerEntryViewModel(Application application) {
        super(application);

        mRepository = new TipRepository(application);
    }

    public LiveData<List<LedgerEntryEntity>> getAllLedgerEntries(){
        return mRepository.getAllLedgerEntries();
    }

    public LiveData<List<LedgerEntryEntity>> getLedgerEntriesByJobId(int jobId){
        return mRepository.getLedgerEntriesByJobId(jobId);
    }

    public LiveData<LedgerEntryEntity> getLedgerEntryById(int ledgerEntryId) {
        return mRepository.getLedgerEntryById(ledgerEntryId);
    }

    public void insert(LedgerEntryEntity ledgerEntryEntity) { mRepository.insert(ledgerEntryEntity);}

    public void deleteLedgerEntryByJobId(int jobId) { mRepository.deleteLedgerEntryByJobId(jobId); }

    public void deleteLedgerEntryByLedgerEntryId(int ledgerEntryId) { mRepository.deleteLedgerEntryByLedgerEntryId(ledgerEntryId); }
}
