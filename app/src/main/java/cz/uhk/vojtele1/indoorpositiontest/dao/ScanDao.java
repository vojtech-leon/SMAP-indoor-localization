package cz.uhk.vojtele1.indoorpositiontest.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;

import java.util.List;

@Dao
public interface ScanDao {
    @Query("SELECT * FROM scan")
    LiveData<List<Scan>> getAllLive();

    @Query("SELECT COUNT(*) FROM scan")
    int getSize();

    @Query("SELECT * FROM scan WHERE own = '1'")
    LiveData<List<Scan>> getAllLiveOwn();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Scan> Scans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Scan bleScan);

    @Query("DELETE FROM scan WHERE own = '1'")
    void deleteAllOwn();

    @Query("DELETE FROM scan")
    void deleteAll();
}
