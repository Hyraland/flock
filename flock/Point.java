package flock;

import java.util.*;

public class Point {

    private ArrayList<Double> coords;
    private int dim;

    public Point(ArrayList<Double> coords) {
        this.coords = coords;
        this.dim = coords.size();
    }

    public double get(int i) {
        return coords.get(i);
    }

    public int dim() {
        return coords.size();
        //return this.dim;
    }

    /**
     * Returns the euclidean distance (L2 norm) squared between two points
     * (x1, y1) and (x2, y2). Note: This is the square of the Euclidean distance,
     * i.e. there's no square root.
     */
    private static double distance(ArrayList<Double> coords1, ArrayList<Double> coords2) {
        double totd = 0.0;
        int d = coords1.size();
        for (int i = 0; i < d; i++) {
            totd += Math.pow(coords1.get(i) - coords2.get(i), 2);
        }
        return totd;
    }

    private static ArrayList<Double> vdist(ArrayList<Double> coords1, ArrayList<Double> coords2) {
        int d = coords1.size();
        ArrayList<Double> totd = new ArrayList<>(d);
        for (int i = 0; i < d; i++) {
            totd.add(i, coords1.get(i) - coords2.get(i));
        }
        return totd;
    }

    public Point add(ArrayList<Double> d) {
        for (int i = 0; i < dim; i++) {
            double x = this.coords.remove(i);
            this.coords.add(i, x + d.get(i));
        }
        return this;
    }

    public Point add(Point d) {
        for (int i = 0; i < dim; i++) {
            double x = this.coords.remove(i);
            this.coords.add(i, x + d.get(i));
        }
        return this;
    }

    public ArrayList<Double> vmul(ArrayList<Double> d1, ArrayList<Double> d2) {
        ArrayList<Double> d = new ArrayList<>(dim);
        for (int i = 0; i < dim; i++) {d.add(0.0);}
        for (int i = 0; i < dim; i++) {
            double x = d1.get(i);
            double y = d2.get(i);
            if (Math.abs(y-x) < 0.0000001){break;}
            else{
                d.remove(i);
                d.add(i,x * y);
            }
        }
        return d;
    }

    public double mul(ArrayList<Double> d1, ArrayList<Double> d2) {
        double d = 0;
        for (int i = 0; i < dim; i++) {
            double x = d1.get(i);
            double y = d2.get(i);
            if (Math.abs(y-x) < 0.0000001){break;}
            else{
                d+=x * y;
            }
        }
        return d;
    }

    public Point addmul(ArrayList<Double> d, double f) {
        for (int i = 0; i < dim; i++) {
            double x;
            double y = d.get(i);
            if (Math.abs(y) < 0.0000001){break;}
            else{
                x = this.coords.remove(i);
                this.coords.add(i,x + f*y);
            }
        }
        return this;
    }

    public Point addmul(Point d, double f) {
        for (int i = 0; i < dim; i++) {
            double x;
            double y = d.get(i);
            if (Math.abs(y) < 0.0000001){break;}
            else{
                x = this.coords.remove(i);
                this.coords.add(i,x + f*y);
            }
        }
        return this;
    }

    public Point adddev(ArrayList<Double> d, double f, double mindis) {
        for (int i = 0; i < dim; i++) {
            double x;
            double y = d.get(i);
            if (Math.abs(y) < 0.0000001){break;}
            else{
                x = this.coords.remove(i);
                this.coords.add(i,x + f/(y-mindis));
            }
        }
        return this;
    }

    public Point adddev(Point d, double f) {
        for (int i = 0; i < dim; i++) {
            double x;
            double y = d.get(i);
            if (Math.abs(y) < 0.0000001){break;}
            else{
                x = this.coords.remove(i);
                this.coords.add(i,x + f/y);
            }
        }
        return this;
    }

    /**
     * Returns the euclidean distance (L2 norm) squared between two points.
     * Note: This is the square of the Euclidean distance, i.e.
     * there's no square root. 
     */
    public static double distance(Point p1, Point p2) {
        return distance(p1.coords, p2.coords);
    }

    public static ArrayList<Double> vdist(Point p1, Point p2) {
        return vdist(p1.coords, p2.coords);
    }

    @Override
    public boolean equals(Object other) {
        if (other == null) {
            return false;
        }
        if (other.getClass() != this.getClass()) {
            return false;
        }
        Point otherp = (Point) other;
        for (int i = 0; i < otherp.coords.size(); i++){
            if (coords.get(i) != otherp.coords.get(i)) {return false;}
        }
        return true;
    }

    @Override
    public int hashCode() {
        int tothash = 1;
        for (int i = 0; i < coords.size(); i++){
            double curc = coords.get(i);
            tothash *= Double.hashCode(curc);
        }
        return tothash;
    }
}
