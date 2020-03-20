package flock;

import java.util.*;

public class BoidKDTree {
    private ArrayList<Double> trees = null;
    private ArrayList<Double> vels = null;
    private BoidKDTree lchild;
    private BoidKDTree rchild;
    private int D;
    private int dim;

    public BoidKDTree(int dim) {
        trees = null;
        lchild = null;
        rchild = null;
        D = 0;
        this.dim = dim;
    }

    public BoidKDTree(boids b){
        int D = 0;
        ArrayList<Point> points = b.pos;
        ArrayList<Point> bvels = b.vel;
        int n = points.size();
        if (n == 0) {
            trees = null;
            vels = null;
            lchild = null;
            rchild = null;
            this.D = 0;
            this.dim = 0;
            return;
        }
        else {
            this.dim = points.get(0).dim();
            this.D = 0;
            BoidKDTree nodetrack;
            Point p = points.get(0);
            Point v = bvels.get(0);
            trees = new ArrayList<Double>();
            vels = new ArrayList<Double>();
            for (int i = 0; i < this.dim; i++){
                trees.add(p.get(i));
                vels.add(v.get(i));
            }

            for (int i = 1; i < n; i++) {
                p = points.get(i);
                v = bvels.get(i);
                nodetrack = this;
                while (true) {
                    if (insert(p, nodetrack) && nodetrack.lchild == null) {
                        nodetrack.lchild = new BoidKDTree(dim);
                        nodetrack.lchild.trees = new ArrayList<Double>(dim);
                        nodetrack.lchild.vels = new ArrayList<Double>(dim);
                        for (int j = 0; j < this.dim; j++){
                            nodetrack.lchild.trees.add(p.get(j));
                            nodetrack.lchild.vels.add(v.get(j));
                        }
                        nodetrack.lchild.D = (nodetrack.D + 1) % dim;
                        break;
                    } else if (!insert(p, nodetrack) && nodetrack.rchild == null) {
                        nodetrack.rchild = new BoidKDTree(dim);
                        nodetrack.rchild.trees = new ArrayList<Double>(dim);
                        nodetrack.rchild.vels = new ArrayList<Double>(dim);
                        for (int j = 0; j < this.dim; j++){
                            nodetrack.rchild.trees.add(p.get(j));
                            nodetrack.rchild.vels.add(v.get(j));

                        }
                        nodetrack.rchild.D = (nodetrack.D + 1) % dim;
                        break;
                    } else if (insert(p, nodetrack)) {
                        nodetrack = nodetrack.lchild;
                    } else {
                        nodetrack = nodetrack.rchild;
                    }
                }
            }
        }
    }

    private boolean insert(Point p, BoidKDTree kdtree) {
        ArrayList<Double> coords = new ArrayList<>();
        for (int i = 0; i < dim; i++){coords.add(kdtree.trees.get(i));}
        Point ptree = new Point(coords);
        return Compare(kdtree.D, p, ptree);
    }

    private boolean Compare(int D, Point p, Point pc) {
        if (p.get(D) < pc.get(D)) {
            return true; // if goodside on the left, return true, otherwise return false
        }else if (p.get(D) >= pc.get(D)) {
            return false;
        }
        return false;
    }

