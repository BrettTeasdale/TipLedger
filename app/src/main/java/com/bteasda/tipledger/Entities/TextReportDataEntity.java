package com.bteasda.tipledger.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.bteasda.tipledger.Converters;

import java.util.Date;

@Entity(tableName="text_report_data")
public class TextReportDataEntity {
    @PrimaryKey(autoGenerate = true)
    private int textReportDataId;

    private String dateString;

    private int textReportId;
    private int jobId;
    private String jobTitle;

    private float total;
    private float mean;
    private float lowest;
    private float highest;

    public TextReportDataEntity(String dateString, int textReportId, int jobId, String jobTitle, float total, float mean, float lowest, float highest) {
        this.dateString = dateString;
        this.textReportId = textReportId;
        this.jobId = jobId;
        this.jobTitle = jobTitle;
        this.total = total;
        this.mean = mean;
        this.lowest = lowest;
        this.highest = highest;
    }

    public String getDateString() {
        return this.dateString;
    }

    public int getTextReportId() {
        return this.textReportId;
    }

    public int getTextReportDataId() {
        return this.textReportDataId;
    }

    public int getJobId() {
        return this.jobId;
    }

    public String getJobTitle() {
        return this.jobTitle;
    }

    public float getTotal() {
        return this.total;
    }

    public float getMean() {
        return this.mean;
    }

    public float getLowest() {
        return this.lowest;
    }

    public float getHighest() {
        return this.highest;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }

    public void setTextReportId(int textReportId) {
        this.textReportId = textReportId;
    }

    public void setTextReportDataId(int textReportId) {
        this.textReportId = textReportDataId;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public void setTotal(float total) {
        this.total = total;
    }

    public void setMean(float mean) {
        this.mean = mean;
    }

    public void setLowest(float lowest) {
        this.lowest = lowest;
    }

    public void setHighest(float highest) {
        this.highest = highest;
    }
}
