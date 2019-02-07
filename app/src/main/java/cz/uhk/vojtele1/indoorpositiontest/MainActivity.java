package cz.uhk.vojtele1.indoorpositiontest;

import android.arch.lifecycle.ViewModelProviders;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.*;
import android.graphics.drawable.Drawable;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageButton;
import android.widget.ImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.vojtele1.indoorpositiontest.NN.CalculatePosition;
import cz.uhk.vojtele1.indoorpositiontest.NN.CalculateSignalDistance;
import cz.uhk.vojtele1.indoorpositiontest.NN.NearestNeighbor;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;
import cz.uhk.vojtele1.indoorpositiontest.utils.AlgType;
import cz.uhk.vojtele1.indoorpositiontest.utils.C;
import cz.uhk.vojtele1.indoorpositiontest.utils.Scanner;
import cz.uhk.vojtele1.indoorpositiontest.utils.Utils;
import cz.uhk.vojtele1.indoorpositiontest.viewModel.AppViewModel;

import java.util.*;

public class MainActivity extends AppCompatActivity {

    private ImageView imageView;
    private ImageButton settingsButton;
    private boolean wasBTEnabled, wasWifiEnabled;
    private WifiManager wm;
    private BluetoothAdapter bluetoothAdapter;
    private AppViewModel appViewModel;
    private List<Scan> scansDB = new ArrayList<>();
    private Scanner scanner;
    private SharedPreferences sharedPreferences;
    boolean wifi, ble;

    private int k = 2;
    private int time = 10; // sec
    private CalculateSignalDistance csd;
    private CalculatePosition cp;
    private Scan newScan = null;
    private boolean running = false;

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
        appViewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        appViewModel.getAllScans().observe(this, scansFromLive -> scansDB.addAll(scansFromLive));

        scanner = new Scanner(this);
        sharedPreferences = getSharedPreferences(C.SHARED_PREFERENCES_NAME, MODE_PRIVATE);
        csd = new CalculateSignalDistance();
        cp = new CalculatePosition(csd);
    }

    @Override
    protected void onResume() {
        super.onResume();
        wasBTEnabled = bluetoothAdapter.isEnabled();
        wasWifiEnabled = wm.isWifiEnabled();
        Utils.changeBTWifiState(true, wm, bluetoothAdapter, wasWifiEnabled, wasBTEnabled);
        wifi = sharedPreferences.getBoolean(C.WIFI_SCANNING, false);
        ble = sharedPreferences.getBoolean(C.BLE_SCANNING, false);
        running = true;
        scanRepeater();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.changeBTWifiState(false, wm, bluetoothAdapter, wasWifiEnabled, wasBTEnabled);
        running = false;
    }

    private void doScan() {
        scanner.startScan(time, wifi, ble, (wifiScans, bleScans) -> {
            newScan = new Scan(wifiScans, bleScans, 0, 0, System.currentTimeMillis(), true);
            Map<String, Integer> signals;
            AlgType algType;
            if (sharedPreferences.getInt(C.ALG, 0) == R.id.rbWifi) {
                signals = newScan.getWifiSignals();
                algType = AlgType.WIFI;
            } else if (sharedPreferences.getInt(C.ALG, 0) == R.id.rbBle) {
                signals = newScan.getBleSignals();
                algType = AlgType.BLE;
            } else {
                signals = newScan.getCombinedSignals();
                algType = AlgType.COMBINED;
            }
            NearestNeighbor nn = cp.calculatePosition(signals, scansDB, k, algType, true);
            showPosition(nn);
        });
    }

    private void scanRepeater() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                doScan();
                if (running) {
                    handler.postDelayed(this, 1000);
                }
            }
        }, 1000);
    }

    /**
     * https://stackoverflow.com/questions/18520287/draw-a-circle-on-an-existing-image
     * @param nn
     */
    private void showPosition(NearestNeighbor nn) {
        runOnUiThread(() -> {
            BitmapFactory.Options myOptions = new BitmapFactory.Options();
            myOptions.inDither = true;
            myOptions.inScaled = false;
            myOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;// important
            myOptions.inPurgeable = true;

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.j3np, myOptions);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setColor(Color.BLUE);

            Bitmap workingBitmap = Bitmap.createBitmap(bitmap);
            Bitmap mutableBitmap = workingBitmap.copy(Bitmap.Config.ARGB_8888, true);

            Canvas canvas = new Canvas(mutableBitmap);
            canvas.drawCircle((float) nn.getX(), (float) nn.getY(), 25, paint);

            imageView.setAdjustViewBounds(true);
            imageView.setImageBitmap(mutableBitmap);

        });

    }
}
