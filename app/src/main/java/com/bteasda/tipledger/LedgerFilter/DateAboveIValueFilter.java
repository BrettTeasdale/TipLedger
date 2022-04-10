package com.bteasda.tipledger.LedgerFilter;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.Date;
import java.util.stream.Stream;

public class DateAboveIValueFilter extends IValueFilter {
    private Date addedDate;

    public DateAboveIValueFilter(Date addedDate) {
        this.addedDate = addedDate;
    }

    @Override
    public Stream<LedgerEntryEntity> filter(Stream<LedgerEntryEntity> ledgerStream) {
        return ledgerStream.filter(ledgerEntity -> {
            return ledgerEntity.getAddedDateTime().toInstant().toEpochMilli() > addedDate.toInstant().toEpochMilli();
        });
    }
}
