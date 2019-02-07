package cz.uhk.vojtele1.indoorpositiontest;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import cz.uhk.vojtele1.indoorpositiontest.NN.CalculateSignalDistance;
import cz.uhk.vojtele1.indoorpositiontest.NN.NearestNeighbor;
import cz.uhk.vojtele1.indoorpositiontest.model.Scan;
import cz.uhk.vojtele1.indoorpositiontest.utils.AlgType;
import org.junit.Test;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TestKnownPosition {
    public static final String  ASSET_BASE_PATH = "../app/src/main/assets/";
    private static double floorPixelsToMeters = 45.0/2250.0; // 45m = 2250 pixels // FIM J
    private boolean weightedMode = true;
    private List<Scan> scansDB = new ArrayList<>();
    public TestKnownPosition() {
        Gson gson = new Gson();
        try {
            Reader reader = new InputStreamReader(new FileInputStream(ASSET_BASE_PATH + "populateDB.txt"));
            scansDB.addAll(gson.fromJson(reader, new TypeToken<List<Scan>>(){}.getType()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * metoda obsahující všechny testy
     */
    @Test
    public void tests() {

     //   getAllDistances();

       // testSingleScan();
    }

    public void getAllDistances() {
        List<List<Double>> allScansDistancesWifi = new ArrayList<>();
        List<List<Double>> allScansDistancesBle = new ArrayList<>();
        List<List<Double>> allScansDistancesComb = new ArrayList<>();

        for (Scan scan : scansDB) {
            allScansDistancesWifi.add(getRealDistances(scan, AlgType.WIFI));
            allScansDistancesBle.add(getRealDistances(scan, AlgType.BLE));
            allScansDistancesComb.add(getRealDistances(scan, AlgType.COMBINED));
        }
        try {
            FileWriter fileWriter = new FileWriter("distancesWifi.json");
            fileWriter.write(new Gson().toJson(allScansDistancesWifi));
            fileWriter.close();
            fileWriter = new FileWriter("distancesBle.json");
            fileWriter.write(new Gson().toJson(allScansDistancesBle));
            fileWriter.close();
            fileWriter = new FileWriter("distancesComb.json");
            fileWriter.write(new Gson().toJson(allScansDistancesComb));
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * obsahuje upravenou metodu z CalculatePosition pro testování
     */
    @Test
    public void testSingleScan() {

        String testData1 = "{\"wifiScans\":[{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:af\",\"rssi\":-76,\"time\":4565},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:ae\",\"rssi\":-83,\"time\":4567},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:a0\",\"rssi\":-80,\"time\":4567},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:a1\",\"rssi\":-81,\"time\":4567},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:60\",\"rssi\":-59,\"time\":4568},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:6f\",\"rssi\":-49,\"time\":4568},{\"ssid\":\"hkfree.org\",\"mac\":\"00:25:45:24:7e:b1\",\"rssi\":-87,\"time\":4568},{\"ssid\":\"eduroam\",\"mac\":\"00:25:45:24:7e:b0\",\"rssi\":-86,\"time\":4568},{\"ssid\":\"eduroam\",\"mac\":\"00:24:14:3a:bb:b0\",\"rssi\":-87,\"time\":4569},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:ae\",\"rssi\":-83,\"time\":9570},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:a0\",\"rssi\":-80,\"time\":9571},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:a1\",\"rssi\":-76,\"time\":9571},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:60\",\"rssi\":-63,\"time\":9571},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:6f\",\"rssi\":-50,\"time\":9571},{\"ssid\":\"hkfree.org\",\"mac\":\"00:25:45:24:7e:b1\",\"rssi\":-88,\"time\":9572},{\"ssid\":\"eduroam\",\"mac\":\"00:25:45:24:7e:b0\",\"rssi\":-88,\"time\":9572},{\"ssid\":\"eduroam\",\"mac\":\"00:24:14:3a:bb:b0\",\"rssi\":-89,\"time\":9573},{\"ssid\":\"eduroam\",\"mac\":\"00:21:a0:f9:54:c0\",\"rssi\":-89,\"time\":9573},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:50\",\"rssi\":-89,\"time\":9573}],\"bleScans\":[{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-77,\"time\":1833},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-85,\"time\":4376},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-87,\"time\":4460},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-85,\"time\":4465},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-85,\"time\":4592},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-79,\"time\":5297},{\"address\":\"FD:1D:71:EC:33:60\",\"rssi\":-77,\"time\":5537},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-77,\"time\":5609},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-77,\"time\":5828},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-86,\"time\":5842},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-77,\"time\":6664},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-78,\"time\":6764},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-77,\"time\":6897},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-76,\"time\":6966},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":7176},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":7282},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":7397},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":7499},{\"address\":\"FD:1D:71:EC:33:60\",\"rssi\":-79,\"time\":7548},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":7704},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":7819},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-73,\"time\":7821},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-76,\"time\":7906},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-78,\"time\":7928},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":8037},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-79,\"time\":8044},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":8134},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-79,\"time\":8146},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":8217},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-79,\"time\":8251},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":8335},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-78,\"time\":8357},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":8423},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-95,\"time\":8455},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-77,\"time\":8459},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":8539},{\"address\":\"F5:B8:49:90:E9:F7\",\"rssi\":-97,\"time\":8607},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":8643},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":8749},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":8847},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":8953},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-75,\"time\":8990},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-76,\"time\":9055},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":9175},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":9272},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":9378},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-75,\"time\":9467},{\"address\":\"FD:1D:71:EC:33:60\",\"rssi\":-77,\"time\":9549},{\"address\":\"D1:98:1D:B8:77:29\",\"rssi\":-74,\"time\":9580},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-73,\"time\":9619},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-74,\"time\":9703},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-74,\"time\":9805}],\"x\":1600,\"y\":750,\"timestamp\":1456475729000,\"own\":false}";
        String testData2 = "{\"wifiScans\":[{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:21\",\"rssi\":-88,\"time\":5175},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:20\",\"rssi\":-87,\"time\":5175},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d9:60\",\"rssi\":-71,\"time\":5176},{\"ssid\":\"\",\"mac\":\"b4:75:0e:c3:fa:f6\",\"rssi\":-74,\"time\":5176},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d9:6f\",\"rssi\":-69,\"time\":5176},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d2:d1\",\"rssi\":-86,\"time\":5177},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d2:d0\",\"rssi\":-88,\"time\":5178}],\"bleScans\":[{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-94,\"time\":205},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":205},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":312},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-93,\"time\":313},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-66,\"time\":516},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-64,\"time\":704},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-66,\"time\":809},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":915},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-96,\"time\":1289},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-62,\"time\":1342},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-62,\"time\":1444},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-95,\"time\":1492},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-94,\"time\":1600},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-62,\"time\":1749},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":2074},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-94,\"time\":2590},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-64,\"time\":2910},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-96,\"time\":3014},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-65,\"time\":3652},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-95,\"time\":3658},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-64,\"time\":3729},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":3833},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":3935},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-96,\"time\":3977},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-96,\"time\":4005},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-95,\"time\":4944},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-62,\"time\":4978},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-61,\"time\":5079},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-61,\"time\":5390},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":5816},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-62,\"time\":5915},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-96,\"time\":6617},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-97,\"time\":6792},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-94,\"time\":7131},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-94,\"time\":7234},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-97,\"time\":7531},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":7577},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-64,\"time\":7663},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":7868},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-95,\"time\":7976},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":7978},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-94,\"time\":8397},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":8496},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":8604},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-98,\"time\":8690},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-99,\"time\":9321},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-64,\"time\":9651},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-63,\"time\":9767},{\"address\":\"C2:95:26:4E:65:D4\",\"rssi\":-64,\"time\":9868}],\"x\":750,\"y\":2250,\"timestamp\":1456478491000,\"own\":false}";
        String testData3 = "{\"wifiScans\":[{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:21\",\"rssi\":-61,\"time\":5058},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:20\",\"rssi\":-58,\"time\":5058},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:2e\",\"rssi\":-76,\"time\":5058},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:2f\",\"rssi\":-77,\"time\":5058},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:80\",\"rssi\":-83,\"time\":5058}],\"bleScans\":[{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-82,\"time\":360},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-73,\"time\":482},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-84,\"time\":493},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-79,\"time\":563},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-84,\"time\":583},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-73,\"time\":592},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-73,\"time\":785},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-85,\"time\":785},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-84,\"time\":989},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-70,\"time\":993},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-72,\"time\":1113},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-73,\"time\":1215},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":1298},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":1403},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-79,\"time\":1488},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-88,\"time\":1520},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-79,\"time\":1609},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-90,\"time\":1700},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-69,\"time\":1711},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-84,\"time\":1920},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-72,\"time\":1920},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-86,\"time\":2020},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-67,\"time\":2024},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-68,\"time\":2125},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-93,\"time\":2210},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-84,\"time\":2233},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-67,\"time\":2234},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-81,\"time\":2312},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-93,\"time\":2313},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-66,\"time\":2349},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-80,\"time\":2432},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-90,\"time\":2514},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-80,\"time\":2534},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-80,\"time\":2643},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-67,\"time\":2654},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-94,\"time\":2725},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-79,\"time\":2744},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-67,\"time\":2761},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-95,\"time\":2827},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":2845},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-95,\"time\":2949},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":2963},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-79,\"time\":3067},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-77,\"time\":3173},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-77,\"time\":3274},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":3377},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":3497},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-66,\"time\":3511},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":3580},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":3684},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-77,\"time\":3786},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-95,\"time\":3786},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-67,\"time\":3806},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-96,\"time\":4201},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":4205},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-94,\"time\":4303},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":4324},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":4526},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-94,\"time\":4730},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":4747},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":4837},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-95,\"time\":4939},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-66,\"time\":4971},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":5055},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-64,\"time\":5386},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-65,\"time\":5489},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":5490},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":5588},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-65,\"time\":5590},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":5694},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-65,\"time\":5695},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-66,\"time\":5810},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-97,\"time\":5869},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":5913},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":6017},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-66,\"time\":6019},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-76,\"time\":6119},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":6326},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":6430},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":6535},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-99,\"time\":6620},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":6638},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":6738},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-92,\"time\":6824},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":6846},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-73,\"time\":6961},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-77,\"time\":7063},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-77,\"time\":7168},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7187},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-73,\"time\":7270},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7288},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-90,\"time\":7356},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-77,\"time\":7376},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7397},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7493},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":7580},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-62,\"time\":7600},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-89,\"time\":7664},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":7685},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7701},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-88,\"time\":7769},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":7785},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7821},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-88,\"time\":7877},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-78,\"time\":7889},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":7909},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-63,\"time\":8042},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-94,\"time\":8097},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":8112},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-66,\"time\":8131},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":8197},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-94,\"time\":8200},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":8300},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-70,\"time\":8505},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-70,\"time\":8833},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-96,\"time\":9038},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":9039},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-94,\"time\":9167},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":9263},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":9362},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-97,\"time\":9471},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-75,\"time\":9474},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":9501},{\"address\":\"DF:55:9F:F1:B4:FF\",\"rssi\":-74,\"time\":9708},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-97,\"time\":9796},{\"address\":\"DA:06:AB:63:78:C5\",\"rssi\":-67,\"time\":9819}],\"x\":450,\"y\":1700,\"timestamp\":1456477444000,\"own\":false}";
        String testData4 = "{\"wifiScans\":[{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:21\",\"rssi\":-79,\"time\":4463},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:20\",\"rssi\":-80,\"time\":4463},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:2e\",\"rssi\":-84,\"time\":4463},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:2f\",\"rssi\":-85,\"time\":4464},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:00\",\"rssi\":-87,\"time\":4464},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:01\",\"rssi\":-88,\"time\":4464},{\"ssid\":\"CEPSOS\",\"mac\":\"24:a4:3c:b2:59:0b\",\"rssi\":-89,\"time\":4464},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:21\",\"rssi\":-79,\"time\":8983},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:20\",\"rssi\":-79,\"time\":8984},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:e7:2e\",\"rssi\":-85,\"time\":8984},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:e7:2f\",\"rssi\":-86,\"time\":8984},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:00\",\"rssi\":-87,\"time\":8985},{\"ssid\":\"CEPSOS\",\"mac\":\"24:a4:3c:b2:59:0b\",\"rssi\":-89,\"time\":8985}],\"bleScans\":[{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-68,\"time\":2074},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-92,\"time\":4735},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-73,\"time\":6176},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-74,\"time\":6374},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-74,\"time\":6467},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-73,\"time\":6584},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-74,\"time\":6684},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-93,\"time\":6722},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-73,\"time\":6797},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-73,\"time\":6890},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-74,\"time\":7010},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-77,\"time\":7102},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-77,\"time\":7200},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-78,\"time\":7320},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-78,\"time\":7421},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-82,\"time\":8072},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-81,\"time\":8176},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-79,\"time\":8481},{\"address\":\"FB:A8:E3:75:67:AE\",\"rssi\":-79,\"time\":8584},{\"address\":\"C1:C2:C2:30:7E:37\",\"rssi\":-95,\"time\":8741}],\"x\":450,\"y\":1050,\"timestamp\":1456477063000,\"own\":false}";
        String testData5 = "{\"wifiScans\":[{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:af\",\"rssi\":-51,\"time\":4644},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:ae\",\"rssi\":-50,\"time\":4644},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:a0\",\"rssi\":-61,\"time\":4645},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:a1\",\"rssi\":-61,\"time\":4645},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d9:60\",\"rssi\":-88,\"time\":4645},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:60\",\"rssi\":-90,\"time\":4645},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:50\",\"rssi\":-83,\"time\":4645},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:7f\",\"rssi\":-86,\"time\":4645},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:70\",\"rssi\":-82,\"time\":4646},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:51\",\"rssi\":-85,\"time\":4646},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d2:d0\",\"rssi\":-88,\"time\":4646},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d2:d1\",\"rssi\":-88,\"time\":4646},{\"ssid\":\"\",\"mac\":\"b4:75:0e:c3:fa:f6\",\"rssi\":-87,\"time\":4646},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:af\",\"rssi\":-51,\"time\":9824},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:ae\",\"rssi\":-51,\"time\":9824},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:a0\",\"rssi\":-62,\"time\":9824},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:a1\",\"rssi\":-62,\"time\":9825},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d9:60\",\"rssi\":-83,\"time\":9825},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:60\",\"rssi\":-90,\"time\":9825},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:50\",\"rssi\":-83,\"time\":9825},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:7f\",\"rssi\":-82,\"time\":9826},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d3:70\",\"rssi\":-83,\"time\":9826},{\"ssid\":\"hkfree.org\",\"mac\":\"00:1a:e3:d2:d3:51\",\"rssi\":-85,\"time\":9826},{\"ssid\":\"eduroam\",\"mac\":\"00:1a:e3:d2:d2:d0\",\"rssi\":-89,\"time\":9826},{\"ssid\":\"\",\"mac\":\"b4:75:0e:c3:fa:f6\",\"rssi\":-82,\"time\":9826}],\"bleScans\":[{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-68,\"time\":1704},{\"address\":\"D3:37:BE:D1:6A:85\",\"rssi\":-58,\"time\":1977},{\"address\":\"CA:91:06:B0:83:A7\",\"rssi\":-81,\"time\":2247},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":4911},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":5051},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-88,\"time\":5133},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":5150},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-90,\"time\":5234},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":5255},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":5353},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-89,\"time\":5782},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-67,\"time\":5981},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-86,\"time\":6610},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-87,\"time\":6710},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-74,\"time\":6712},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-87,\"time\":6812},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-75,\"time\":6813},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-68,\"time\":6848},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-65,\"time\":6947},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-65,\"time\":7016},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-91,\"time\":7136},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-65,\"time\":7137},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-92,\"time\":7137},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-64,\"time\":7152},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-90,\"time\":7238},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":7239},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":7340},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-92,\"time\":7341},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":7446},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-64,\"time\":7483},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-66,\"time\":7552},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-89,\"time\":7649},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-65,\"time\":7679},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-89,\"time\":7746},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-90,\"time\":7851},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-89,\"time\":7955},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-89,\"time\":8178},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-65,\"time\":8398},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-65,\"time\":8482},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-91,\"time\":8486},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-90,\"time\":8580},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-64,\"time\":8598},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-91,\"time\":8681},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-63,\"time\":8701},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-64,\"time\":8804},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-64,\"time\":8840},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-64,\"time\":8906},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-92,\"time\":8908},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-63,\"time\":9038},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-92,\"time\":9108},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-62,\"time\":9110},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-93,\"time\":9210},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-63,\"time\":9230},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-64,\"time\":9258},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-63,\"time\":9327},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-64,\"time\":9430},{\"address\":\"CE:FD:39:A0:CF:D5\",\"rssi\":-64,\"time\":9549},{\"address\":\"D3:37:BE:D1:6A:85\",\"rssi\":-59,\"time\":9750},{\"address\":\"E2:B0:39:C5:EB:44\",\"rssi\":-92,\"time\":9751},{\"address\":\"D2:6E:DB:29:FF:AA\",\"rssi\":-64,\"time\":9974}],\"x\":1750,\"y\":1900,\"timestamp\":1456474800000,\"own\":false}";

        Scan scan1 = new Gson().fromJson(testData5, new TypeToken<Scan>(){}.getType());

        scan1 = scansDB.get(2);
        //      Scan scan2 = gson.fromJson(testData2, new TypeToken<Scan>(){}.getType());
