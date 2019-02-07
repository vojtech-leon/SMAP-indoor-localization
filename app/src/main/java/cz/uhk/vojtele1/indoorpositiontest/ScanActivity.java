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
import cz.uhk.vojtele1.indoorpositiontest.adapter.ScanListAdapter;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;
import cz.uhk.vojtele1.indoorpositiontest.utils.C;
import cz.uhk.vojtele1.indoorpositiontest.utils.Scanner;
import cz.uhk.vojtele1.indoorpositiontest.utils.Utils;
import cz.uhk.vojtele1.indoorpositiontest.viewModel.AppViewModel;

public class ScanActivity extends AppCompatActivity {
    private static final int NEW_SCAN_REQUEST_CODE = 1;

    private AppViewModel appViewModel;
    private int x;
    private int y;
    private int time;
    private Scanner scanner;

    private ImageButton btnBack, btnDeleteAll;
    private SharedPreferences sharedPreferences;

    private boolean wasBTEnabled, wasWifiEnabled;
    private WifiManager wm;
    private BluetoothAdapter bluetoothAdapter;
    private boolean startingNewWifiBle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanlist);
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        FloatingActionButton fab = findViewById(R.id.fab);
        btnBack = findViewById(R.id.ibBackScanList);
        btnBack.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
        btnDeleteAll = findViewById(R.id.ibDeleteAllScans);
        btnDeleteAll.setOnClickListener(v -> appViewModel.deleteAllOwnScans());

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final ScanListAdapter scanListAdapter = new ScanListAdapter(this);
        recyclerView.setAdapter(scanListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Update the cached copy of the scans in the adapter.
        appViewModel.getAllOwnScansLive().observe(this, scanListAdapter::setScans);

        fab.setOnClickListener(view -> {
            Intent intent = new Intent(ScanActivity.this, NewWifiScanActivity.class);
            startingNewWifiBle = true;
            startActivityForResult(intent, NEW_SCAN_REQUEST_CODE);
        });
        scanner = new Scanner(this);
        sharedPreferences = getSharedPreferences(C.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_SCAN_REQUEST_CODE && resultCode == RESULT_OK) {
            if (data.getStringExtra(NewWifiScanActivity.EXTRA_X).matches("-?\\d+(.\\d*)?")) {
                x = Integer.parseInt(data.getStringExtra(NewWifiScanActivity.EXTRA_X));
            }
            if (data.getStringExtra(NewWifiScanActivity.EXTRA_Y).matches("-?\\d+(.\\d*)?")) {
                y = Integer.parseInt(data.getStringExtra(NewWifiScanActivity.EXTRA_Y));
            }
            if (data.getStringExtra(NewWifiScanActivity.EXTRA_TIME).matches("-?\\d+")) {
                time = Integer.parseInt(data.getStringExtra(NewWifiScanActivity.EXTRA_TIME));
            }
            boolean wifi = sharedPreferences.getBoolean(C.WIFI_SCANNING, false);
            boolean ble = sharedPreferences.getBoolean(C.BLE_SCANNING, false);
            scanner.startScan(time, wifi, ble, (wifiScans, bleScans) -> {
                appViewModel.insertScan(new Scan(wifiScans, bleScans, x, y, System.currentTimeMillis(), true));
            });
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (!startingNewWifiBle) {
            wasBTEnabled = bluetoothAdapter.isEnabled();
            wasWifiEnabled = wm.isWifiEnabled();
        }
        startingNewWifiBle = false;
        Utils.changeBTWifiState(true, wm, bluetoothAdapter, wasWifiEnabled, wasBTEnabled);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (!startingNewWifiBle) {
            Utils.changeBTWifiState(false, wm, bluetoothAdapter, wasWifiEnabled, wasBTEnabled);
        }
        scanner.stopScan();
    }
}
