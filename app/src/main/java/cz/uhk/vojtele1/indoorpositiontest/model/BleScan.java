package cz.uhk.vojtele1.indoorpositiontest.model;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class BleScan {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "rssi")
    private int rssi;
    @ColumnInfo(name = "timestamp")
    private long timestamp;
    @ColumnInfo(name = "x")
    private double x;
    @ColumnInfo(name = "y")
    private double y;

    public BleScan(String address, int rssi, long timestamp, double x, double y) {
        this.address = address;
        this.rssi = rssi;
        this.timestamp = timestamp;
        this.x = x;
        this.y = y;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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
}
