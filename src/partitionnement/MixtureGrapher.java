package partitionnement;

import java.io.FileWriter;
import java.io.IOException;

public class MixtureGrapher {
    Mixture m;
    String dataFile;
    String finalFile;
    String dataPath = "generated/data/";
    String gnuplotPath = "generated/gnuplot/";
    MixtureGrapher(Mixture m, String dataFile, String finalFile){
        this.m = m;
        this.dataFile = dataFile;
        this.finalFile = finalFile;
    }

    public void createCentersFile() throws IOException {
        FileWriter centersFile = new FileWriter(dataPath+finalFile+".gnu");
        double[][] centers = m.getMeans();
        double[][] sigmas = m.getSigma();
        double sigmaTempMean = 0;
        double[] densities = m.getRoh();

        for (int centerIdx = 0; centerIdx < centers.length; centerIdx += 1){
            for (int i = 0; i < centers[centerIdx].length; i  += 1){
                centersFile.write("" + centers[centerIdx][i] + " ");
                sigmaTempMean += sigmas[centerIdx][i] * sigmas[centerIdx][i];
            }
            centersFile.write("" + 10*Math.pow(sigmaTempMean,0.5) + " " +(int)(255 * densities[centerIdx]));
            sigmaTempMean = 0;
        }
    }

    public void createGnuPlot() throws IOException {
        FileWriter gnuplot = new FileWriter(gnuplotPath+finalFile+".gnu");
        gnuplot.write("rgb(r,g,b) = int(65536 * r) + int(256 * g) + int(b)\n");
        gnuplot.write("splot \"../data/" + dataFile + ".d\" using 1:2:3:(rgb($4,$5,$6)) with points lc rgb variable,\\ \n");
        gnuplot.write("splot \" ../data/" + finalFile + ".d\" using 1:2:3:4:(rgb($5,$5,$5)) pt 7 ps variable lc rgb variable");
        gnuplot.close();
    }

    public void createAllFiles() throws IOException {
        this.createCentersFile();
        this.createGnuPlot();
    }

}
