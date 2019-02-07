package cz.uhk.vojtele1.indoorpositiontest.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;
import cz.uhk.vojtele1.indoorpositiontest.converter.BleScanConverter;
import cz.uhk.vojtele1.indoorpositiontest.converter.WifiScanConverter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Entity
public class Scan {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @TypeConverters(WifiScanConverter.class)
    @ColumnInfo(name = "wifiscans")
    private List<WifiScan> wifiScans;
    @TypeConverters(BleScanConverter.class)
    @ColumnInfo(name = "blescans")
    private List<BleScan> bleScans;
    @ColumnInfo(name = "x")
    private int x;
    @ColumnInfo(name = "y")
    private int y;
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    @ColumnInfo(name = "own")
    private boolean own;

    @Override
    public String toString() {
        return "{" +
                "wifiScans=" + wifiScans +
                ", bleScans=" + bleScans +
                ", x=" + x +
                ", y=" + y +
                ", timestamp=" + timestamp +
                ", own=" + own +
                '}';
    }

    public Scan(List<WifiScan> wifiScans, List<BleScan> bleScans, int x, int y, long timestamp, boolean own) {
        this.wifiScans = wifiScans;
        this.bleScans = bleScans;
        this.x = x;
        this.y = y;
        this.timestamp = timestamp;
        this.own = own;
    }

    public List<WifiScan> getWifiScans() {
        return wifiScans;
    }

    public void setWifiScans(List<WifiScan> wifiScans) {
        this.wifiScans = wifiScans;
    }

    public List<BleScan> getBleScans() {
        return bleScans;
    }

    public void setBleScans(List<BleScan> bleScans) {
        this.bleScans = bleScans;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isOwn() {
        return own;
    }

    public void setOwn(boolean own) {
        this.own = own;
    }

    public Map<String, Integer> getWifiSignals() {
        Map<String, Integer> signalsSum = new HashMap<>();
        Map<String, Integer> eachSignalCounter = new HashMap<>();

        for (WifiScan wifiScan : wifiScans) {
          if (signalsSum.containsKey(wifiScan.getMac())) {
              signalsSum.put(wifiScan.getMac(), wifiScan.getRssi() + signalsSum.get(wifiScan.getMac()));
              eachSignalCounter.put(wifiScan.getMac(), eachSignalCounter.get(wifiScan.getMac()) + 1);
          } else {
              signalsSum.put(wifiScan.getMac(), wifiScan.getRssi());
              eachSignalCounter.put(wifiScan.getMac(), 1);
          }
        }
        Map<String, Integer> signals = new HashMap<>();
        for (Map.Entry<String, Integer> entry : signalsSum.entrySet()) {
            signals.put(entry.getKey(), entry.getValue() / eachSignalCounter.get(entry.getKey()));
        }
        return signals;
    }

    public Map<String, Integer> getBleSignals() {
        Map<String, Integer> signalsSum = new HashMap<>();
        Map<String, Integer> eachSignalCounter = new HashMap<>();

        for (BleScan bleScan : bleScans) {
            if (signalsSum.containsKey(bleScan.getAddress())) {
                signalsSum.put(bleScan.getAddress(), bleScan.getRssi() + signalsSum.get(bleScan.getAddress()));
                eachSignalCounter.put(bleScan.getAddress(), eachSignalCounter.get(bleScan.getAddress()) + 1);
            } else {
                signalsSum.put(bleScan.getAddress(), bleScan.getRssi());
                eachSignalCounter.put(bleScan.getAddress(), 1);
            }
        }
        Map<String, Integer> signals = new HashMap<>();
        for (Map.Entry<String, Integer> entry : signalsSum.entrySet()) {
            signals.put(entry.getKey(), entry.getValue() / eachSignalCounter.get(entry.getKey()));
        }
        return signals;
    }

    public Map<String, Integer> getCombinedSignals() {
        Map<String, Integer> signals = new HashMap<>();
        signals.putAll(getWifiSignals());
        signals.putAll(getBleSignals());
        return signals;
    }
}
