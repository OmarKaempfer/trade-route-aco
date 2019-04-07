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
