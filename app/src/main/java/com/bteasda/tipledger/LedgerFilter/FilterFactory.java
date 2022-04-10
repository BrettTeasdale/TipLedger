package com.bteasda.tipledger.LedgerFilter;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FilterFactory {
    public static Filter make(List<LedgerEntryEntity> ledgerEntryEntities, FactoryConfig factoryConfig) {
        ArrayList<IValueFilter> ledgerFilters = new ArrayList();

        if(factoryConfig.getEnableDateAboveFilter()) {
            ledgerFilters.add(new DateAboveIValueFilter(factoryConfig.getDateAboveFilterValue()));
        }

        if(factoryConfig.getEnableDateBelowFilter()) {
            ledgerFilters.add(new DateBelowIValueFilter(factoryConfig.getDateBelowFilterValue()));
        }

        if(factoryConfig.getEnableTipAboveFilter()) {
            ledgerFilters.add(new TipAboveIValueFilter(factoryConfig.getTipAboveFilterValue()));
        }

        if(factoryConfig.getEnableTipBelowFilter()) {
            ledgerFilters.add(new TipBelowIValueFilter(factoryConfig.getTipBelowFilterValue()));
        }

        if(factoryConfig.getEnableTipEqualsFilter()) {
            ledgerFilters.add(new TipEqualsIValueFilter(factoryConfig.getTipEqualsFilterValue()));
        }

        Filter filter = new Filter(ledgerEntryEntities, ledgerFilters);
        return filter;
    }

    public static class FactoryConfig {
        private boolean enableDateAboveFilter;
        private boolean enableDateBelowFilter;
        private boolean enableTipAboveFilter;
        private boolean enableTipBelowFilter;
        private boolean enableTipEqualsFilter;

        private Date dateAboveFilterValue;
        private Date dateBelowFilterValue;
        private float tipAboveFilterValue;
        private float tipBelowFilterValue;
        private float tipEqualsFilterValue;

        public void setEnableDateAboveFilter(boolean enableDateAboveFilter) {
            this.enableDateAboveFilter = enableDateAboveFilter;
        }

        public void setEnableDateBelowFilter(boolean enableDateBelowFilter) {
            this.enableDateBelowFilter = enableDateBelowFilter;
        }

        public void setEnableTipAboveFilter(boolean enableTipAboveFilter) {
            this.enableTipAboveFilter = enableTipAboveFilter;
        }

        public void setEnableTipBelowFilter(boolean enableTipBelowFilter) {
            this.enableTipBelowFilter = enableTipBelowFilter;
        }

        public void setEnableTipEqualsFilter(boolean enableTipEqualsFilter) {
            this.enableTipEqualsFilter = enableTipEqualsFilter;
        }

        public void setDateAboveFilterValue(Date dateAboveFilterValue) {
            this.dateAboveFilterValue = dateAboveFilterValue;
        }

        public void setDateBelowFilterValue(Date dateBelowFilterValue) {
            this.dateBelowFilterValue = dateBelowFilterValue;
        }

        public void setTipAboveFilterValue(float tipAboveFilterValue) {
            this.tipAboveFilterValue = tipAboveFilterValue;
        }

        public void setTipBelowFilterValue(float tipBelowFilterValue) {
            this.tipBelowFilterValue = tipBelowFilterValue;
        }

        public void setTipEqualsFilterValue(float tipEqualsFilterValue) {
            this.tipEqualsFilterValue = tipEqualsFilterValue;
        }

        public boolean getEnableDateAboveFilter() {
            return enableDateAboveFilter;
        }

        public boolean getEnableDateBelowFilter() {
            return enableDateBelowFilter;
        }

        public boolean getEnableTipAboveFilter() {
            return enableTipAboveFilter;
        }

        public boolean getEnableTipBelowFilter() {
            return enableTipBelowFilter;
        }

        public boolean getEnableTipEqualsFilter() {
            return enableTipEqualsFilter;
        }

        public Date getDateAboveFilterValue() {
            return dateAboveFilterValue;
        }

        public Date getDateBelowFilterValue() {
            return dateBelowFilterValue;
        }

        public float getTipAboveFilterValue() {
            return tipAboveFilterValue;
        }

        public float getTipBelowFilterValue() {
            return tipBelowFilterValue;
        }

        public float getTipEqualsFilterValue() {
            return tipEqualsFilterValue;
        }

    }
}
