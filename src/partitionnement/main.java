package partitionnement;

import java.io.*;

public class main {

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
        for (int i =0; i < 20; i += 1){
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
        for (int i = 0; i < data.length; i +=1){
            for (int k = 0; k < data[i].length; k += 1){
                System.out.print(""+data[i][k] + " ");
            }
            System.out.println("");
        }
        double[] scores = new double[10];
        FileWriter fw = new FileWriter("generatedFiles/data/deuxDims.d");
        for (int k = 1; k < 11; k +=1){
            fw.write(""+moyenneScore(k,data));
        }

    }

    public static void main(String[] arg) throws IOException {
        question4();
    }


}
