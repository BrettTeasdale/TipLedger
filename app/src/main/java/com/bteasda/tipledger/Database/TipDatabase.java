package com.bteasda.tipledger.Database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bteasda.tipledger.DAO.JobDAO;
import com.bteasda.tipledger.DAO.LedgerEntryDAO;
import com.bteasda.tipledger.DAO.TextReportDAO;
import com.bteasda.tipledger.DAO.TextReportDataDAO;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.Entities.TextReportDataEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;

@Database(entities = {LedgerEntryEntity.class, JobEntity.class, TextReportEntity.class, TextReportDataEntity.class},
        version = 2, exportSchema = false)
public abstract class TipDatabase extends RoomDatabase {
    public abstract LedgerEntryDAO ledgerEntryDAO();
    public abstract JobDAO jobDAO();
    public abstract TextReportDAO textReportDAO();
    public abstract TextReportDataDAO textReportDataDAO();

    private static volatile TipDatabase INSTANCE;

    static TipDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (TipDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(), TipDatabase.class, "tip_database")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

}
