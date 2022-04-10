package com.bteasda.tipledger.Entities;


import android.widget.SimpleExpandableListAdapter;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.bteasda.tipledger.Converters;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Entity(tableName="ledger")
public class LedgerEntryEntity {
    @PrimaryKey(autoGenerate = true)
    private int ledgerEntryId;

    @TypeConverters({Converters.class})
    private Date addedDateTime;

    private float credit;

    private int jobId;

    public LedgerEntryEntity(Date addedDateTime, float credit, int jobId) {
        this.addedDateTime = addedDateTime;
        this.credit = credit;
        this.jobId = jobId;
    }

    public int getLedgerEntryId() {
        return this.ledgerEntryId;
    }

    public Date getAddedDateTime() {
        return this.addedDateTime;
    }

    public String getAddedDateTimeFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/y hh:ss");
        return sdf.format(this.getAddedDateTime());
    }

    public float getCredit() {
        return this.credit;
    }

    public int getJobId() {
        return this.jobId;
    }

    public void setLedgerEntryId(int ledgerEntryId) {
        this.ledgerEntryId = ledgerEntryId;
    }

    public void setAddedDateTime(Date addedDateTime) {
        this.addedDateTime = addedDateTime;
    }

    public void setCredit(float credit) {
        this.credit = credit;
    }

    public void setJobId(int jobId) {
        this.jobId = jobId;
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }

        if (object == null || getClass() != object.getClass()) {
            return false;
        }

        LedgerEntryEntity that = (LedgerEntryEntity) object;
        return this.getCredit() == that.getCredit() &&
               this.getAddedDateTime().equals(that.getAddedDateTime()) &&
               this.getJobId() == that.getJobId();
    }
}
