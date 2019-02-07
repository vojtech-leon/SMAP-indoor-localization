package cz.uhk.vojtele1.indoorpositiontest;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import cz.uhk.vojtele1.indoorpositiontest.utils.C;

public class SettingsActivity extends AppCompatActivity {

    private SharedPreferences sharedPreferences;

    private boolean isWifiScanningEnabled, isBLEScanningEnabled;

    private Switch wifiScanning, bleScanning;

    private SharedPreferences.Editor editor;

    private RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        Button btnScanList = findViewById(R.id.btnScanList);
        btnScanList.setOnClickListener(v -> startActivity(new Intent(this, ScanActivity.class)));
        ImageButton btnBack = findViewById(R.id.ibBackSettings);
        btnBack.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        sharedPreferences = getSharedPreferences(C.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        isWifiScanningEnabled = sharedPreferences.getBoolean(C.WIFI_SCANNING, false);
        isBLEScanningEnabled = sharedPreferences.getBoolean(C.BLE_SCANNING, false);

        wifiScanning = findViewById(R.id.switchWifi);
        bleScanning = findViewById(R.id.switchBLE);

        wifiScanning.setChecked(isWifiScanningEnabled);
        wifiScanning.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean(C.WIFI_SCANNING, isChecked);
            editor.apply();
        });

        bleScanning.setChecked(isBLEScanningEnabled);
        bleScanning.setOnCheckedChangeListener((buttonView, isChecked) -> {
            editor = sharedPreferences.edit();
            editor.putBoolean(C.BLE_SCANNING, isChecked);
            editor.apply();
        });

        radioGroup = findViewById(R.id.radioGroup);
        radioGroup.check(sharedPreferences.getInt(C.ALG, R.id.rbComb));
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            editor = sharedPreferences.edit();
            editor.putInt(C.ALG, checkedId);
            editor.apply();
        });
    }
}
