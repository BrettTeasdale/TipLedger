package com.bteasda.tipledger.LedgerFilter;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.stream.Stream;

public class TipEqualsIValueFilter extends IValueFilter {
    private float amount;

    public TipEqualsIValueFilter(float amount) {
        this.amount = amount;
    }

    @Override
    public Stream<LedgerEntryEntity> filter(Stream<LedgerEntryEntity> ledgerStream) {
        return ledgerStream.filter(ledgerEntity -> {
            return ledgerEntity.getCredit() == amount;
        });
    }
}
