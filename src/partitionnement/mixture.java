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
    private double[] Rk = new double[this.centers.length];

    /**
     * constructor
     * @param point
     * @param center
     * @param sigma
     * @param roh
     * @param means
     */
    public mixture(double[][] point, double[][]center , double[][] sigma, double[]roh, double[][]means){
        this.points = point;
        this.centers = center;
        this.pointDimension = point[0].length;
        this.sigma = sigma;
        this.roh = roh;
        this.means = means;
    }

    /**
     * constructor
     * @param point
     * @param center
     */
    public mixture(double[][] point, double[][] center){
        this.points = point;
        this.centers = center;
        this.pointDimension = point[0].length;
        for(int i = 0; i < this.means.length; i++){
            for(int j = 0; j < this.means[0].length; j++){
                this.means[i][j] = (double)(Math.random() * (1 - 0));
            }
        }
        for(int i = 0; i < this.sigma.length; i++){
            for(int j = 0; j <this.sigma[0].length; j++){
                this.sigma[i][j] = (double)(Math.random() * (5 - 1));
            }
        }
        for(int i = 0; i < this.roh.length; i++){
            roh[i] = Math.random() * (1 - 0);
        }


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
    private void majM(double[][] assignment){
        for(int centeridx = 0; centeridx < this.centers.length; centeridx++){
            //maj means
            for(int dimidx = 0; dimidx < this.pointDimension; dimidx++){
                double summeans = 0;
                for(int pointidx = 0;  pointidx < this.points.length; pointidx++){
                    summeans += assignment[pointidx][centeridx] * this.points[pointidx][dimidx];
                }
                this.means[centeridx][dimidx] = summeans/this.Rk[centeridx];
            }

        }
    }

    /**
     * process the maj of sandard deviation
     * @param assignment
     */
    private void majS(double[][] assignment){
        for(int centeridx = 0; centeridx < this.centers.length; centeridx++){
            // maj sigma
            for(int dimidx = 0; dimidx < this.pointDimension; dimidx++){
                double sumsigma = 0;
                for(int pointidx = 0;  pointidx < this.points.length; pointidx++){
                    sumsigma += assignment[pointidx][centeridx] * Math.pow(this.points[pointidx][dimidx] - this.means[centeridx][dimidx], 2);
                }
                this.sigma[centeridx][dimidx] = Math.pow(sumsigma/this.Rk[centeridx],0.5);
            }

        }
    }


    /**
     * process the maj of density
     */
    private void majR(){

        for(int centeridx = 0; centeridx < this.centers.length; centeridx++){
            //maj roh
            this.roh[centeridx] = this.Rk[centeridx]/this.points.length;
        }

    }


    /**
     * process the maj Rk
     * @param assignment
     */
    private void majRk(double[][] assignment){
        //Rk
        for(int centeridx = 0; centeridx < this.centers.length; centeridx++){
            this.Rk[centeridx] = 0;
            for(int pointidx = 0; pointidx < this.points.length; pointidx++){
                this.Rk[centeridx] += assignment[pointidx][centeridx];
            }

        }

    }

    /**
     * process the maj of all parametter
     * @param assignment
     */
    private void maj(double[][] assignment){
        majRk(assignment);
        majM(assignment);
        majS(assignment);
        majR();

    }


    /**
     * process at nb maj on the data
     * @param nb
     */
    public void epoque(int nb){
        for(int i = 0; i < nb; i++){
            double[][] assign = this.assign();
            this.maj(assign);
        }
    }


    /**
     * get the score
     * @return the score
     */
    public double score(){
        double[] score = new double[this.points.length];
        double res = 0;
        for(int d =  0; d < this.points.length; d++){
            double sum = 0;
            for(int k = 0; k < this.centers.length; k++){
                sum += this.roh[k] * prod(this.points[d], k);
            }
            score[d] = Math.log(sum);
            res += score[d];
        }
        return res/this.points.length;
    }

    /**
     * getter
     * @return standar deviation
     */
    private double[][] getSigma(){
        return this.sigma;
    }

    /**
     * getter
     * @return means
     */
    private double[][] getMeans(){
        return this.means;
    }

    /**
     * getter
     * @return density
     */
    private double[] getRoh(){
        return this.roh;
    }
}
