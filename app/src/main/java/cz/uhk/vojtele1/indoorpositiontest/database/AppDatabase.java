package cz.uhk.vojtele1.indoorpositiontest.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.vojtele1.indoorpositiontest.dao.ScanDao;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;

import java.io.*;
import java.util.List;

@Database(entities = {Scan.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ScanDao scanDao();

    private static AppDatabase INSTANCE;

    private static Context context;

    public static AppDatabase getDatabase(final Context cont) {
        context = cont;
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "database")
                            // Wipes and rebuilds instead of migrating
                            // if no Migration object.
                            // Migration is not part of this practical.
                            .fallbackToDestructiveMigration()

                            // vymaže vše v db
                             //  .addCallback(roomDatabaseCallback)
                            .addCallback(populateDatabaseCallback)
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
                    new ClearDb(INSTANCE).execute();
                }
            };

    private static RoomDatabase.Callback populateDatabaseCallback =
            new RoomDatabase.Callback() {

                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                    new PopulateDb(INSTANCE).execute();
                }
            };

    private static class ClearDb extends AsyncTask<Void, Void, Void> {

        private final ScanDao scanDao;

        ClearDb(AppDatabase db) {
            scanDao = db.scanDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            scanDao.deleteAll();
            return null;
        }
    }

    private static class PopulateDb extends AsyncTask<Void, Void, Void> {

        private final ScanDao scanDao;

        PopulateDb(AppDatabase db) {
            scanDao = db.scanDao();
        }

        @Override
        protected Void doInBackground(final Void... params) {
            if (scanDao.getSize() == 0) {
                System.out.println("prazdna db, plnim daty");
                Gson gson = new Gson();
                AssetManager assetManager = context.getApplicationContext().getAssets();
                try {
                    Reader reader = new InputStreamReader(assetManager.open("populateDB.txt"));
                    scanDao.insertAll(gson.fromJson(reader, new TypeToken<List<Scan>>(){}.getType()));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
    }
}