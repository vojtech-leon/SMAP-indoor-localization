package cz.uhk.vojtele1.indoorpositiontest.NN;

import java.util.Collections;
import java.util.Map;

/**
 * Created by Kriz on 16. 11. 2015
 * Modified by Vojtech on 02. 02. 2019
 */
public class CalculateSignalDistance {
    private int noSignal = -105; // dB

    public CalculateSignalDistance() {

    }

    public CalculateSignalDistance(int noSignal) {
        this.noSignal = noSignal;
    }

    public double calculateDistance(final Map<String, Integer> signals1, final Map<String, Integer> signals2) {

        // pokud neobsahuje zadny stejny klic
        if (Collections.disjoint(signals1.keySet(), signals2.keySet())) {
            return Double.MAX_VALUE;
        }
        for (String keyName : signals1.keySet()) {
            if (!signals2.containsKey(keyName)) {
                signals2.put(keyName, noSignal);
            }
        }
        for (String keyName : signals2.keySet()) {
            if (!signals1.containsKey(keyName)) {
                signals1.put(keyName, noSignal);
            }
        }
        int distanceSquareSum = 0;
        for (String id : signals1.keySet()) {
            double signalStrength1 = signals1.get(id);
            double signalStrength2 = signals2.get(id);
            distanceSquareSum += Math.pow(signalStrength1-signalStrength2, 2);
        }
        // System.out.println("distanceSquareSum="+distanceSquareSum);
        return Math.sqrt(distanceSquareSum);
    }
}
