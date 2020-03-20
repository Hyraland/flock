package flock;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class KDTreeTest {
    @Test
    public void simpleKDTreeNearestCheck() {
        ArrayList<Point> points = new ArrayList<Point>();
        ArrayList<Double> coords = new ArrayList<>();
        coords.add(2.0);
        coords.add(3.0);
        points.add(new Point(coords));
        coords = new ArrayList<>();
        coords.add(4.0);
        coords.add(2.0);
        points.add(new Point(coords));
        coords = new ArrayList<>();
        coords.add(4.0);
        coords.add(5.0);
        points.add(new Point(coords));
        coords = new ArrayList<>();
        coords.add(3.0);
        coords.add(3.0);
        points.add(new Point(coords));
        coords = new ArrayList<>();
        coords.add(1.0);
        coords.add(5.0);
        points.add(new Point(coords));
        coords = new ArrayList<>();
        coords.add(4.0);
        coords.add(4.0);
        points.add(new Point(coords));

        KDTree newkd = new KDTree(points);
        NaivePointSet newnv = new NaivePointSet(points);
        coords = new ArrayList<>();
        coords.add(0.0);
        coords.add(5.0);

        assertEquals(newkd.nearest(coords).get(0), newnv.nearest(0,5).get(0),0.001);
        assertEquals(newkd.nearest(coords).get(1), newnv.nearest(0,5).get(1),0.001);
        assertEquals(newkd.nearestK(coords, 1).get(0).get(0), newkd.nearest(coords).get(0),0.001);
        assertEquals(newkd.nearestK(coords,1).get(0).get(1), newkd.nearest(coords).get(1),0.001);
        assertEquals(newkd.nearestK(coords, 1).get(0).get(0), newnv.nearest(0,5).get(0),0.001);
        assertEquals(newkd.nearestK(coords,1).get(0).get(1), newnv.nearest(0,5).get(1),0.001);
        coords = new ArrayList<>();
        coords.add(2.0);
        coords.add(3.0);
        assertEquals(newkd.nearest(coords).get(0), newnv.nearest(2,3).get(0),0.001);
        assertEquals(newkd.nearest(coords).get(1), newnv.nearest(2,3).get(1),0.001);

        coords = new ArrayList<>();
        coords.add(7.0);
        coords.add(7.0);
        assertEquals(newkd.nearest(coords).get(0), newnv.nearest(7,7).get(0),0.001);
        assertEquals(newkd.nearest(coords).get(1), newnv.nearest(7,7).get(1),0.001);

    }

    @Test
    public void randomKDTreeNearestCheck() {
        ArrayList<Point> points = new ArrayList<Point>();
        ArrayList<Double> coords = new ArrayList<>();
        Random r =new Random();

        for (int i = 0; i < 50;i++) {
            coords = new ArrayList<>();
            coords.add(r.nextDouble() * 20);
            coords.add(r.nextDouble() * 20);
            points.add(new Point(coords));
        }

        KDTree newkd = new KDTree(points);
        NaivePointSet newnv = new NaivePointSet(points);

        coords = new ArrayList<>();
        coords.add(2.0);
        coords.add(3.0);
        assertEquals(newkd.nearest(coords).get(0), newnv.nearest(2,3).get(0),0.001);
        assertEquals(newkd.nearest(coords).get(1), newnv.nearest(2,3).get(1),0.001);

        coords = new ArrayList<>();
        coords.add(0.0);
        coords.add(5.0);
        assertEquals(newkd.nearestK(coords, 1).get(0).get(0), newnv.nearest(0,5).get(0),0.001);
        assertEquals(newkd.nearestK(coords,1).get(0).get(1), newnv.nearest(0,5).get(1),0.001);

        coords = new ArrayList<>();
        coords.add(7.0);
        coords.add(7.0);
        assertEquals(newkd.nearest(coords).get(0), newnv.nearest(7,7).get(0),0.001);
        assertEquals(newkd.nearest(coords).get(1), newnv.nearest(7,7).get(1),0.001);

    }
}
