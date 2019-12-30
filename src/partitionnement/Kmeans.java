package partitionnement;

import java.util.Random;

public class Kmeans {
    private double [][] points;
    int pointDimension;

    private Random rdGenerator;

    /**
     * variables that varies during the class execution.
     *
     *
     *  pointToCentersPointers :
     *      for a point of a same index in points, gives the center index it points to.
     */
    int [] pointToCentersPointers;
    private double [][] centers;

    /**
     * not so imortantVariables.
     */
    private final double powerDistance = 2;
    private double radiusForCenters;


    public Kmeans(double points[][], double[][] centers) {
        this.pointDimension = points[0].length;
        this.points = points;
        this.pointToCentersPointers = new int[this.points.length];
        this.centers = centers;
    }

    public Kmeans(double points[][], int numberOfCenters,double radiusForCenters){
        this(points,null);
        centers = new double[this.centers.length][this.pointDimension];
        for (int centerIndex = 0; centerIndex < numberOfCenters; centerIndex += 1){
            for (int pointCoord = 0; pointCoord < pointDimension; pointCoord += 1) {
                centers[centerIndex][pointCoord] =
                        (rdGenerator.nextDouble() * radiusForCenters) - radiusForCenters/2;
            }
        }
    }


    public Kmeans(double points[][], int numberOfCenters){
        this(points,numberOfCenters,200);
    }

    public void printPoints(){

        for(int pointIndex=0; pointIndex<points.length; pointIndex++){
            for (int coordIndex = 0; coordIndex < points[pointIndex].length; coordIndex+= 1){
                System.out.print(points[pointIndex][coordIndex]+"; ");
            }
            System.out.println();
        }
    }
    private double distance(double pointA[],double pointB[]){
        double sum = 0;
        for (int coordIndex = 0; coordIndex < pointDimension; coordIndex += 1) {
            sum += Math.pow(Math.abs(pointA[coordIndex] - pointB[coordIndex]), powerDistance);
        }
        return Math.pow(sum,1.f/powerDistance);
    }
    private int getClosestIndex(double point[], double pointsB[][]){
        int minIndex = 0;
        double minDistance = distance(pointsB[0],point);
        System.out.print("{"+pointsB[0][0]+","+pointsB[0][1] +"},{"+point[0]+","+point[1]+"},"+minDistance);

        for (int pointsIndex = 1; pointsIndex < pointDimension; pointsIndex += 1){
            System.out.print("{"+pointsB[pointsIndex][0]+","+pointsB[pointsIndex][1] +"},{"+point[0]+","+point[1]+"},"+distance(pointsB[pointsIndex],point));
            if (minDistance > distance(pointsB[pointsIndex],point)){
                minDistance = distance(pointsB[pointsIndex],point);
                minIndex = pointsIndex;
            }
        }
        System.out.println("\nMin distance = " + minDistance);
        return minIndex;
    }

    private void updatePTCP(){
        for (int pointIndex = 0; pointIndex < pointToCentersPointers.length; pointIndex += 1){
            pointToCentersPointers[pointIndex] = getClosestIndex(points[pointIndex],centers);
        }
    }

    private double updateCentersPos() {
        double[][] newCenters = new double[this.centers.length][pointDimension];
        double summedCenterToNewDistance = 0;
        int [] numberOfLinkedPoints= new int[this.centers.length];
        for (int pointIndex = 0; pointIndex < points.length; pointIndex += 1) {
            int centerIndex = pointToCentersPointers[pointIndex];
            numberOfLinkedPoints[centerIndex] += 1;
            for (int pointCoordIndex = 0; pointCoordIndex < pointDimension; pointCoordIndex += 1) {
                newCenters[centerIndex][pointCoordIndex] += points[pointIndex][pointCoordIndex];
            }

        }

        for (int centerIndex = 0; centerIndex < this.centers.length; centerIndex += 1) {
            for (int coordIndex = 0; coordIndex < pointDimension; coordIndex+= 1){
                newCenters[centerIndex][coordIndex] /= (double)numberOfLinkedPoints[centerIndex];
            }
            summedCenterToNewDistance += distance(centers[centerIndex], newCenters[centerIndex]);
        }
        centers = newCenters;
        return summedCenterToNewDistance;
    }

    private static void littleTeacherTest(){
        // creation d'un jeu de donnes simples pour tester l'algo
        int D=2; // deux dimensions
        int k=2; // deux centres
        double[][] X = new double[6][D]; // 6 points en D dimensions
        double[][] centres = new double[k][D];

        centres[0][0] = -1; centres[1][0] = 1;
        centres[0][1] = 0; centres[1][1] = 0;

        // position des donnees
        X[0][0] = -3;   X[0][1] = 1;
        X[1][0] = -2.5; X[1][1] = -0.5;
        X[2][0] = -4;   X[2][1] = 0;
        X[3][0] = 2;    X[3][1] = 2;
        X[4][0] = 2.5;  X[4][1] = -0.5;
        X[5][0] = 1.5;  X[5][1] = -1;
        Kmeans kmeans = new Kmeans(X,centres);
        kmeans.printPoints();
        double eps=0.001;
        double maj = 10;
        while(maj>eps) {
            System.out.println(maj + ">"+eps);
            kmeans.updatePTCP();
            maj = kmeans.updateCentersPos();
            for(int i=0; i<X.length; i++) {
                System.out.println("Pt "+i+" assigné à "+kmeans.pointToCentersPointers[i]);
            }
            for(int i=0; i<centres.length; i++) {
                System.out.println("Pos centre "+i+": "+kmeans.centers[i][0]+" "+kmeans.centers[i][1]);
            }

        }
        System.out.println(maj + "<" + eps);
// verification
        for(int i=0; i<X.length; i++) {
            System.out.println("Pt "+i+" assigné à "+kmeans.pointToCentersPointers[i]);
        }
        for(int i=0; i<centres.length; i++) {
            System.out.println("Pos centre "+i+": "+kmeans.centers[i][0]+" "+kmeans.centers[i][1]);
        }
    }


    public static void main(String[] args) {
        littleTeacherTest();
    }

}
