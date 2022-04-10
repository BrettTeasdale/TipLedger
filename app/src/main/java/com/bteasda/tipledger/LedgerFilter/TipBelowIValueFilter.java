package com.bteasda.tipledger.LedgerFilter;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.stream.Stream;

public class TipBelowIValueFilter extends IValueFilter {
    private float amount;

    public TipBelowIValueFilter(float amount) {
        this.amount = amount;
    }

    @Override
    public Stream<LedgerEntryEntity> filter(Stream<LedgerEntryEntity> ledgerStream) {
        return ledgerStream.filter(ledgerEntity -> {
            return ledgerEntity.getCredit() < amount;
        });
    }
}
