package acoalgorithm;

public class ACOParameters {
    private final double startingTrailValue;             //initial value of trails
    private final double alpha;                          //pheromone importance
    private final double beta;                           //distance priority
    private final double evaporation;
    private final double Q;                              //pheromone left on trail per ant
    private final double antFactor;                      //no of ants per node
    private final double randomFactor;                   //introducing randomness
    private final int maxIterations;

    public ACOParameters() {
        this.startingTrailValue = 0;
        this.alpha = 1;
        this.beta = 5;
        this.evaporation = 0.5;
        this.Q = 500;
        this.antFactor = 0.8;
        this.randomFactor = 0.01;
        this.maxIterations = 3000;
    }
    /**
     * 
     * @param startingTrailValue the value we use to initialize the trails
     * @param alpha  the weight of the ant trails ant trails in the probability calculation
     * @param beta the weight of the profit in the probability calculation
     * @param evaporation the rate at which we decrease the total left pheromone
     * @param Q the total contribution of pheromone an ant can make
     * @param antFactor the amount of ants per node
     * @param randomFactor chance of making a random transaction
     * @param maxIterations max number of iterations
     */
    public ACOParameters(double startingTrailValue, double alpha, double beta, 
            double evaporation, double Q, double antFactor, double randomFactor, 
            int maxIterations) {
        
        this.startingTrailValue = startingTrailValue;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.Q = Q;
        this.antFactor = antFactor;
        this.randomFactor = randomFactor;
        this.maxIterations = maxIterations;
    }

    public double getStartingTrailValue() {
        return startingTrailValue;
    }

    public double getAlpha() {
        return alpha;
    }

    public double getBeta() {
        return beta;
    }

    public double getEvaporation() {
        return evaporation;
    }

    public double getQ() {
        return Q;
    }

    public double getAntFactor() {
        return antFactor;
    }

    public double getRandomFactor() {
        return randomFactor;
    }

    public int getMaxIterations() {
        return maxIterations;
    }
    
}