//        System.out.println(csd.calculateDistance(scan1.getWifiSignals(), scan2.getWifiSignals()));
   /*     Map<Integer, Double> neighbors = new HashMap<>();

        for (Scan scan : scansDB) {
            neighbors.put(scan.id, csd.calculateDistance(scan1.getWifiSignals(), scan.getWifiSignals()));
        }
        neighbors = Utils.sortByValue(neighbors);
        System.out.println(neighbors);

    */    // ------- List je rychlejsi --------------------
        testScan(scan1, AlgType.WIFI);
        System.out.println("druhy typ");
        testScan(scan1, AlgType.BLE);
        System.out.println("treti typ");
        testScan(scan1, AlgType.COMBINED);

        System.out.println("koncim");
    }

    private void testScan(Scan scan1, AlgType algType) {
        CalculateSignalDistance csd = new CalculateSignalDistance();
        List<NearestNeighbor> nearestNeighbors = new ArrayList<>();
        for (Scan scan : scansDB) {
            double distance;
            if (algType == AlgType.WIFI) {
                distance = csd.calculateDistance(scan1.getWifiSignals(), scan.getWifiSignals());
            } else if (algType == AlgType.BLE) {
                distance = csd.calculateDistance(scan1.getBleSignals(), scan.getBleSignals());
            } else {
                distance = csd.calculateDistance(scan1.getCombinedSignals(), scan.getCombinedSignals());
            }
            if (distance != Double.MAX_VALUE) {
                nearestNeighbors.add(new NearestNeighbor(scan.getX(), scan.getY(), distance));
            }
        }
        Collections.sort(nearestNeighbors);
       // System.out.println(nearestNeighbors);
        double weightsSum = 0;
        NearestNeighbor position = new NearestNeighbor(0,0,0);
        List<NearestNeighbor> realPositions = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NearestNeighbor nn = nearestNeighbors.get(i+1);
            double dist = nn.getDistance();
            if (dist == 0) {
                dist = 0.0000001;  // aby bylo možné dělit distancí
            }
            double weight = weightedMode ? 1 / dist : 1;
            weightsSum += weight;
            position.setX(position.getX() + weight * nn.getX());
            position.setY(position.getY() + weight * nn.getY());

            double pixelError = Math.sqrt(Math.pow(position.getX() / weightsSum - scan1.getX(), 2) + Math.pow(position.getY() / weightsSum - scan1.getY(), 2));
            double metersError = pixelError * floorPixelsToMeters;
            realPositions.add(new NearestNeighbor(position.getX() / weightsSum, position.getY() / weightsSum, metersError));
            position.setDistance(metersError);
            // System.out.println("i je: " + i + " a nn je: " + new NearestNeighbor(position.getX() / weightsSum, position.getY() / weightsSum, metersError));
        }
        if (weightsSum > 0) {
            position.setX(position.getX() / weightsSum);
            position.setY(position.getY() / weightsSum);
        }
        System.out.println(position.toString());

        // vypoctena pozice pro kazde k
        System.out.println(realPositions);

        Collections.sort(realPositions);
        //srovnany list od nejmensiho rozdilu
        System.out.println(realPositions);
    }

    private List<Double> getRealDistances(Scan scan1, AlgType algType) {
        CalculateSignalDistance csd = new CalculateSignalDistance();
        List<NearestNeighbor> nearestNeighbors = new ArrayList<>();
        for (Scan scan : scansDB) {
            double distance;
            if (algType == AlgType.WIFI) {
                distance = csd.calculateDistance(scan1.getWifiSignals(), scan.getWifiSignals());
            } else if (algType == AlgType.BLE) {
                distance = csd.calculateDistance(scan1.getBleSignals(), scan.getBleSignals());
            } else {
                distance = csd.calculateDistance(scan1.getCombinedSignals(), scan.getCombinedSignals());
            }
            if (distance != Double.MAX_VALUE) {
                nearestNeighbors.add(new NearestNeighbor(scan.getX(), scan.getY(), distance));
            }
        }
        Collections.sort(nearestNeighbors);
        // System.out.println(nearestNeighbors);
        double weightsSum = 0;
        NearestNeighbor position = new NearestNeighbor(0,0,0);
        List<Double> realDistances = new ArrayList<>();
        for (int i = 0; i < nearestNeighbors.size() - 1; i++) {
            // + 1 protoze v db mame i testovany scan
            NearestNeighbor nn = nearestNeighbors.get(i+1);
            double dist = nn.getDistance();
            if (dist == 0) {
                dist = 0.0000001;  // aby bylo možné dělit distancí
            }
            double weight = weightedMode ? 1 / dist : 1;
            weightsSum += weight;
            position.setX(position.getX() + weight * nn.getX());
            position.setY(position.getY() + weight * nn.getY());

            double pixelError = Math.sqrt(Math.pow(position.getX() / weightsSum - scan1.getX(), 2) + Math.pow(position.getY() / weightsSum - scan1.getY(), 2));
            double metersError = pixelError * floorPixelsToMeters;
            realDistances.add(metersError);
        }
        return realDistances;
    }
}
