package com.bteasda.tipledger.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName="jobs")
public class JobEntity {
    /**
     * The Primary Key of the table
     */
    @PrimaryKey(autoGenerate = true)
    private int jobId;

    /**
     * The name of the job
     */
    private String name;

    public JobEntity(String name) {
        this.name = name;
    }

    /**
     * Get the jobId of the job
     * @return the job Id
     */
    public int getJobId() {
        return this.jobId;
    }

    /**
     * Get the name of the job
     * @return the job name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Set the jobId of the job
     * @param jobId the job id
     */
    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    /**
     * Set the name of the job
     * @param name  the name of the job
     */
    public void setName(String name) {
        this.name = name;
    }
}
