package partitionnement;

import java.io.FileWriter;
import java.io.IOException;

public class graph2D {
    String dataName;
    double dataX[];
    double dataY[];
    String dataPath = "generatedFiles/data/";
    String gnuPath = "generatedFiles/gnuplot/";
    String graphPath = "../graphs/";

    public graph2D(String dataName, double dataX[], double dataY[]){
        this.dataName = dataName;
        this.dataX = dataX;
        this.dataY = dataY;
    }
    public void execute() throws IOException {
        FileWriter gnuplot = new FileWriter(gnuPath+dataName+".gnu");
        gnuplot.write("set terminal svg size 920,920 \n" +
                "set output '"+graphPath+dataName+".svg'\n");
        gnuplot.write("plot \"../data/" + dataName + ".d\"");
        gnuplot.close();
        FileWriter data = new FileWriter(dataPath + dataName + ".d");
        for (int i =0; i < dataX.length; i += 1){
            data.write("" + dataX[i] + " " + dataY[i]+ "\n");
        }
        data.close();
    }
}
