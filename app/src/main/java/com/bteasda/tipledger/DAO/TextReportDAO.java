package com.bteasda.tipledger.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;

import java.util.List;

@Dao
public interface TextReportDAO {
    /**
     * Insert a text_reports entry
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(TextReportEntity textReportEntity);

    /**
     * Get all text_reports
     * @return
     */
    @Query("SELECT * FROM text_reports")
    LiveData<List<TextReportEntity>> getAllTextReports();

    /**
     * Get text reports with a specific textReportId
     * @param textReportId
     * @return
     */
    @Query("SELECT * FROM text_reports where textReportId=:textReportId")
    LiveData<TextReportEntity> getTextReportByTextReportId(int textReportId);

    /**
     * Get a text report with a specific textReportId
     * @return
     */
    @Query("SELECT * FROM text_reports ORDER BY textReportId DESC")
    LiveData<TextReportEntity> getLastTextReport();

    /**
     * Delete a text report with a specfici textReportId
     * @param textReportId
     */
    @Query("DELETE FROM text_reports WHERE textReportId=:textReportId")
    void deleteTextReportByTextReportId(int textReportId);
}