    public boids nearest(ArrayList<Double> coords) {
        Point curp = new Point(coords);
        BoidKDTree best = nearest(curp, this, this);
        ArrayList<Double> cbest = new ArrayList<>();
        ArrayList<Double> cvbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){
            cbest.add(best.trees.get(i));
            cvbest.add(best.vels.get(i));
        }
        Point pbest = new Point(cbest);
        Point vbest = new Point(cvbest);
        ArrayList<Point> ps = new ArrayList<>();
        ArrayList<Point> vs = new ArrayList<>();
        ps.add(pbest);
        vs.add(vbest);
        boids b = new boids(1, ps, vs);
        return b;
    }

    private BoidKDTree nearest(Point goal, BoidKDTree node, BoidKDTree best) {
        if (node == null) {
            return best;
        }
        BoidKDTree goodside = null;
        BoidKDTree badside = null;
        ArrayList<Double> cnode = new ArrayList<>();
        for (int i = 0; i < dim; i++){cnode.add(node.trees.get(i));}
        ArrayList<Double> cbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(best.trees.get(i));}
        Point pnode = new Point(cnode);
        Point pbest = new Point(cbest);

        if (Point.distance(goal, pnode) < Point.distance(goal, pbest)) {
            best = node;
        }
        if (Compare(node.D, goal, pnode)) {
            goodside = node.lchild;
            badside = node.rchild;
        }else {
            goodside = node.rchild;
            badside = node.lchild;
        }
        best = nearest(goal, goodside, best);
        if (Math.abs(goal.get(D) - pnode.get(D)) < Point.distance(goal, pbest)) {
            best = nearest(goal, badside, best);
        }
        return best;
    }

    public boids nearestK(ArrayList<Double> coords, int k) {
        Point curp = new Point(coords);
        ArrayHeapMinPQ<BoidKDTree> bestthis = new ArrayHeapMinPQ<>();
        ArrayList<Double> cbest = new ArrayList<>();
        ArrayList<Double> vbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(this.trees.get(i));}
        Point pbest = new Point(cbest);
        Point pvbest = new Point(cbest);
        bestthis.add(this, -Point.distance(curp, pbest));

        ArrayHeapMinPQ<BoidKDTree> bests = nearestK(curp, this, bestthis, k);
        ArrayList<Point> cbests = new ArrayList<>();
        ArrayList<Point> vbests = new ArrayList<>();
        BoidKDTree cthis;

        for (int j = 0; j < k; j++) {
            cthis = bests.removeSmallest();
            cbest = new ArrayList<>();
            vbest = new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                cbest.add(cthis.trees.get(i));
                vbest.add(cthis.vels.get(i));
            }
            pbest = new Point(cbest);
            pvbest = new Point(vbest);
            cbests.add(pbest);
            vbests.add(pvbest);
        }
        boids b = new boids(cbests.size(), cbests, vbests);
        return b;
    }

    public boids nearestK(Point curp, int k) {
        ArrayHeapMinPQ<BoidKDTree> bestthis = new ArrayHeapMinPQ<>();
        ArrayList<Double> cbest = new ArrayList<>();
        ArrayList<Double> vbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(this.trees.get(i));}
        Point pbest = new Point(cbest);
        Point pvbest = new Point(cbest);
        bestthis.add(this, -Point.distance(curp, pbest));

        ArrayHeapMinPQ<BoidKDTree> bests = nearestK(curp, this, bestthis, k);
        ArrayList<Point> cbests = new ArrayList<>();
        ArrayList<Point> vbests = new ArrayList<>();
        BoidKDTree cthis;

        for (int j = 0; j < k; j++) {
            if(bests.size()>0) {
                cthis = bests.removeSmallest();
                cbest = new ArrayList<>();
                vbest = new ArrayList<>();
                for (int i = 0; i < dim; i++) {
                    cbest.add(cthis.trees.get(i));
                    vbest.add(cthis.vels.get(i));
                }
                pbest = new Point(cbest);
                pvbest = new Point(vbest);
                cbests.add(pbest);
                vbests.add(pvbest);
            }
        }
        boids b = new boids(cbests.size(), cbests, vbests);
        return b;
    }

    private ArrayHeapMinPQ<BoidKDTree> nearestK(Point goal, BoidKDTree node, ArrayHeapMinPQ<BoidKDTree> bests, int k) {

        if (node == null) {
            return bests;
        }
        BoidKDTree goodside = null;
        BoidKDTree badside = null;
        ArrayList<Double> cnode = new ArrayList<>();
        for (int i = 0; i < dim; i++){cnode.add(node.trees.get(i));}
        ArrayList<Double> cbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(bests.getSmallest().trees.get(i));}
        Point pnode = new Point(cnode);
        Point pbest = new Point(cbest);

        if (Point.distance(goal, pnode) < Point.distance(goal, pbest) || bests.size() <= k) {
            bests.add(node, -Point.distance(goal, pnode));
            if (bests.size()>k){bests.removeSmallest();}
        }
        if (Compare(node.D, goal, pnode)) {
            goodside = node.lchild;
            badside = node.rchild;
        }else {
            goodside = node.rchild;
            badside = node.lchild;
        }
        bests = nearestK(goal, goodside, bests, k);

        if (Math.abs(goal.get(D) - pnode.get(D)) < Point.distance(goal, pbest)) {
            bests = nearestK(goal, badside, bests, k);
        }
        return bests;
    }
}

