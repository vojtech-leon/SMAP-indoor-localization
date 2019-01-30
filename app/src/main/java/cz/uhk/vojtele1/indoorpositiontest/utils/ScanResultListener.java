package cz.uhk.vojtele1.indoorpositiontest.utils;

import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.util.List;

public interface ScanResultListener {
    void onScanFinished(List<WifiScan> wifiScans, List<BleScan> bleScans);
}
