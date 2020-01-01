package partitionnement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TasGaussien {

    static private double mean(double [] tab){
        double total = 0;
        for(int randomIndex = 0; randomIndex < tab.length; randomIndex += 1){
            total += tab[randomIndex];
        }
        return total/tab.length;
    }

    static private double sigma(double []tab){
        double mean = mean(tab);
        double sigma = 0;
        for(int randomIndex = 0; randomIndex < tab.length; randomIndex += 1){
            sigma += Math.pow((mean - tab[randomIndex]),2);
        }
        return Math.pow(sigma/tab.length,0.5f);
    }

    public static double[][] histogramme(double xmin, double xmax, int NbCases, double[] ech) {
        // On creer le tableau qui va contenir
        //  0: les abcisses des cases de l'histogramme
        //  1: les valeurs pour chaque case
        double[][] Histo = new double[2][NbCases];
        // TODO: Calcule de la taille d'une case
        double tailleCase = (xmax-xmin)/NbCases;
        // TODO: Calcule des abcisses Histo[0][...]
        for(int histoIndex = 0; histoIndex < Histo[0].length; histoIndex += 1){
            Histo[0][histoIndex] = xmin + tailleCase*(histoIndex+0.5);
        }

        for(int i=0; i<ech.length; i++) {
            // TODO: pour chaque valeur: trouver a quelle case elle appartient et incrementer de un l'histogramme
            Histo[1][(int) Math.floor((ech[i]-xmin)/tailleCase)] += 1;
        }
        int sum = 0;
        for (int i = 0; i < Histo[1].length; i+=1){
            sum += Histo[1][i];
        }
        return Histo;
    }

    private static void histoFileWriter(String fileName, double histo[][]) {
        try {
                FileWriter fw = new FileWriter(fileName+".d");
                for (int i = 0; i < histo[0].length; i += 1) {
                    fw.write("" + histo[0][i] + " " + histo[1][i] + "\n");
                }
                fw.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }

    private static void pointFileWriter(String fileName, double points[][]){
        try {
            FileWriter fw = new FileWriter(fileName+".d");
            for (int i = 0; i < points.length; i += 1) {
                for (int j = 0; j < points[i].length; j += 1){
                    fw.write("" + points[i][j]+ " ");
                }
                fw.write("\n");
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static void pointFileWriter(String fileName, double points[][], int clusterSizes[]){
        try {
            int idxInCluster = 0;
            int clusterIdx = 0;
            FileWriter fw = new FileWriter(fileName+".d");
            for (int i = 0; i < points.length; i += 1) {
                for (int j = 0; j < points[i].length; j += 1){
                    fw.write("" + points[i][j]+ " ");
                }
                fw.write("\n");
                if (idxInCluster == clusterSizes[clusterIdx]){
                    idxInCluster=0;
                    fw.write("\n");
                    clusterIdx+=1;
                }
                idxInCluster+= 1;
            }
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static void gnuplotHistoWriter(String plotName, String fileName) {
        try {
            FileWriter fw = new FileWriter(plotName + ".gnu");
            fw.write("set terminal svg size 920,920 \nset output '");
            fw.write(""+plotName);
            fw.write(".svg'\nset title \"histo\" \n");
            fw.write("set grid\nset style data boxes\nplot");
            fw.write("'"+fileName + ".d'");

            fw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }

    private static void gnuplotPointsWriter(String graphName, String gnuFileName, String dataFileName) {
        try {
            FileWriter fw = new FileWriter(gnuFileName + ".gnu");
            fw.write("set terminal svg size 920,920 \nset output '");
            fw.write(""+graphName);
            fw.write(".svg'\nset title \"histo\" \n");
            fw.write("set grid\nset style data points\nplot");
            fw.write("'"+dataFileName + ".d'");

            fw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        double [][] testRandom = new double[10000][2];

        Random rn = new Random();
        double gaussianCenters[][] = new double[2][2];
        int [] gaussianSizes = new int[gaussianCenters.length];
        double min = -20;
        double max = 20;
        gaussianCenters[0][0] = -2;
        gaussianCenters[0][1] = 2;

        gaussianCenters[1][0] = 5;
        gaussianCenters[1][1] = 0;

        for(int randomIndex = 0; randomIndex < testRandom.length; randomIndex += 1){
            int gaussianIndex = (int) (((float)randomIndex/testRandom.length)* gaussianCenters.length);
            testRandom[randomIndex][0] = gaussianCenters[gaussianIndex][0]+rn.nextGaussian();
            testRandom[randomIndex][1]= gaussianCenters[gaussianIndex][1]+rn.nextGaussian();
            gaussianSizes[gaussianIndex]+=1;
            System.out.print(""+gaussianIndex+"\n");

        }
        pointFileWriter("generatedFiles/data/twoGaussians",testRandom,gaussianSizes);
        gnuplotPointsWriter("../graphs/twoGaussians","../gnuplot/twoGaussians","../data/twoGaussians");
    }
}
