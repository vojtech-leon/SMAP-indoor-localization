package cz.uhk.vojtele1.indoorpositiontest;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageButton;
import android.widget.Switch;
import cz.uhk.vojtele1.indoorpositiontest.adapter.BleScanListAdapter;
import cz.uhk.vojtele1.indoorpositiontest.adapter.WifiScanListAdapter;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;
import cz.uhk.vojtele1.indoorpositiontest.utils.C;
import cz.uhk.vojtele1.indoorpositiontest.utils.Scanner;
import cz.uhk.vojtele1.indoorpositiontest.utils.Utils;
import cz.uhk.vojtele1.indoorpositiontest.viewModel.AppViewModel;

public class ScanActivity extends AppCompatActivity {
    private static final int NEW_SCAN_REQUEST_CODE = 1;

    private AppViewModel appViewModel;
    private double x;
    private double y;
    private int time;
    private Scanner scanner;

    private ImageButton btnBack;
    private SharedPreferences sharedPreferences;

    private boolean wasBTEnabled, wasWifiEnabled;
    private WifiManager wm;
    private BluetoothAdapter bluetoothAdapter;

    private Switch switchWifiBle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanlist);
        switchWifiBle = findViewById(R.id.switchBleWifi);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        btnBack = findViewById(R.id.ibBackScanList);
        btnBack.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final WifiScanListAdapter wifiScanListAdapter = new WifiScanListAdapter(this);
        final BleScanListAdapter bleScanListAdapter = new BleScanListAdapter(this);
        recyclerView.setAdapter(wifiScanListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Update the cached copy of the words in the adapter.
        appViewModel.getAllWifiScans().observe(this, wifiScanListAdapter::setWifiScans);
        appViewModel.getAllBleScans().observe(this, bleScanListAdapter::setBleScans);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ScanActivity.this, NewWifiScanActivity.class);
            startActivityForResult(intent, NEW_SCAN_REQUEST_CODE);
        });
        scanner = new Scanner(this);
        sharedPreferences = getSharedPreferences(C.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        switchWifiBle.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (!isChecked) {
                switchWifiBle.setText(R.string.wifi);
                recyclerView.setAdapter(wifiScanListAdapter);
            } else {
                switchWifiBle.setText(R.string.ble);
                recyclerView.setAdapter(bleScanListAdapter);
            }
        });
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SCAN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getStringExtra(NewWifiScanActivity.EXTRA_X).matches("-?\\d+.\\d+")) {
                x = Double.parseDouble(data.getStringExtra(NewWifiScanActivity.EXTRA_X));
            }
            if (data.getStringExtra(NewWifiScanActivity.EXTRA_Y).matches("-?\\d+.\\d+")) {
                y = Double.parseDouble(data.getStringExtra(NewWifiScanActivity.EXTRA_Y));
            }
            if (data.getStringExtra(NewWifiScanActivity.EXTRA_TIME).matches("-?\\d+")) {
                time = Integer.parseInt(data.getStringExtra(NewWifiScanActivity.EXTRA_TIME));
            }
            boolean wifi = sharedPreferences.getBoolean(C.WIFI_SCANNING, false);
            boolean ble = sharedPreferences.getBoolean(C.BLE_SCANNING, false);
            scanner.startScan(time, wifi, ble, (wifiScans, bleScans) -> {
                for (WifiScan wifiScan : wifiScans) {
                    appViewModel.insertWifiScan(wifiScan);
                }
                for (BleScan bleScan : bleScans) {
                    appViewModel.insertBleScan(bleScan);
                }
            }, x, y);
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        wasBTEnabled = bluetoothAdapter.isEnabled();
        wasWifiEnabled = wm.isWifiEnabled();
        Utils.changeBTWifiState(true, wm, bluetoothAdapter, wasWifiEnabled, wasBTEnabled);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.changeBTWifiState(false, wm, bluetoothAdapter, wasWifiEnabled, wasBTEnabled);
    }
}
