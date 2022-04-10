package com.bteasda.tipledger.LedgerFilter;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Filter {
    List<LedgerEntryEntity> ledgerEntryEntities;
    List<IValueFilter> ledgerFilters;

    public Filter(List<LedgerEntryEntity> ledgerEntryEntities, List<IValueFilter> ledgerFilters) {
        this.ledgerEntryEntities = ledgerEntryEntities;
        this.ledgerFilters = ledgerFilters;
    }

    public List<LedgerEntryEntity> execute() {
        Stream ledgerStream = ledgerEntryEntities.stream();

        for(IValueFilter ledgerFilter : ledgerFilters) {
            ledgerStream = ledgerFilter.filter(ledgerStream);
        }

        return (List<LedgerEntryEntity>) ledgerStream.collect(Collectors.toList());
    }


}
