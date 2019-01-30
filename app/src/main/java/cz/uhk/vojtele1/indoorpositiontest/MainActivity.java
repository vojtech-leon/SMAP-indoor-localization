package cz.uhk.vojtele1.indoorpositiontest;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import cz.uhk.vojtele1.indoorpositiontest.utils.Utils;


public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageButton settingsButton;
    private boolean wasBTEnabled, wasWifiEnabled;
    private WifiManager wm;
    private BluetoothAdapter bluetoothAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
        settingsButton = findViewById(R.id.imageButton);
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Drawable drawable;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            drawable = getResources().getDrawable(R.drawable.j3np, getTheme());
        } else {
            drawable = getResources().getDrawable(R.drawable.j3np);
        }
        imageView.setImageDrawable(drawable);

        settingsButton.setOnClickListener(v -> startActivity(new Intent(this, SettingsActivity.class)));
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
