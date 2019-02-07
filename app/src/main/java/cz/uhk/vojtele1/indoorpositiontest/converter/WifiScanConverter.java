package cz.uhk.vojtele1.indoorpositiontest.converter;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.vojtele1.indoorpositiontest.model.WifiScan;

import java.lang.reflect.Type;
import java.util.List;

public class WifiScanConverter {

    @TypeConverter
    public String fromWifiScanList(List<WifiScan> wifiScans) {
        if (wifiScans == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<WifiScan>>() {}.getType();
        return gson.toJson(wifiScans, type);
    }

    @TypeConverter
    public List<WifiScan> toWifiScanList(String wifiScansString) {
        if (wifiScansString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<WifiScan>>() {}.getType();
        return gson.fromJson(wifiScansString, type);
    }
}