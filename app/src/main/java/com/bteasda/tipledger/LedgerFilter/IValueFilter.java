package com.bteasda.tipledger.LedgerFilter;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.stream.Stream;

public abstract class IValueFilter {
    abstract public Stream<LedgerEntryEntity> filter(Stream<LedgerEntryEntity> ledgerStream);
}
