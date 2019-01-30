package cz.uhk.vojtele1.indoorpositiontest.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;
import cz.uhk.vojtele1.indoorpositiontest.repository.AppRepository;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private AppRepository repository;

    private LiveData<List<WifiScan>> wifiScans;
    private LiveData<List<BleScan>> bleScans;

    public AppViewModel (Application application) {
        super(application);
        repository = new AppRepository(application);
        wifiScans = repository.getAllWifiScan();
        bleScans = repository.getAllBleScan();
    }

    public LiveData<List<WifiScan>> getAllWifiScans() { return wifiScans; }
    public LiveData<List<BleScan>> getAllBleScans() { return bleScans; }

    public void insertWifiScan(WifiScan wifiScan) { repository.insertWifiScan(wifiScan); }
    public void insertBleScan(BleScan bleScan) { repository.insertBleScan(bleScan); }
}