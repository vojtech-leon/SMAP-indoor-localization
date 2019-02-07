package cz.uhk.vojtele1.indoorpositiontest.model;

public class WifiScan {
    private String ssid;
    private String mac;
    private int rssi;
    private long time;

    @Override
    public String toString() {
        return "{" +
                "ssid='" + ssid + '\'' +
                ", mac='" + mac + '\'' +
                ", rssi=" + rssi +
                ", time=" + time +
                '}';
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
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

    public WifiScan(String ssid, String mac, int rssi, long time) {
        this.ssid = ssid;
        this.mac = mac;
        this.rssi = rssi;
        this.time = time;
    }
}
