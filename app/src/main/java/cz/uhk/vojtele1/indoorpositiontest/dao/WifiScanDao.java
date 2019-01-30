package cz.uhk.vojtele1.indoorpositiontest.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.util.List;

@Dao
public interface WifiScanDao {
    @Query("SELECT * FROM wifiscan")
    LiveData<List<WifiScan>> getAll();

    @Insert
    void insertAll(List<WifiScan> wifiScans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(WifiScan wifiScan);

    @Delete
    void delete(WifiScan wifiScan);

    @Query("DELETE FROM wifiscan")
    void deleteAll();
}
