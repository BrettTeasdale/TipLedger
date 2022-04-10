package com.bteasda.tipledger.DAO;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bteasda.tipledger.Entities.LedgerEntryEntity;

import java.util.List;

@Dao
public interface LedgerEntryDAO {
    /**
     * Insert a ledger entry into the ledger table
     * @param ledgerEntryEntity
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(LedgerEntryEntity ledgerEntryEntity);

    /**
     * Get all entries from the ledger table ordered by the data they were added
     * @return
     */
    @Query("SELECT * FROM ledger ORDER BY addedDateTime ASC")
    LiveData<List<LedgerEntryEntity>> getAllLedgerEntries();

    /**
     * Get all ledger entries with a specific job id
     * @param jobId
     * @return
     */
    @Query("SELECT * FROM ledger where jobId=:jobId")
    LiveData<List<LedgerEntryEntity>> getLedgerEntryByJobId(int jobId);

    /**
     * Get a specific ledger entry by ledgerEntryId
     * @param ledgerEntryId
     * @return
     */
    @Query("SELECT * FROM ledger where ledgerEntryId=:ledgerEntryId limit 1")
    LiveData<LedgerEntryEntity> getLedgerEntryById(int ledgerEntryId);

    /**
     * Delete all ledger entries from the ledger table with a specific jobId
     * @param jobId
     */
    @Query("DELETE FROM ledger where jobId=:jobId")
    void deleteLedgerEntryByJobId(int jobId);

    /**
     * Delete a ledger entry with a specific ledgerEntryId value
     * @param ledgerEntryId
     */
    @Query("DELETE FROM ledger where ledgerEntryId=:ledgerEntryId")
    void deleteLedgerEntryByLedgerEntryId(int ledgerEntryId);
}
