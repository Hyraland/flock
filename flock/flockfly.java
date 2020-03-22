package flock;

import java.util.ArrayList;
import java.util.*;
import java.io.*;

public class flockfly {

    public static void main(String[] args) {
        int width = 800;
        int height = 800;
        double unirad = 500;
        double space = 0.2;
        double velfactor = 0.1;
        int nb =100;
        int k = 10;
        double mindissep = 2.5;
        double mindiscoh = 5;
        double sepfrac = 0.3;
        double cohfrac = 0.05;
        double alifrac = 1.0;
        double curT = 0;
        double T = 20.0; // Double.valueOf(args[0]);
        double dt = 0.01; // Double.valueOf(args[1]);
        ArrayList<Point> ps = new ArrayList<>(nb);
        ArrayList<Point> vs = new ArrayList<>(nb);
        Random r = new Random();
        ArrayList<Double> tempp;
        for (int i = 0; i < nb; i++){
            tempp = new ArrayList<Double>();
            tempp.add(r.nextDouble()*space*2*unirad-space*unirad);
            tempp.add(r.nextDouble()*space*2*unirad-space*unirad);
            Point p = new Point(tempp);
            tempp = new ArrayList<Double>();
            tempp.add(r.nextDouble()*velfactor*unirad-0.5*velfactor*unirad);
            tempp.add(r.nextDouble()*velfactor*unirad-0.5*velfactor*unirad);
            Point v = new Point(tempp);
            ps.add(p);
            vs.add(v);
        }
        boids b = new boids(nb, ps, vs);

        double[][] pos4d = new double[nb][2];
        String file = String.format("fly-nb_%d-k_%d-sepfrac_%f-cohfrac_%f-alifrac_%f-T_%f-velfactor_%f-mindis_%f.txt", nb, k, sepfrac, cohfrac, alifrac, T, velfactor, mindissep);
        try {
            FileWriter os = new FileWriter(file);
            for (int it = 0; it < nb; it++) {
                os.write(b.posof(it).get(0)+"\t"); // writes the bytes
                os.write(b.posof(it).get(1)+"\t"); // writes the bytes
                pos4d[it][0] = b.posof(it).get(0);
                pos4d[it][1] = b.posof(it).get(1);
            }
            os.write("\r\n");

            while (curT <= T) {
                for (int i = 0; i < nb; i++) {
                    b.separation(1, i, sepfrac, mindissep);
                    b.cohesion(k*2, i, cohfrac, mindiscoh);
                    b.alignment(1, i, alifrac);
                    b.posof(i).addmul(b.velof(i), dt);
                }

                for (int i = 0; i < nb; i++) {
                    double x = b.posof(i).get(0);
                    double y = b.posof(i).get(1);
                    while (x > unirad) {x -= 2*unirad;}
                    while (y > unirad) {y -= 2*unirad;}
                    os.write(x+"\t"); // writes the bytes
                    os.write(y+"\t"); // writes the bytes
                }
                os.write("\r\n");
                curT += dt;
                System.out.printf("Constructing data, current: %f, target: %f \n", curT, T);
            }
            os.close();

            StdDraw.setCanvasSize(width, height);
            StdDraw.setScale(-1 * unirad, unirad);
            StdDraw.setPenRadius(0.005);
            StdDraw.clear();
            for (int i = 0; i < nb; i++) {
                StdDraw.point(pos4d[i][0], pos4d[i][1]);
            }
            StdDraw.enableDoubleBuffering();

            BufferedReader is = new BufferedReader(new FileReader(file));
            String line;
            while((line = is.readLine()) != null){
                StdDraw.clear();
                String[] temp = line.split("\t");
                for(int j=0;j<nb;j+=1){
                    pos4d[j][0] = Double.parseDouble(temp[2*j]);
                    pos4d[j][1] = Double.parseDouble(temp[2*j+1]);
                    while (pos4d[j][0] > unirad) {pos4d[j][0] -= 2*unirad;}
                    while (pos4d[j][1] > unirad) {pos4d[j][1] -= 2*unirad;}
                    while (pos4d[j][0] < -unirad) {pos4d[j][0] += 2*unirad;}
                    while (pos4d[j][1] < -unirad) {pos4d[j][1] += 2*unirad;}
                    StdDraw.point(pos4d[j][0],pos4d[j][1]);
                }
                StdDraw.show();
                StdDraw.pause(10);
            }
            is.close();

        } catch (IOException e) {
            System.out.print("Exception");
        }
    }
}
