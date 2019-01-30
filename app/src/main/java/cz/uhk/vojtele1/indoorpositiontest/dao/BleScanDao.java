package cz.uhk.vojtele1.indoorpositiontest.dao;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.*;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;

import java.util.List;

@Dao
public interface BleScanDao {
    @Query("SELECT * FROM blescan")
    LiveData<List<BleScan>> getAll();

    @Insert
    void insertAll(List<BleScan> bleScans);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BleScan bleScan);

    @Delete
    void delete(BleScan bleScan);

    @Query("DELETE FROM blescan")
    void deleteAll();
}
