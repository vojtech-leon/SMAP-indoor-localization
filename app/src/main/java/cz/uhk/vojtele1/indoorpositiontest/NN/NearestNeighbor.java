package cz.uhk.vojtele1.indoorpositiontest.NN;

import android.support.annotation.NonNull;

public class NearestNeighbor implements Comparable {
    private double x, y;
    private double distance; // pro testování

    public NearestNeighbor(double x, double y, double distance) {
        this.x = x;
        this.y = y;
        this.distance = distance;
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

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    @Override
    public String toString() {
        return "{" +
                "x=" + x +
                ", y=" + y +
                ", distance=" + distance +
                '}';
    }


    @Override
    public int compareTo(@NonNull Object o) {
        if (o instanceof NearestNeighbor) {
            double v1 = ((NearestNeighbor)o).getDistance();
            double v2 = this.getDistance();
            return Double.compare(v2, v1);
        } else {
            return 0;
        }
    }
}
