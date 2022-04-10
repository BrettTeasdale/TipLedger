package com.bteasda.tipledger;

import androidx.room.TypeConverter;

import java.util.Date;

public class Converters {
    @TypeConverter
    /**
     * Convert epoch milli to Date
     *
     * @return Date the date object created from the epoch milli
     */
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    /**
     * Convert Date to epoch milli
     * @param Date the Date to be converted
     * @return long the epoch milli of the date
     */
    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        if (date == null) {
            return null;
        } else {
            return date.getTime();
        }
    }
}
