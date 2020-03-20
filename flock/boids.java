package flock;

import java.util.ArrayList;

public class boids {
    int bid = 0;
    ArrayList<Point> pos;
    ArrayList<Point> vel;
    int size = 0;

    public boids(int n, ArrayList<Point> p, ArrayList<Point> v) {
        bid = n;
        pos = p;
        vel = v;
        size = p.size();
    }

    public Point posof(int i) {
        return pos.get(i);
    }

    public Point velof(int i) {
        return vel.get(i);
    }

    public void uppos(Point p, int i) {
        pos.remove(i);
        pos.add(i, p);
    }

    public void upvel(Point v, int i) {
        vel.remove(i);
        vel.add(i, v);
    }

    public int size() {
        return size;
    }

    public void separation(int k, int ind, double sepfrac) {
        Point v = velof(ind);
        Point p = posof(ind);
        int bn = size();

        ArrayList<Double> vdis = new ArrayList<>(k);
        ArrayList<Double> pdis = new ArrayList<>(k);

        BoidKDTree knn = new BoidKDTree(this);
        boids neis = knn.nearestK(p, k);
        for(int i = 0; i < k; i++) {
            if (i < neis.size()) {
                vdis = Point.vdist(neis.vel.get(i), v);
                pdis = Point.vdist(neis.pos.get(i), p);
                if (v.mul(vdis, pdis) > 0){
                    v.adddev(v.vmul(vdis, pdis), -sepfrac / Point.distance(neis.pos.get(i), p) / k, 0.0);
                }
                // v add max?
            }
        }
        upvel(v, ind);
        //return v;
    }

    public void cohesion(int k, int ind, double cohfrac) {
        int bn = size();
        Point v = velof(ind);
        Point p = posof(ind);
        ArrayList<Double> dis = new ArrayList<>(k);
        KDTree knn = new KDTree(pos);
        ArrayList<Point> neis = knn.nearestK(p, k);
        for(int i = 0; i < k; i++) {
            if (i < neis.size()) {
                v.addmul(Point.vdist(neis.get(i), p), cohfrac / k);
                // v add max?
            }
        }
        upvel(v, ind);
        //return v;
    }

    public void alignment(int k, int ind, double alifrac) {
        int bn = size();
        Point v = velof(ind);
        Point p = posof(ind);
        ArrayList<Double> dis = new ArrayList<>(k);
        BoidKDTree knn = new BoidKDTree(this);
        boids neis = knn.nearestK(p, k);
        for(int i = 0; i < k; i++) {
            if (i < neis.vel.size()) {
                v.addmul(Point.vdist(neis.vel.get(i), v), alifrac / k);
                // v add max?
            }
        }
        upvel(v, ind);
        //return v;
    }
}