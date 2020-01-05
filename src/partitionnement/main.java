package partitionnement;

import java.io.IOException;

public class main {
    public static void main(String[] arg) throws IOException {
        ImageToExtract ext = new ImageToExtract("mms");
        double[][] point = ext.extractAllPixelsColorNormalized();
        CenterCreator center = new CenterCreator(10, 3);
        double[][] centers = new double[10][3];
        float[] minlim = new float[3];
        float[] maxlim = new float[3];
        for (int i = 0;  i < 3; i++){
            minlim[i] = 0.0f;
            maxlim[i] = 1.0f;
        }
        centers =  center.generateSpacedCenters(point);
        Mixture mix = new Mixture(point, centers);
        mix.epoque(50);
        for(int i = 0; i < mix.getMeans().length; i++){
            System.out.println("");
            System.out.print("le centre " + i + " a pour coordonnée: ");
            for(int j = 0; j < mix.getMeans()[0].length; j++){
                System.out.print(mix.getMeans()[i][j] + ", ");
            }
        }
    }
}
