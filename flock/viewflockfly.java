package flock;

import java.io.*;

public class viewflockfly {
    public static void main(String[] args) {
        int width = 800;
        int height = 800;
        double unirad = 500;
        int nb = 500;

        double[][] pos4d = new double[nb][2];
        String file = "fly-nb_1000-k_40-sepfrac_0.300000-cohfrac_0.025000-alifrac_0.000100-T_20.000000-velfactor_0.100000-mindis_5.000000.txt";
        try {
            StdDraw.setCanvasSize(width, height);
            StdDraw.setScale(-1 * unirad, unirad);
            StdDraw.setPenRadius(0.003);
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
