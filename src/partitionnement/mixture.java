package partitionnement;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;


public class mixture {
    private double [][] points;
    private int pointDimension;
    private double [][] centers;
    private double[][] sigma;
    private double[] roh;
    private double[][] means;


    public mixture(double[][] point, double[][]center , double[][] sigma, double[]roh, double[][]means){
        this.points = point;
        this.centers = center;
        this.pointDimension = point[0].length;
        this.sigma = sigma;
        this.roh = roh;
        this.means = means;
    }


    /**
     * function here for help in the function prob
     * @param point
     * @param k
     * @return
     */
    private double prod(double[] point, int k){
        double prod = 1;
        for(int i = 0; i < this.pointDimension; i++){
            prod *= 1/Math.sqrt(2 * Math.PI * Math.pow(this.sigma[k][i], 2)) * (Math.exp(Math.pow(point[i] - this.means[k][i], 2))/(2 * Math.pow(this.sigma[k][i], 2)));
        }
        return prod;
    }

    /**
     * take a point and a number of center en process the probability for this point to be associate at this center
     * @param point
     * @param k
     * @return the probability a double
     */
     private double prob(double[] point, int k){
        double somme = 0;
        for(int i = 0; i < this.centers.length; i++ ){
            somme += this.roh[i] * prod(point, i);
        }
        double res =  (this.roh[k] * prod(point, k))/somme;
        return res;
    }

    /**
     * fonction process the probability of each point to be associate to a center
     * @return a 2 dimension double tab where each sub tab represent the prpbability for a point to be associate to each center
     */
    private double [][] assign(){
        double[][] assign = new double[this.points.length][this.centers.length];
        for(int i = 0; i < this.points.length; i++){
            for(int j = 0; j < this.centers.length; j++){
                assign[i][j] = prob(this.points[i], j);
            }
        }
        return assign;
    }

    /**
     * the function maj the diffrent mixture parameter's
     * @param assignment
     */
    private void maj(double[][] assignment){
        //Rk
        double[] Rk = new double[this.centers.length];
        for(int centeridx = 0; centeridx < this.centers.length; centeridx++){
            Rk[centeridx] = 0;
            for(int j = 0; j < this.points.length; j++){
                Rk[centeridx] += assignment[j][centeridx];
            }
            //maj roh
            this.roh[centeridx] = Rk[centeridx]/this.points.length;

            //maj means
            // maj sigma
            for(int dimidx = 0; dimidx < this.pointDimension; dimidx++){
                double summeans = 0;
                double sumsigma = 0;
                for(int pointidx = 0;  pointidx < this.points.length; pointidx++){
                    summeans += assignment[pointidx][centeridx] * this.points[pointidx][dimidx];
                    sumsigma += assignment[pointidx][centeridx] * Math.pow(this.points[pointidx][dimidx] - this.means[centeridx][dimidx], 2);
                }
                this.sigma[centeridx][dimidx] = Math.pow(sumsigma/Rk[centeridx],0.5);
                this.means[centeridx][dimidx] = summeans/Rk[centeridx];
            }

        }

    }

}
