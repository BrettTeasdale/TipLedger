package com.bteasda.tipledger;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.LedgerFilter.DateAboveIValueFilter;
import com.bteasda.tipledger.LedgerFilter.DateBelowIValueFilter;
import com.bteasda.tipledger.LedgerFilter.IValueFilter;
import com.bteasda.tipledger.LedgerFilter.TipAboveIValueFilter;
import com.bteasda.tipledger.LedgerFilter.TipBelowIValueFilter;
import com.bteasda.tipledger.LedgerFilter.TipEqualsIValueFilter;

import org.junit.Assert;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.CoreMatchers.hasItems;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class FilterUnitTest {
    @Test
    public void tipAboveIValueFilter_isCorrect() {
        LedgerEntryEntity item1 = new LedgerEntryEntity(new Date(1618012800000L), 0, 0);
        LedgerEntryEntity item2 = new LedgerEntryEntity(new Date(1618012800000L), 1, 0);
        LedgerEntryEntity item3 = new LedgerEntryEntity(new Date(1618012800000L), 2, 0);
        LedgerEntryEntity item4 = new LedgerEntryEntity(new Date(1620691200000L), 1.11f, 0);
        LedgerEntryEntity item5 = new LedgerEntryEntity(new Date(1620691200000L), 2.22f, 0);
        LedgerEntryEntity item6 = new LedgerEntryEntity(new Date(1620691200000L), 50, 0);
        LedgerEntryEntity item7 = new LedgerEntryEntity(new Date(1623369600000L), 50.11f, 0);
        LedgerEntryEntity item8 = new LedgerEntryEntity(new Date(1623369600000L), 100, 0);
        LedgerEntryEntity item9 = new LedgerEntryEntity(new Date(1623369600000L), 100.10f, 0);
        LedgerEntryEntity item10 = new LedgerEntryEntity(new Date(1625961600000L), Float.MAX_VALUE, 0);

        List<LedgerEntryEntity> original = new ArrayList<LedgerEntryEntity>();
        original.add(item1);
        original.add(item2);
        original.add(item3);
        original.add(item4);
        original.add(item5);
        original.add(item6);
        original.add(item7);
        original.add(item8);
        original.add(item9);
        original.add(item10);

        IValueFilter filter = new TipAboveIValueFilter(50);
        List<LedgerEntryEntity> actual = filter.filter(original.stream()).collect(Collectors.toList());

        assertThat(actual, hasItems(item7, item8, item9, item10));
        assertThat(actual.size(), is(4));
    }

    @Test
    public void tipBelowIValueFilter_isCorrect() {
        LedgerEntryEntity item1 = new LedgerEntryEntity(new Date(1618012800000L), 0, 0);
        LedgerEntryEntity item2 = new LedgerEntryEntity(new Date(1618012800000L), 1, 0);
        LedgerEntryEntity item3 = new LedgerEntryEntity(new Date(1618012800000L), 2, 0);
        LedgerEntryEntity item4 = new LedgerEntryEntity(new Date(1620691200000L), 1.11f, 0);
        LedgerEntryEntity item5 = new LedgerEntryEntity(new Date(1620691200000L), 2.22f, 0);
        LedgerEntryEntity item6 = new LedgerEntryEntity(new Date(1620691200000L), 50, 0);
        LedgerEntryEntity item7 = new LedgerEntryEntity(new Date(1623369600000L), 50.11f, 0);
        LedgerEntryEntity item8 = new LedgerEntryEntity(new Date(1623369600000L), 100, 0);
        LedgerEntryEntity item9 = new LedgerEntryEntity(new Date(1623369600000L), 100.10f, 0);
        LedgerEntryEntity item10 = new LedgerEntryEntity(new Date(1625961600000L), Float.MAX_VALUE, 0);

        List<LedgerEntryEntity> original = new ArrayList<LedgerEntryEntity>();
        original.add(item1);
        original.add(item2);
        original.add(item3);
        original.add(item4);
        original.add(item5);
        original.add(item6);
        original.add(item7);
        original.add(item8);
        original.add(item9);
        original.add(item10);

        IValueFilter filter = new TipBelowIValueFilter(50);
        List<LedgerEntryEntity> actual = filter.filter(original.stream()).collect(Collectors.toList());

        assertThat(actual, hasItems(item1, item2, item3, item4, item5));
        assertThat(actual.size(), is(5));
    }

    @Test
    public void tipEqualsIValueFilter_isCorrect() {
        LedgerEntryEntity item1 = new LedgerEntryEntity(new Date(1618012800000L), 0, 0);
        LedgerEntryEntity item2 = new LedgerEntryEntity(new Date(1618012800000L), 1, 0);
        LedgerEntryEntity item3 = new LedgerEntryEntity(new Date(1618012800000L), 2, 0);
        LedgerEntryEntity item4 = new LedgerEntryEntity(new Date(1620691200000L), 1.11f, 0);
        LedgerEntryEntity item5 = new LedgerEntryEntity(new Date(1620691200000L), 2.22f, 0);
        LedgerEntryEntity item6 = new LedgerEntryEntity(new Date(1620691200000L), 50, 0);
        LedgerEntryEntity item7 = new LedgerEntryEntity(new Date(1623369600000L), 50.11f, 0);
        LedgerEntryEntity item8 = new LedgerEntryEntity(new Date(1623369600000L), 50.11f, 0);
        LedgerEntryEntity item9 = new LedgerEntryEntity(new Date(1623369600000L), 100.10f, 0);
        LedgerEntryEntity item10 = new LedgerEntryEntity(new Date(1625961600000L), Float.MAX_VALUE, 0);

        List<LedgerEntryEntity> original = new ArrayList<LedgerEntryEntity>();
        original.add(item1);
        original.add(item2);
        original.add(item3);
        original.add(item4);
        original.add(item5);
        original.add(item6);
        original.add(item7);
        original.add(item8);
        original.add(item9);
        original.add(item10);

        IValueFilter filter = new TipEqualsIValueFilter(50.11f);
        List<LedgerEntryEntity> actual = filter.filter(original.stream()).collect(Collectors.toList());

        assertThat(actual, hasItems(item7, item8));
        assertThat(actual.size(), is(2));
    }

    @Test
    public void dateAboveIValueFilter_isCorrect() {
        LedgerEntryEntity item1 = new LedgerEntryEntity(new Date(1618012800000L), 0, 0);
        LedgerEntryEntity item2 = new LedgerEntryEntity(new Date(1618012800000L), 1, 0);
        LedgerEntryEntity item3 = new LedgerEntryEntity(new Date(1618012800000L), 2, 0);
        LedgerEntryEntity item4 = new LedgerEntryEntity(new Date(1620691199999L), 1.11f, 0);
        LedgerEntryEntity item5 = new LedgerEntryEntity(new Date(1620691200000L), 2.22f, 0);
        LedgerEntryEntity item6 = new LedgerEntryEntity(new Date(1620691200001L), 50, 0);
        LedgerEntryEntity item7 = new LedgerEntryEntity(new Date(1623369600000L), 50.11f, 0);
        LedgerEntryEntity item8 = new LedgerEntryEntity(new Date(1623369600000L), 100, 0);
        LedgerEntryEntity item9 = new LedgerEntryEntity(new Date(1623369600000L), 100.10f, 0);
        LedgerEntryEntity item10 = new LedgerEntryEntity(new Date(1625961600000L), Float.MAX_VALUE, 0);

        List<LedgerEntryEntity> original = new ArrayList<LedgerEntryEntity>();
        original.add(item1);
        original.add(item2);
        original.add(item3);
        original.add(item4);
        original.add(item5);
        original.add(item6);
        original.add(item7);
        original.add(item8);
        original.add(item9);
        original.add(item10);

        IValueFilter filter = new DateBelowIValueFilter(new Date(1620691200000L));
        List<LedgerEntryEntity> actual = filter.filter(original.stream()).collect(Collectors.toList());

        assertThat(actual, hasItems(item1, item2, item3, item4));
        assertThat(actual.size(), is(4));
    }

    @Test
    public void dateBelowIValueFilter_isCorrect() {
        LedgerEntryEntity item1 = new LedgerEntryEntity(new Date(1618012800000L), 0, 0);
        LedgerEntryEntity item2 = new LedgerEntryEntity(new Date(1618012800000L), 1, 0);
        LedgerEntryEntity item3 = new LedgerEntryEntity(new Date(1618012800000L), 2, 0);
        LedgerEntryEntity item4 = new LedgerEntryEntity(new Date(1620691199999L), 1.11f, 0);
        LedgerEntryEntity item5 = new LedgerEntryEntity(new Date(1620691200000L), 2.22f, 0);
        LedgerEntryEntity item6 = new LedgerEntryEntity(new Date(1620691200001L), 50, 0);
        LedgerEntryEntity item7 = new LedgerEntryEntity(new Date(1623369600000L), 50.11f, 0);
        LedgerEntryEntity item8 = new LedgerEntryEntity(new Date(1623369600000L), 100, 0);
        LedgerEntryEntity item9 = new LedgerEntryEntity(new Date(1623369600000L), 100.10f, 0);
        LedgerEntryEntity item10 = new LedgerEntryEntity(new Date(1625961600000L), Float.MAX_VALUE, 0);

        List<LedgerEntryEntity> original = new ArrayList<LedgerEntryEntity>();
        original.add(item1);
        original.add(item2);
        original.add(item3);
        original.add(item4);
        original.add(item5);
        original.add(item6);
        original.add(item7);
        original.add(item8);
        original.add(item9);
        original.add(item10);

        IValueFilter filter = new DateAboveIValueFilter(new Date(1620691200000L));
        List<LedgerEntryEntity> actual = filter.filter(original.stream()).collect(Collectors.toList());

        assertThat(actual, hasItems(item6, item7, item8, item9, item10));
        assertThat(actual.size(), is(5));
    }
}