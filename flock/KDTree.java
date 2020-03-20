package flock;

import java.util.*;

public class KDTree {
    private ArrayList<Double> trees = null;
    private KDTree lchild;
    private KDTree rchild;
    private int D;
    private int dim;

    public KDTree(int dim) {
        trees = null;
        lchild = null;
        rchild = null;
        D = 0;
        this.dim = dim;
    }

    public KDTree(ArrayList<Point> points){
        int D = 0;
        int n = points.size();
        if (n == 0) {
            trees = null;
            lchild = null;
            rchild = null;
            this.D = 0;
            this.dim = 0;
            return;
        }
        else {
            this.dim = points.get(0).dim();
            this.D = 0;
            KDTree nodetrack;
            Point p = points.get(0);
            trees = new ArrayList<Double>();
            for (int i = 0; i < this.dim; i++){trees.add(p.get(i));}

            for (int i = 1; i < n; i++) {
                p = points.get(i);
                nodetrack = this;
                while (true) {
                    if (insert(p, nodetrack) && nodetrack.lchild == null) {
                        nodetrack.lchild = new KDTree(dim);
                        nodetrack.lchild.trees = new ArrayList<Double>(dim);
                        for (int j = 0; j < this.dim; j++){nodetrack.lchild.trees.add(p.get(j));}
                        nodetrack.lchild.D = (nodetrack.D + 1) % dim;
                        break;
                    } else if (!insert(p, nodetrack) && nodetrack.rchild == null) {
                        nodetrack.rchild = new KDTree(dim);
                        nodetrack.rchild.trees = new ArrayList<Double>(dim);
                        for (int j = 0; j < this.dim; j++){nodetrack.rchild.trees.add(p.get(j));}
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

    private boolean insert(Point p, KDTree kdtree) {
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

    public Point nearest(ArrayList<Double> coords) {
        Point curp = new Point(coords);
        KDTree best = nearest(curp, this, this);
        ArrayList<Double> cbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(best.trees.get(i));}
        Point pbest = new Point(cbest);
        return pbest;
    }

    private KDTree nearest(Point goal, KDTree node, KDTree best) {
        if (node == null) {
            return best;
        }
        KDTree goodside = null;
        KDTree badside = null;
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

    public ArrayList<Point> nearestK(ArrayList<Double> coords, int k) {
        Point curp = new Point(coords);
        ArrayHeapMinPQ<KDTree> bestthis = new ArrayHeapMinPQ<>();
        ArrayList<Double> cbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(this.trees.get(i));}
        Point pbest = new Point(cbest);
        bestthis.add(this, -Point.distance(curp, pbest));

        ArrayHeapMinPQ<KDTree> bests = nearestK(curp, this, bestthis, k);
        ArrayList<Point> cbests = new ArrayList<>();
        KDTree cthis;

        for (int j = 0; j < k; j++) {
            cthis = bests.removeSmallest();
            cbest = new ArrayList<>();
            for (int i = 0; i < dim; i++) {
                cbest.add(cthis.trees.get(i));
            }
            pbest = new Point(cbest);
            cbests.add(pbest);
        }
        return cbests;
    }

    public ArrayList<Point> nearestK(Point curp, int k) {
        ArrayHeapMinPQ<KDTree> bestthis = new ArrayHeapMinPQ<>();
        ArrayList<Double> cbest = new ArrayList<>();
        for (int i = 0; i < dim; i++){cbest.add(this.trees.get(i));}
        Point pbest = new Point(cbest);
        bestthis.add(this, -Point.distance(curp, pbest));

        ArrayHeapMinPQ<KDTree> bests = nearestK(curp, this, bestthis, k);

        ArrayList<Point> cbests = new ArrayList<>();
        KDTree cthis;

        for (int j = 0; j < k; j++) {
            if(bests.size() > 0) {
                cthis = bests.removeSmallest();
                cbest = new ArrayList<>();
                for (int i = 0; i < dim; i++) {
                    cbest.add(cthis.trees.get(i));
                }
                pbest = new Point(cbest);
                cbests.add(pbest);
            }
        }
        return cbests;
    }

    private ArrayHeapMinPQ<KDTree> nearestK(Point goal, KDTree node, ArrayHeapMinPQ<KDTree> bests, int k) {

        if (node == null) {
            return bests;
        }
        KDTree goodside = null;
        KDTree badside = null;
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
