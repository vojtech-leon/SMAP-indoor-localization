package cz.uhk.vojtele1.indoorpositiontest.repository;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import cz.uhk.vojtele1.indoorpositiontest.dao.BleScanDao;
import cz.uhk.vojtele1.indoorpositiontest.dao.WifiScanDao;
import cz.uhk.vojtele1.indoorpositiontest.database.AppDatabase;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.util.List;

public class AppRepository {

    private WifiScanDao wifiScanDao;
    private BleScanDao bleScanDao;
    private LiveData<List<WifiScan>> wifiScans;
    private LiveData<List<BleScan>> bleScans;

    public AppRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        wifiScanDao = db.wifiScanDao();
        bleScanDao = db.bleScanDao();
        wifiScans = wifiScanDao.getAll();
        bleScans = bleScanDao.getAll();
    }

    public LiveData<List<WifiScan>> getAllWifiScan() {
        return wifiScans;
    }

    public LiveData<List<BleScan>> getAllBleScan() {
        return bleScans;
    }

    public void insertWifiScan(WifiScan wifiScan) {
        new insertAsyncTaskWifiScan(wifiScanDao).execute(wifiScan);
    }

    public void insertBleScan(BleScan bleScan) {
        new insertAsyncTaskBleScan(bleScanDao).execute(bleScan);
    }

    private static class insertAsyncTaskWifiScan extends AsyncTask<WifiScan, Void, Void> {

        private WifiScanDao wifiScanDao;

        insertAsyncTaskWifiScan(WifiScanDao dao) {
            wifiScanDao = dao;
        }

        @Override
        protected Void doInBackground(final WifiScan... params) {
            wifiScanDao.insert(params[0]);
            return null;
        }
    }

    private static class insertAsyncTaskBleScan extends AsyncTask<BleScan, Void, Void> {

        private BleScanDao bleScanDao;

        insertAsyncTaskBleScan(BleScanDao dao) {
            bleScanDao = dao;
        }

        @Override
        protected Void doInBackground(final BleScan... params) {
            bleScanDao.insert(params[0]);
            return null;
        }
    }
}