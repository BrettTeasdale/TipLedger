package com.bteasda.tipledger.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.List;

@Dao
public interface JobDAO {
    /**
     * Insert a job into the jobs table
     * @param jobEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(JobEntity jobEntity);

    /**
     * Get all Jobs from the jobs table
     * @return
     */
    @Query("SELECT * FROM jobs")
    LiveData<List<JobEntity>> getAllJobs();

    /**
     * Get all jobs with a specific jobId from the jobs table
     * @param jobId
     * @return
     */
    @Query("SELECT * FROM jobs where jobId=:jobId")
    LiveData<JobEntity> getJobByJobId(int jobId);

    /**
     * Delete all jobs with a specific jobId from the jobs table
     * @param jobId
     */
    @Query("DELETE FROM jobs where jobId=:jobId")
    void deleteJobByJobId(int jobId);
}
