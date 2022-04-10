package com.bteasda.tipledger.Database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.bteasda.tipledger.DAO.JobDAO;
import com.bteasda.tipledger.DAO.LedgerEntryDAO;
import com.bteasda.tipledger.DAO.TextReportDAO;
import com.bteasda.tipledger.DAO.TextReportDataDAO;
import com.bteasda.tipledger.Entities.JobEntity;
import com.bteasda.tipledger.Entities.LedgerEntryEntity;
import com.bteasda.tipledger.Entities.TextReportDataEntity;
import com.bteasda.tipledger.Entities.TextReportEntity;

import java.util.List;

public class TipRepository {
    private LedgerEntryDAO mLedgerEntryDAO;
    private JobDAO mJobDAO;
    private TextReportDAO mTextReportDAO;
    private TextReportDataDAO mTextReportDataDAO;

    public TipRepository(Application application) {
        TipDatabase db = TipDatabase.getDatabase(application);

        mLedgerEntryDAO = db.ledgerEntryDAO();
        mJobDAO = db.jobDAO();
        mTextReportDAO = db.textReportDAO();
        mTextReportDataDAO = db.textReportDataDAO();
    }

    // Begin Ledger Queries

    public void insert(LedgerEntryEntity ledgerEntryEntity) {
        new InsertLedgerEntryAsyncTask(mLedgerEntryDAO).execute(ledgerEntryEntity);
    }

    private static class InsertLedgerEntryAsyncTask extends AsyncTask<LedgerEntryEntity, Void, Void> {

        private LedgerEntryDAO mAsyncTaskDao;

        InsertLedgerEntryAsyncTask(LedgerEntryDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final LedgerEntryEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public LiveData<List<LedgerEntryEntity>> getAllLedgerEntries() {
        return mLedgerEntryDAO.getAllLedgerEntries();
    }

    public LiveData<List<LedgerEntryEntity>> getLedgerEntriesByJobId(int jobId) {
        return mLedgerEntryDAO.getLedgerEntryByJobId(jobId);
    }

    public LiveData<LedgerEntryEntity> getLedgerEntryById(int ledgerEntryId) {
        return mLedgerEntryDAO.getLedgerEntryById(ledgerEntryId);
    }

    public void deleteLedgerEntryByJobId(int jobId) {
        new DeleteLedgerEntryByJobId(mLedgerEntryDAO).execute(jobId);
    }

    private static class DeleteLedgerEntryByJobId extends AsyncTask<Integer, Void, Void> {

        private LedgerEntryDAO mAsyncTaskDao;

        DeleteLedgerEntryByJobId(LedgerEntryDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteLedgerEntryByJobId(params[0]);
            return null;
        }
    }

    public void deleteLedgerEntryByLedgerEntryId(int ledgerEntryId) {
        new DeleteLedgerEntryByLedgerEntryID(mLedgerEntryDAO).execute(ledgerEntryId);
    }

    private static class DeleteLedgerEntryByLedgerEntryID extends AsyncTask<Integer, Void, Void> {

        private LedgerEntryDAO mAsyncTaskDao;

        DeleteLedgerEntryByLedgerEntryID(LedgerEntryDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteLedgerEntryByLedgerEntryId(params[0]);
            return null;
        }
    }

    // End Ledger Queries

    // Begin Job Queries

    public void insert(JobEntity jobEntity) {
        new InsertJobAsyncTask(mJobDAO).execute(jobEntity);
    }

    private static class InsertJobAsyncTask extends AsyncTask<JobEntity, Void, Void> {

        private JobDAO mAsyncTaskDao;

        InsertJobAsyncTask(JobDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final JobEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public LiveData<List<JobEntity>> getAllJobs() {
        return mJobDAO.getAllJobs();
    }

    public LiveData<JobEntity> getJobByJobId(int jobId) {
        return mJobDAO.getJobByJobId(jobId);
    }

    public void deleteJobByJobId(int jobId) {
        new DeleteJobByJobId(mJobDAO).execute(jobId);
    }

    private static class DeleteJobByJobId extends AsyncTask<Integer, Void, Void> {

        private JobDAO mAsyncTaskDao;

        DeleteJobByJobId(JobDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteJobByJobId(params[0]);
            return null;
        }
    }

    // End Job Queries

    // Begin Text Report Queries

    public void insert(TextReportEntity textReportEntity) {
        new InsertTextReportAsyncTask(mTextReportDAO).execute(textReportEntity);
    }

    private static class InsertTextReportAsyncTask extends AsyncTask<TextReportEntity, Void, Void> {

        private TextReportDAO mAsyncTaskDao;

        InsertTextReportAsyncTask(TextReportDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TextReportEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public LiveData<List<TextReportEntity>> getAllTextReports() {
        return mTextReportDAO.getAllTextReports();
    }

    public LiveData<TextReportEntity> getTextReportByTextReportId(int textReportId) {
        return mTextReportDAO.getTextReportByTextReportId(textReportId);
    }

    public LiveData<TextReportEntity> getLastTextReport() {
        return mTextReportDAO.getLastTextReport();
    }

    public void deleteTextReportByTextReportId(int textReportId) {
        new DeleteTextReportByTextReportId(mTextReportDAO).execute(textReportId);
    }

    private static class DeleteTextReportByTextReportId extends AsyncTask<Integer, Void, Void> {

        private TextReportDAO mAsyncTaskDao;

        DeleteTextReportByTextReportId(TextReportDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteTextReportByTextReportId(params[0]);
            return null;
        }
    }


    // End Text Report Queries

    // Begin Text Report Data Queries

    public void insert(TextReportDataEntity textReportDataEntity) {
        new InsertTextReportDataAsyncTask(mTextReportDataDAO).execute(textReportDataEntity);
    }

    private static class InsertTextReportDataAsyncTask extends AsyncTask<TextReportDataEntity, Void, Void> {

        private TextReportDataDAO mAsyncTaskDao;

        InsertTextReportDataAsyncTask(TextReportDataDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final TextReportDataEntity... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    public LiveData<List<TextReportDataEntity>> getAllTextReportDataByTextReportId(int textReportId) {
        return mTextReportDataDAO.getAllTextReportDataByTextReportId(textReportId);
    }


    public void deleteTextReportDataByTextReportId(int textReportId) {
        new DeleteTextReportDataByTextReportId(mTextReportDataDAO).execute(textReportId);
    }

    private static class DeleteTextReportDataByTextReportId extends AsyncTask<Integer, Void, Void> {

        private TextReportDataDAO mAsyncTaskDao;

        DeleteTextReportDataByTextReportId(TextReportDataDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Integer... params) {
            mAsyncTaskDao.deleteTextReportDataByTextReportId(params[0]);
            return null;
        }
    }

    // End Text Report Data Queries
}
