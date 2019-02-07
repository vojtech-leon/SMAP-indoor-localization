package cz.uhk.vojtele1.indoorpositiontest.utils;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.le.ScanRecord;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.SystemClock;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Scanner {
    private Context context;

    private List<WifiScan> wifiScans = new ArrayList<>();
    private List<BleScan> bleScans = new ArrayList<>();

    private long startTime;
    /**
     * zda prave probiha sken
     */
    private boolean running;

    private WifiManager wm;
    private BluetoothAdapter bluetoothAdapter;
    private BroadcastReceiver wifiBroadcastReceiver;
    private Timer timer;
    private Handler handler;
    private BluetoothAdapter.LeScanCallback leScanCallback;

    private boolean isWifiBRRegistered, isBleBRRegistered;

    public Scanner(Context context) {
        this.context = context;
        init();
    }

    /**
     * Priprava broadcast receiveru, manazeru apod.
     */
    private void init() {
        wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        //receiver pro prijem naskenovanych wifi siti
        wifiBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                //System.out.println("Ziskal jsem wifiScany v case: " + (SystemClock.uptimeMillis() - startTime));
                wifiScans.addAll(convertWifiResults(wm.getScanResults()));
            }
        };
        leScanCallback = (device, rssi, scanRecord) -> {
            // System.out.println("Ziskal jsem ble rssi: " + rssi + " v case: " + (SystemClock.uptimeMillis() - startTime));
            bleScans.add(new BleScan(device.getAddress(), rssi, SystemClock.uptimeMillis() - startTime));
        };
    }

    private List<WifiScan> convertWifiResults(List<ScanResult> scanResults) {
        List<WifiScan> wifiScans = new ArrayList<>();
        for (ScanResult scan : scanResults) {
            WifiScan wifiScan = new WifiScan(scan.SSID, scan.BSSID, scan.level, SystemClock.uptimeMillis() - startTime);
            wifiScans.add(wifiScan);
        }
        return wifiScans;
    }

    /**
     *
     * @param time is s
     * @param wifi
     * @param ble
     * @param scanResultListener
     * @return
     */
    public boolean startScan(int time, boolean wifi, boolean ble, final ScanResultListener scanResultListener) {
        ble = ble && context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE); //vyradime ble pokud ho zarizeni nema.
        if (time == 0) time = 10;
        if (running) {
            return false; //pokud jeste nedobehlo probihajici skenovani (nebo problemy pri zapinani HW), NEstartuj nove a vrat false
        }
        running = true;


        wifiScans.clear();
        bleScans.clear();

        startTime = SystemClock.uptimeMillis(); //zaznamenej cas zacatku skenovani
        if (wifi) {
            //zaregistrovani receiveru pro wifi sken
            context.registerReceiver(wifiBroadcastReceiver, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
            isWifiBRRegistered = true;
            wm.startScan();
        }
        if (ble) {
            isBleBRRegistered = true;
            bluetoothAdapter.startLeScan(leScanCallback);
        }
        //casovac ukonceni skenovani
        timer = new Timer();
        timer.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (running && scanResultListener != null) {
                            scanResultListener.onScanFinished(wifiScans, bleScans);
                        }
                        stopScan();
                    }
                }, time * 1000);

        // neda se zapnout novy scan az po obdrzeni vysledku (zacne to poustet spoustu scanu), proto jsou scany pousteny kazdou vterinu
        handler = new Handler();
        int delay = 1000; //milliseconds

        boolean finalBle = ble;
        handler.postDelayed(new Runnable() {
            public void run() {
                if (wifi) {
                    wm.startScan();
                }
                if (finalBle) {
                    bluetoothAdapter.startLeScan(leScanCallback);
                }
                if (running) {
                    handler.postDelayed(this, delay);
                }
            }
        }, delay);
        return true;
    }

    public void stopScan() {
        if (!running) {
            return;
        }
        timer.cancel();

        if (isWifiBRRegistered) {
            context.unregisterReceiver(wifiBroadcastReceiver);
        }
        if (isBleBRRegistered) {
            bluetoothAdapter.stopLeScan(leScanCallback);
        }

        isWifiBRRegistered = false;
        isBleBRRegistered = false;
        running = false;
        handler.removeCallbacks(() -> {});
    }
}
