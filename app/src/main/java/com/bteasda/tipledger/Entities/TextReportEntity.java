package com.bteasda.tipledger.Entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.bteasda.tipledger.Converters;

import java.text.SimpleDateFormat;
import java.util.Date;

@Entity(tableName="text_reports")
public class TextReportEntity {
    @PrimaryKey(autoGenerate = true)
    private int textReportId;

    @TypeConverters({Converters.class})
    private Date generatedDate;

    private String title;

    public TextReportEntity(Date generatedDate, String title) {
        this.generatedDate = generatedDate;
        this.title = title;
    }

    public int getTextReportId() {
        return this.textReportId;
    }

    public Date getGeneratedDate() {
        return this.generatedDate;
    }

    public String getGeneratedDateFormatted() {
        SimpleDateFormat sdf = new SimpleDateFormat("M/d/y hh:mm:ss");
        return sdf.format(this.getGeneratedDate());
    }

    public String getTitle() {
        return this.title;
    }

    public void setTextReportId(int textReportId) {
        this.textReportId = textReportId;
    }

    public void setGeneratedDate(Date generatedDate) {
        this.generatedDate = generatedDate;
    }

    public void setTitle(String title) { this.title = title;}
}
