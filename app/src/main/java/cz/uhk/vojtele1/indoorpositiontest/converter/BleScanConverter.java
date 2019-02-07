package cz.uhk.vojtele1.indoorpositiontest.converter;

import android.arch.persistence.room.TypeConverter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.vojtele1.indoorpositiontest.model.BleScan;

import java.lang.reflect.Type;
import java.util.List;

public class BleScanConverter {

    @TypeConverter
    public String fromBleScanList(List<BleScan> bleScans) {
        if (bleScans == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BleScan>>() {}.getType();
        return gson.toJson(bleScans, type);
    }

    @TypeConverter
    public List<BleScan> toBleScanList(String bleScansString) {
        if (bleScansString == null) {
            return (null);
        }
        Gson gson = new Gson();
        Type type = new TypeToken<List<BleScan>>() {}.getType();
        return gson.fromJson(bleScansString, type);
    }
}