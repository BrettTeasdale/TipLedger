package com.bteasda.tipledger.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.TextReportDataEntity;

import java.util.List;

@Dao
public interface TextReportDataDAO {
    /**
     * Insert a text_report_data entry
     * @param textReportDataEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TextReportDataEntity textReportDataEntity);

    /**
     * Get all text_report_data entries with a specific textReportId
     * @param textReportId
     * @return
     */
    @Query("SELECT * FROM text_report_data WHERE textReportId=:textReportId")
    LiveData<List<TextReportDataEntity>> getAllTextReportDataByTextReportId(int textReportId);

    /**
     * Delete all text_report_data entries with a specific textReportId
     * @param textReportId
     */
    @Query("DELETE FROM text_report_data WHERE textReportId=:textReportId")
    void deleteTextReportDataByTextReportId(int textReportId);
}
