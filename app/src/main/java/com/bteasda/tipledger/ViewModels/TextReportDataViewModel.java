package com.bteasda.tipledger.ViewModels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bteasda.tipledger.Database.TipRepository;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.TextReportDataEntity;

import java.util.List;

public class TextReportDataViewModel extends AndroidViewModel {
    private TipRepository mRepository;

    public TextReportDataViewModel(Application application) {
        super(application);

        mRepository = new TipRepository(application);
    }

    public void insert(TextReportDataEntity textReportDataEntity) { mRepository.insert(textReportDataEntity); }

    public LiveData<List<TextReportDataEntity>> getAllTextReportDataByTextReportId(int textReportId){
        return mRepository.getAllTextReportDataByTextReportId(textReportId);
    }

    public void deleteTextReportDataByTextReportId(int textReportId) {
        mRepository.deleteTextReportDataByTextReportId(textReportId);
    }

}
