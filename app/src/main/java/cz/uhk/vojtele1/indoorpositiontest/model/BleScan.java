package cz.uhk.vojtele1.indoorpositiontest.model;

public class BleScan {
    private String address;
    private int rssi;
    private long time;

    public BleScan(String address, int rssi, long time) {
        this.address = address;
        this.rssi = rssi;
        this.time = time;
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

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "{" +
                "address='" + address + '\'' +
                ", rssi=" + rssi +
                ", time=" + time +
                '}';
    }
}
