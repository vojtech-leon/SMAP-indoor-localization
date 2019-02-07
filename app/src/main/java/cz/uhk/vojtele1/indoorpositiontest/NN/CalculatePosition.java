package cz.uhk.vojtele1.indoorpositiontest.NN;

import cz.uhk.vojtele1.indoorpositiontest.model.Scan;
import cz.uhk.vojtele1.indoorpositiontest.utils.AlgType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CalculatePosition {

    private CalculateSignalDistance csd;

    public CalculatePosition(CalculateSignalDistance csd) {
        this.csd = csd;
    }

    public NearestNeighbor calculatePosition(Map<String, Integer> signals, List<Scan> scansDB, int k, AlgType algType, boolean weightedMode) {
        List<NearestNeighbor> nearestNeighbors = new ArrayList<>();
        for (Scan scan : scansDB) {
            double distance;
            if (algType == AlgType.WIFI) {
                distance = csd.calculateDistance(signals, scan.getWifiSignals());
            } else if (algType == AlgType.BLE) {
                distance = csd.calculateDistance(signals, scan.getBleSignals());
            } else {
                distance = csd.calculateDistance(signals, scan.getCombinedSignals());
            }
            if (distance != Double.MAX_VALUE) {
                nearestNeighbors.add(new NearestNeighbor(scan.getX(), scan.getY(), distance));
            }
        }
        Collections.sort(nearestNeighbors);
        // System.out.println(nearestNeighbors);
        double weightsSum = 0;
        NearestNeighbor position = new NearestNeighbor(0,0,0);
        if (k > nearestNeighbors.size()) {
            k = nearestNeighbors.size();
        }
        for (int i = 0; i < k; i++) {
            NearestNeighbor nn = nearestNeighbors.get(i);
            double dist = nn.getDistance();
            if (dist == 0) {
                dist = 0.0000001;  // aby bylo možné dělit distancí pro vážený mód
            }
            double weight = weightedMode ? 1 / dist : 1;
            weightsSum += weight;
            position.setX(position.getX() + weight * nn.getX());
            position.setY(position.getY() + weight * nn.getY());
        }
        if (weightsSum > 0) {
            position.setX(position.getX() / weightsSum);
            position.setY(position.getY() / weightsSum);
        }
        return position;
    }
}
