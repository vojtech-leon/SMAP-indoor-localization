package cz.uhk.vojtele1.indoorpositiontest.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import cz.uhk.vojtele1.indoorpositiontest.dao.BleScanDao;
import cz.uhk.vojtele1.indoorpositiontest.dao.WifiScanDao;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

@Database(entities = {WifiScan.class, BleScan.class}, version = 2)
public abstract class AppDatabase extends RoomDatabase {
    public abstract WifiScanDao wifiScanDao();
    public abstract BleScanDao bleScanDao();

    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()

                            // doplní defaultní data napsaná dole / vymaže db
                             //  .addCallback(roomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDbAsync(INSTANCE).execute();
                }
            };

    /**
     * Populate the database in the background.
     */
    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final WifiScanDao wifiScanDao;

        PopulateDbAsync(AppDatabase db) {
            wifiScanDao = db.wifiScanDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            wifiScanDao.deleteAll();
            //wifiScanDao.insertWifiScan(new WifiScan("prvni", "mac", -50, 10000, 0, 0));
            return null;
        }
    }
}