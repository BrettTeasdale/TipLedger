package com.bteasda.tipledger.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bteasda.tipledger.Database.TipRepository;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;

import java.util.List;

public class TextReportViewModel extends AndroidViewModel {
    private TipRepository mRepository;

    public TextReportViewModel(Application application) {
        super(application);

        mRepository = new TipRepository(application);
    }

    public void insert(TextReportEntity textReportEntity) { mRepository.insert(textReportEntity); }

    public LiveData<TextReportEntity> getTextReportByTextReportId(int textReportId){
        return mRepository.getTextReportByTextReportId(textReportId);
    }

    public LiveData<List<TextReportEntity>> getAllTextReports(){
        return mRepository.getAllTextReports();
    }

    public LiveData<TextReportEntity> getLastTextReport() {
        return mRepository.getLastTextReport();
    }

    public void deleteTextReportByTextReportId(int textReportId) {
        mRepository.deleteTextReportByTextReportId(textReportId);
    }

}
