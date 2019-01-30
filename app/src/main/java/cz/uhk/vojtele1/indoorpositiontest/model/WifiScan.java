package cz.uhk.vojtele1.indoorpositiontest.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class WifiScan {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "ssid")
    private String ssid;
    @ColumnInfo(name = "bssid")
    private String bssid;
    @ColumnInfo(name = "rssi")
    private int rssi;
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    @ColumnInfo(name = "x")
    private double x;
    @ColumnInfo(name = "y")
    private double y;

    @Override
    public String toString() {
        return "WifiScan{" +
                "id=" + id +
                ", ssid='" + ssid + '\'' +
                ", bssid='" + bssid + '\'' +
                ", rssi=" + rssi +
                ", timestamp=" + timestamp +
                ", x=" + x +
                ", y=" + y +
                '}';
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getBssid() {
        return bssid;
    }

    public void setBssid(String bssid) {
        this.bssid = bssid;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public WifiScan(String ssid, String bssid, int rssi, long timestamp, double x, double y) {
        this.ssid = ssid;
        this.bssid = bssid;
        this.rssi = rssi;
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
    }
}
