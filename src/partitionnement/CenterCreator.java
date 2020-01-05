package partitionnement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class CenterCreator {
    int dimension;
    int centerCount;
    double centersLocation[][];
    public CenterCreator(int centerCount, int dimension){
        this.dimension = dimension;
        this.centerCount = centerCount;
        centersLocation = new double[centerCount][dimension];
    }
    public double[][]giveCenters(){
        return centersLocation;
    }

    public double[][]generateRandomCenters(float minBounds[], float maxBounds[]){
        Random rn = new Random();
        for (int centerIdx = 0; centerIdx < centerCount; centerIdx += 1){
            for (int dimIdx = 0; dimIdx < dimension; dimIdx += 1){
                centersLocation[centerIdx][dimIdx] =
                        (maxBounds[dimIdx] - minBounds[dimIdx]) * rn.nextDouble() + minBounds[dimIdx];
            }
        }
        return giveCenters();
    }


    private double min(double a, double b){
        if (a < b){
            return a;
        }
        return b;
    }
    private double max(double a, double b){
        if (a > b){
            return a;
        }
        return b;
    }

    private int argMax(double tab[]){
        double max = tab[0];
        int maxIndex = 0;
        for (int i = 1; i < tab.length; i += 1){
            max = max(tab[i],max);
            if (max == tab[i]){
                maxIndex = i;
            }
        }
        return maxIndex;
    }

    private double squaredDistance(double pointA[], double pointB[]){
        double distance = 0;
        double temp;
        for (int dimIdx = 0; dimIdx < pointA.length; dimIdx += 1){
            temp = (pointA[dimIdx] - pointB[dimIdx]);
            distance += temp * temp;
        }
        return distance;
    }

    private double minDistance(int centersInitialized, double point[]){
        double min = squaredDistance(centersLocation[0],point);
        for (int centerIdx = 1; centerIdx < centersInitialized; centerIdx += 1){
            min = min(squaredDistance(centersLocation[centerIdx],point),min);
        }
        return min;
    }

    public double[][]generateSpacedCenters(double points[][]){
        Random rn = new Random();
        int n = points.length;
        centersLocation[0] = points[rn.nextInt(n)];
        for (int k = 1; k < centerCount; k += 1){
            double[] listMin = new double[points.length];
            for (int dataIdx = 0; dataIdx < points.length; dataIdx += 1){
                listMin[dataIdx] = minDistance(k,points[dataIdx]);
            }
            centersLocation[k] = points[argMax(listMin)];
        }

        return giveCenters();
    }

    public void writeCenters(String fileName) throws IOException {
        FileWriter fw = new FileWriter("generatedFiles/data/" + fileName+".d");
        for (int centerIdx = 0; centerIdx < centersLocation.length; centerIdx += 1){
            for (int dimIdx = 0; dimIdx < centersLocation[centerIdx].length; dimIdx += 1) {
                fw.write(""+centersLocation[centerIdx][dimIdx] + ' ');
            }
            fw.write('\n');
        }
        fw.close();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("CentersCreator's test");
        double[][] data = (new ImageToExtract("mms")).extractAllPixelsColorNormalized();
        CenterCreator cc = new CenterCreator(10,data[0].length);
        double[][] centers = cc.generateSpacedCenters(data);
        for (int i = 0; i < centers.length; i += 1){
            System.out.print("\n{");
            for (int j = 0; j < centers[i].length; j += 1){
                System.out.print(centers[i][j] + ' ');
            }
            System.out.println('}');
        }
        cc.writeCenters("centersMms");
    }
}
