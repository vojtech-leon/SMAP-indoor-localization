package cz.uhk.vojtele1.indoorpositiontest.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;
import cz.uhk.vojtele1.indoorpositiontest.repository.AppRepository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private AppRepository repository;

    private LiveData<List<Scan>> scansOwnLive;

    private LiveData<List<Scan>> scans;

    public AppViewModel (Application application) {
        super(application);
        repository = new AppRepository(application);
        scansOwnLive = repository.getAllOwnScansLive();
        scans = repository.getAllScan();
    }

    public LiveData<List<Scan>> getAllOwnScansLive() {
        return scansOwnLive;
    }

    public LiveData<List<Scan>> getAllScans() {
        return scans;
    }

    public void insertScan(Scan scan) { repository.insertScan(scan); }

    public void deleteAllOwnScans() {
        repository.deleteAllOwnScans();
    }
}