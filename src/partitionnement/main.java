package partitionnement;

import java.io.*;

public class main {

    public static void partie1() throws IOException{
        ImageToExtract ext = new ImageToExtract("mms");
        double[][] point = ext.extractAllPixelsColorNormalized();
        ext.createGnuPlot();
        CenterCreator center = new CenterCreator(6, 3);
        double[][] centers = new double[6][3];
        centers = center.generateSpacedCenters(point);
        /*
        centers[0][0] = 0.0;
        centers[0][1] = 0.0;
        centers[0][2] = 0.0;
        centers[1][0] = 0.0;
        centers[1][1] = 0.2;
        centers[1][2] = 0.9;
        centers[2][0] = 1.0;
        centers[2][1] = 0.0;
        centers[2][2] = 0.0;
        centers[3][0] = 1.0;
        centers[3][1] = 1.0;
        centers[3][2] = 0.0;
        centers[4][0] = 0.0;
        centers[4][1] = 1.0;
        centers[4][2] = 0.1;
        centers[5][0] = 0.6;
        centers[5][1] = 0.8;
        centers[5][2] = 0.8;
         */
        Mixture mix = new Mixture(point, centers);
        mix.epoque(50);
        MixtureGrapher mg = new MixtureGrapher(mix, "mms", "parite1");
        mg.createAllFiles();
    }

    public static void question1() throws IOException {
        ImageToExtract ext = new ImageToExtract("mms");
        double[][] point = ext.extractAllPixelsColorNormalized();
        ext.createGnuPlot();
        CenterCreator center = new CenterCreator(10, 3);
        for (int i =0; i < 10; i += 1){
            double[][] centers = new double[10][3];
            centers =  center.generateSpacedCenters(point);
            Mixture mix = new Mixture(point, centers);
            mix.epoque(50);
            MixtureGrapher mg = new MixtureGrapher(mix,"mms",i+"th");
            System.out.println("at "+i + " score = " + mix.score());
            mg.createAllFiles();
        }
    }

    public static void question2() throws IOException {
        ImageToExtract ext = new ImageToExtract("mms");
        double[][] point = ext.extractAllPixelsColorNormalized();
        double[][] res = new double[9][10];
        for(int i = 2; i <= 10; i++){
            CenterCreator center = new CenterCreator(i, 3);
            for(int j = 0; j < 10; j++){
                double[][] centers;
                centers =  center.generateSpacedCenters(point);
                Mixture mix = new Mixture(point, centers);
                mix.epoque(50);
                res[i-2][j] = mix.score();
            }
        }
        double[] y = new double[9];
        double[] x = new double[9];
        for(int i = 0; i < 9; i++){
            double max = 0;
            for(int j = 0; j < 10; j++){
                if(max < res[i][j]){
                    max = res[i][j];
                }
            }
            y[i] = max;
            x[i] = i + 2;
        }

    }

    public static void question3() throws IOException {
        ImageToExtract ext = new ImageToExtract("peacock");
        double[][] point = ext.extractAllPixelsColorNormalized();
        ext.createGnuPlot();
        CenterCreator center = new CenterCreator(10, 3);
        double[][] centers = new double[10][3];
        centers =  center.generateSpacedCenters(point);
        Mixture mix = new Mixture(point, centers);
        mix.epoque(50);
        MixtureGrapher mg = new MixtureGrapher(mix,"peacock","peacockCenters");
        System.out.println(mix.score());
        mg.createAllFiles();
    }

    public static double[][] dotDExtractor(String fileName) throws FileNotFoundException {
        try {
            double data[][] = new double[4000][2];
            BufferedReader in = new BufferedReader(new FileReader(fileName));
            String str;
            int i = 0;
            while ((str = in.readLine()) != null) {
                String [] strs = str.split(" ");
                for (int j = 0; j < strs.length; j += 1){
                    data[i / 2][i % 2] = Double.parseDouble(strs[j]);
                    i+=1;
                }
            }
            in.close();
            return data;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static double moyenneScore(int nombreCentres, double [][] data){
        double sum = 0;
        for (int i =0; i < 10; i += 1){
            CenterCreator center = new CenterCreator(nombreCentres, 2);
            double[][] centers = new double[10][2];
            centers =  center.generateSpacedCenters(data);
            Mixture mix = new Mixture(data, centers);
            mix.epoque(50);
            sum += mix.score();
        }
        return sum/10;
    }

    public static void question4() throws IOException {
        double [][] data = dotDExtractor("generatedFiles/data/gmm_data.d");
        double[] Xs = new double[10];

        double[] scores = new double[10];
        for (int k = 1; k < 11; k +=1){
            Xs[k-1] = k;
            scores[k-1] = moyenneScore(k,data);
        }
        graph2D g2 = new graph2D("scoreOnNumberOfGaussians",Xs,scores);

        g2.execute();
    }

    public static void main(String[] arg) throws IOException {
    }


}