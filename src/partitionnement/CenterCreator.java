package partitionnement;

public class CenterCreator {
    int dimension;
    int centerCount;
    double centersLocation[][];
    public CenterCreator(int centerCount, int dimension){
        this.dimension = dimension;
        this.centerCount = centerCount;
        centersLocation = new double[centerCount][dimension];
    }
    public double[][]create(){
        return centersLocation;
    }
}
