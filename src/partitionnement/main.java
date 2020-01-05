package partitionnement;

import java.io.IOException;

public class main {

    public static void question1() throws IOException {
        ImageToExtract ext = new ImageToExtract("mms");
        double[][] point = ext.extractAllPixelsColorNormalized();
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

    public static void main(String[] arg) throws IOException {
        question1();
    }


}
