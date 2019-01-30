package cz.uhk.vojtele1.indoorpositiontest.utils;

import android.bluetooth.BluetoothAdapter;
import android.net.wifi.WifiManager;

public class Utils {

    /**
     * Zapne BT a Wifi pokud je aktivita aktivni. Pokud bylo BT nebo Wifi zaple, zustane zaple.
     *
     * @param enable jestli se ma bt a wifi zapnout/vypnout
     * @return true
     */
    public static boolean changeBTWifiState(boolean enable, WifiManager wm, BluetoothAdapter bluetoothAdapter, boolean wasWifiEnabled, boolean wasBTEnabled) {
        if (enable) {
            if (!wasBTEnabled && !wasWifiEnabled) {
                wm.setWifiEnabled(true);
                return bluetoothAdapter.enable();
            } else if (!wasBTEnabled) {
                return bluetoothAdapter.enable();
            } else
                return wasWifiEnabled || wm.setWifiEnabled(true);
        } else {
            if (!wasBTEnabled && !wasWifiEnabled) {
                wm.setWifiEnabled(false);
                return bluetoothAdapter.disable();
            } else if (!wasBTEnabled) {
                return bluetoothAdapter.disable();
            } else
                return wasWifiEnabled || wm.setWifiEnabled(false);
        }
    }
}
