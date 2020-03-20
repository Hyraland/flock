package flock;

import java.util.ArrayList;
import flock.Point;

public class NaivePointSet implements PointSet{
    private ArrayList<Point> points;

    public NaivePointSet(ArrayList<Point> points) {
        this.points = points;
    }

    @Override
    public Point nearest(double x, double y){
        double mindis = Double.MAX_VALUE;
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(x);
        coords.add(y);
        Point thisp = new Point(coords);
        Point minp = null;
        for (Point p: points) {
            double dist = Point.distance(p, thisp);
            if (dist < mindis) {
                mindis = dist;
                minp = p;
            }
        }
        return minp;
    };
}
