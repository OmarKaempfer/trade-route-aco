package acoalgorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.stream.IntStream;


public class AntColonyOptimization {
    public String s="";
    private double c;             //number of trails
    private double alpha;           //pheromone importance
    private double beta;            //distance priority
    private double evaporation;
    private double Q;             //pheromone left on trail per ant
    private double antFactor;     //no of ants per node
    private double randomFactor; //introducing randomness

    private int maxIterations;

    private int numberOfCities;
    private int numberOfAnts;
    private double graph[][];
    private double trails[][];
    private Ant[] ants;
    private Random random = new Random();
    private double probabilities[];

    private int currentIndex;

    private int[] bestTourOrder;
    private double bestTourLength;

    public AntColonyOptimization(double c, double alpha, double beta, double evaporation, 
            double Q, double antFactor, double randomFactor, int maxIter, int nrOfCities) {
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.Q=Q;
        this.antFactor = antFactor;
        this.randomFactor = randomFactor;
        this.maxIterations = maxIter;
        this.numberOfCities = nrOfCities;
                
        graph = generateRandomMatrix(numberOfCities);
        numberOfAnts = (int) (numberOfCities * antFactor);

        trails = new double[numberOfCities][numberOfCities];
        probabilities = new double[numberOfCities];
        
        ants = new Ant[numberOfAnts];
        IntStream.range(0, numberOfAnts).forEach(i -> ants[i] = new Ant(numberOfCities));
    }

    /**
     * Generate initial solution
     */
    public double[][] generateRandomMatrix(int n) {
        double[][] randomMatrix = new double[n][n];
        
        for(int i=0;i<n;i++) {
            for(int j=0;j<n;j++) {
                if(i==j)
                    randomMatrix[i][j]=0;
                else
                    randomMatrix[i][j]=Math.abs(random.nextInt(100)+1);
            }
        }
         
        s+=("\t");
        for(int i=0;i<n;i++)
            s+=(i+"\t");
        s+="\n";
        
        for(int i=0;i<n;i++) {
            s+=(i+"\t");
            for(int j=0;j<n;j++)
                s+=(randomMatrix[i][j]+"\t");
            s+="\n";
        }
        
        int sum=0;
        
        for(int i=0;i<n-1;i++)
            sum+=randomMatrix[i][i+1];
        sum+=randomMatrix[n-1][0];
        s+=("\nNaive solution 0-1-2-...-n-0 = "+sum+"\n");
        System.out.println(s);
        return randomMatrix;
    }

    /**
     * Perform ant optimization
     */
    public void startAntOptimization() {
        for(int i=1;i<=5;i++) {
            s+=("\nAttempt #" +i);
            solve();
            s+="\n";
        }
    }

    /**
     * Use this method to run the main logic
     */
    public int[] solve() {
        setupAnts();
        clearTrails();
        for(int i=0; i < maxIterations; i++) {
            moveAnts();
            updateTrails();
            updateBest();
        }
        s+=("\nBest tour length: " + (bestTourLength - numberOfCities));
        s+=("\nBest tour order: " + Arrays.toString(bestTourOrder));
        return bestTourOrder.clone();
    }

    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        for(int i = 0; i < numberOfAnts; i++) {
            for(Ant ant : ants) {
                ant.clear();
                ant.visitCity(-1, random.nextInt(numberOfCities));
            }
        }
        currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        for(int i=currentIndex;i<numberOfCities-1;i++) {
            for(Ant ant:ants) {
                ant.visitCity(currentIndex,selectNextCity(ant));
            }
            currentIndex++;
        }
    }


    private int selectNextCity(Ant ant) {
        int t = random.nextInt(numberOfCities - currentIndex);
        if (random.nextDouble() < randomFactor) {
            if(!ant.visited(t)) {
                return t;
            }
        }
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        
        for (int i = 0; i < numberOfCities; i++) {
            total += probabilities[i];
            if (total >= r) 
                return i;
        }
        throw new RuntimeException("There are no other cities");
    }

    /**
     * Calculate the next city picks probabilities
     */
    public void calculateProbabilities(Ant ant) {
        int i = ant.trail[currentIndex];
        double pheromone = 0.0;
        for (int l = 0; l < numberOfCities; l++) {
            if (!ant.visited(l)) 
                pheromone += Math.pow(trails[i][l], alpha) * Math.pow(1.0 / graph[i][l], beta);
        }
        for (int j = 0; j < numberOfCities; j++) {
            if (ant.visited(j)) 
                probabilities[j] = 0.0;
            else {
                double numerator = Math.pow(trails[i][j], alpha) * Math.pow(1.0 / graph[i][j], beta);
                probabilities[j] = numerator / pheromone;
            }
        }
    }

    /**
     * Update trails that ants used
     */
    private void updateTrails() {
        for (int i = 0; i < numberOfCities; i++) {
            for (int j = 0; j < numberOfCities; j++) 
                trails[i][j] *= evaporation;
        }
        for (Ant a : ants) {
            double contribution = Q / a.trailLength(graph);
            for (int i = 0; i < numberOfCities - 1; i++)
                trails[a.trail[i]][a.trail[i + 1]] += contribution;
            trails[a.trail[numberOfCities - 1]][a.trail[0]] += contribution;
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest() {
        if (bestTourOrder == null) {
            bestTourOrder = ants[0].trail;
            bestTourLength = ants[0].trailLength(graph);
        }
        
        for (Ant a : ants) {
            if (a.trailLength(graph) < bestTourLength) 
            {
                bestTourLength = a.trailLength(graph);
                bestTourOrder = a.trail.clone();
            }
        }
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails() {
        for(int i=0;i<numberOfCities;i++) {
            for(int j=0;j<numberOfCities;j++)
                trails[i][j]=c;
        }
    }
}
