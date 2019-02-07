package cz.uhk.vojtele1.indoorpositiontest.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import cz.uhk.vojtele1.indoorpositiontest.dao.ScanDao;
import cz.uhk.vojtele1.indoorpositiontest.database.AppDatabase;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;

import java.util.List;

public class AppRepository {

    private ScanDao scanDao;
    private LiveData<List<Scan>> scansLive;
    private static LiveData<List<Scan>> scans;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        scanDao = db.scanDao();
        scansLive = scanDao.getAllLiveOwn();
        scans = scanDao.getAllLive();
    }

    public LiveData<List<Scan>> getAllOwnScansLive() {
        return scansLive;
    }

    public LiveData<List<Scan>> getAllScan() {
        return scans;
    }

    public void insertScan(Scan scan) {
        new insertAsyncTaskScan(scanDao).execute(scan);
    }

    public void deleteAllOwnScans() {
        new deleteAsyncTaskScans(scanDao).execute();
    }

    private static class insertAsyncTaskScan extends AsyncTask<Scan, Void, Void> {

        private ScanDao scanDao;

        insertAsyncTaskScan(ScanDao dao) {
            scanDao = dao;
        }

        @Override
        protected Void doInBackground(final Scan... params) {
            scanDao.insert(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTaskScans extends AsyncTask<Void, Void, Void> {

        private final ScanDao scanDao;

        deleteAsyncTaskScans(ScanDao dao) {
            scanDao = dao;
        }

        @Override
        protected Void doInBackground(final Void... voids) {
            scanDao.deleteAllOwn();
            return null;
        }
    }
}