package acoalgorithm;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.OptionalInt;
import java.util.Random;
import java.util.Set;
import java.util.stream.IntStream;
import model.City;
import model.Commodity;
import model.ProfitCalculator;


public class AntColonyOptimization {
    public String s="";
    private double c;             //initial value of trails
    private double alpha;           //pheromone importance
    private double beta;            //distance priority
    private double evaporation;
    private double Q;             //pheromone left on trail per ant
    private double antFactor;     //no of ants per node
    private double randomFactor; //introducing randomness

    private int maxIterations;

    private int numberOfCities;
    private int numberOfAnts;
    private double[][][] graph;
    private double[][][] trails;
    private Ant[] ants;
    private Random random = new Random();
    private double probabilities[][];

    private int currentIndex;

    private City[] cities;
    private Commodity[] commodities;
    private HashMap<Commodity, City[]> purchasePoints;
    private int[][] bestRouteOrder;
    private double bestRouteProfit;
    private double shipCapacity = 100;

    public AntColonyOptimization(double c, double alpha, double beta, double evaporation, 
            double Q, double antFactor, double randomFactor, int maxIter, City[] cities) {
        this.c = c;
        this.alpha = alpha;
        this.beta = beta;
        this.evaporation = evaporation;
        this.Q=Q;
        this.antFactor = antFactor;
        this.randomFactor = randomFactor;
        this.maxIterations = maxIter;
        this.numberOfCities = cities.length;
        this.cities = cities;
        this.graph = generateGraphMatrix();
        numberOfAnts = (int) (numberOfCities * antFactor);

        this.trails = initializeTrails();
        
        ants = new Ant[numberOfAnts];
        IntStream.range(0, numberOfAnts).forEach(i -> ants[i] = new Ant(numberOfCities, cities, purchasePoints));
    }

    protected double[][][] initializeTrails() {
        double[][][] trails = new double[numberOfCities][][];
        for (int i = 0; i < numberOfCities; i++) {
            trails[i] = new double[cities[i].getSales().keySet().size()][];
            for (int j = 0; j < trails[i].length; j++) {
                trails[i][j]
                        = new double[purchasePoints.get(cities[i].getSales().keySet()
                                .stream().toArray(Commodity[]::new)[j]).length];
            }
        }
        return trails;
    }
    
    
        /**
     * Generate initial solution
     */
    public double[][][] generateGraphMatrix() {
        int n = numberOfCities;
        
        double[][][] graph = new double[n][][];
        HashMap<Commodity, Double> sales;
        HashMap<Commodity, Double> purchases;
        Commodity[] commoditiesSold;
        City currentCity;
        City[] purchasingCities;
        
        
        //The profit of each possible trade in each station is calculated
        //First we iterate through the stations
        for(int i = 0; i < n; i++) {
            currentCity = cities[i];
            sales = currentCity.getSales();
            commoditiesSold = sales.keySet().stream().toArray(Commodity[] ::new); 
            
            double[][] commoditiesProfit = new double[sales.size()][];
            
            //for each commodity the current city sells we calculate the profit
            //for selling it in each city that buys it
            for(int j = 0; j < commoditiesSold.length; j++) {
                purchasingCities = this.purchasePoints.get(commoditiesSold[j]);
                double[] citySpecificProfit = new double[purchasingCities.length];
                
                //we calculate the profit for the current commodity in each
                //station that buys it
                for(int k = 0; k < purchasingCities.length; k++) {
                    citySpecificProfit[k] = ProfitCalculator.calculateProfit(currentCity, 
                                purchasingCities[j], commoditiesSold[j], this.shipCapacity);
                }
                commoditiesProfit[j] = citySpecificProfit;
            }
            graph[i] = commoditiesProfit;
        }
        
        return graph;
    }
    
    
    protected static int getIndexOf(Object[] array, Object element) {
        for(int i = 0; i < array.length; i++) {
            if(array[i] == element) {
                return i;
            }
        }
        return -1;
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
    public int[][] solve() {
        setupAnts();
        clearTrails();
        for(int i=0; i < maxIterations; i++) {
            setupAnts();
            moveAnts();
            updateTrails();
            updateBest();
        }
        s+=("\nBest route profit: " + bestRouteProfit);
        s+=("\nBest route order: " + Arrays.toString(bestRouteOrder));
        System.out.println(s);
        return bestRouteOrder.clone();
    }

    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        for(int i = 0; i < numberOfAnts; i++) {
            for(Ant ant : ants) {
                ant.clear();
                ant.tradeWith(-1, -1, random.nextInt(numberOfCities));
            }
        }
        currentIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        int[] selection;
        for(int i = currentIndex; i < numberOfCities; i++) {
            for(Ant ant : ants) {
                selection = selectNextCity(ant);
                ant.tradeWith(currentIndex, selection[0], selection[1]);
            }
            currentIndex++;
        }
    }


    private int[] selectNextCity(Ant ant) {

        
        //If the randomFactor is fulfilled a random city is visited
        if (random.nextDouble() < randomFactor) {
            int randomCommodity = 
                random.nextInt(ant.getCurrentCity().getSales().keySet().size());
        
            City[] purchasingCities = purchasePoints.get(randomCommodity);
            int randomCity = random.nextInt(purchasingCities.length);
            return new int[] {randomCommodity, randomCity};
        }
        
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        
        for (int j = 0; j < probabilities.length; j++) {
            for (int k = 0; k < probabilities[j].length; k++) {
                total += probabilities[j][k];
                
                if (total >= r) 
                return new int[] {j, k};
            }
        }
        
        int randomCommodity = 
            random.nextInt(ant.getCurrentCity().getSales().keySet().size());

        City[] purchasingCities = purchasePoints.get(randomCommodity);
        int randomCity = random.nextInt(purchasingCities.length);
        return new int[] {randomCommodity, randomCity};
    }

    /**
     * Calculate the next city picks probabilities
     */
    public void calculateProbabilities(Ant ant) {
        City currentCity = ant.getCurrentCity();
        int currentCityIndex = getIndexOf(cities, currentCity);
        Commodity[] commodities = currentCity.getSales().keySet().stream().toArray(Commodity[] ::new);
        City[] currentCommodityPP;
        double pheromone = 0.0;
        probabilities = new double[commodities.length][];
        
        for(int j = 0; j < commodities.length; j++) {
            currentCommodityPP = purchasePoints.get(commodities[j]);
            probabilities[j] = new double[currentCommodityPP.length];
            
            for(int k = 0; k < currentCommodityPP.length; k++) {
                if(!ant.visited(currentCityIndex, j, k)) {
                    if(graph[currentCityIndex][j][k] < -0.1) {
                        pheromone
                        += Math.pow(trails[currentCityIndex][j][k], alpha) * 
                            Math.pow(1 / -graph[currentCityIndex][j][k], beta);

                    } else if(graph[currentCityIndex][j][k] > 0.1){
                        pheromone
                        += Math.pow(trails[currentCityIndex][j][k], alpha) * 
                            Math.pow(graph[currentCityIndex][j][k], beta);
                    } else {
                        pheromone
                        += Math.pow(trails[currentCityIndex][j][k], alpha);
                    }
                }
            }
        }
             
        for(int j = 0; j < commodities.length; j++) {
            currentCommodityPP = purchasePoints.get(commodities[j]);
            
            for(int k = 0; k < currentCommodityPP.length; k++) {
                double numerator = Math.pow(trails[currentCityIndex][j][k], alpha) * Math.pow(1.0 / graph[currentCityIndex][j][k], beta);
                probabilities[j][k] = numerator / pheromone;
            }
        }
    }

    /**
     * Update trails that ants used
     */
    private void updateTrails() {
        for (int i = 0; i < trails.length; i++) {
            for (int j = 0; j < trails[i].length; j++) 
                for(int k = 0; k < trails[j].length; k++)
                    trails[i][j][k] *= evaporation;
        }
        
        for (Ant a : ants) {
            double contribution = Q / a.trailProfit(graph);
            for (int i = 0; i < a.trailSize - 1; i++) {
                trails[a.trail[i + 1][2]][a.trail[i + 1][0]][a.trail[i + 1][1]] 
                        += contribution;
            }
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest() {
        if (bestRouteOrder == null) {
            bestRouteOrder = ants[0].trail;
            bestRouteProfit = ants[0].trailProfit(graph);
        }
        
        for (Ant a : ants) {
            if (a.trailProfit(graph) > bestRouteProfit) 
            {
                bestRouteProfit = a.trailProfit(graph);
                bestRouteOrder = a.trail.clone();
            }
        }
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails() {
        for(int i = 0; i < trails.length; i++) {
            for(int j = 0; j < trails[i].length; j++) {
                for(int k = 0; k < trails[i][j].length; k++) {
                    trails[i][j][k] = c;
                }
            }
        }
    }
    
    public void setCities(City[] cities) {
        this.cities = cities;
    }
    
    public void setCommodities(Commodity[] commodities) {
        this.commodities = commodities;
    }
    
    public void setPurchasePoints(HashMap<Commodity, City[]> purchasePoints) {
        this.purchasePoints = purchasePoints;
    }
    
    public HashMap<Commodity, City[]> getPurchasePoints() {
        return this.purchasePoints;
    }
    
    public City[] getCities() {
        return this.cities;
    }
    
    public Commodity[] getCommodities() {
        return this.commodities;
    }
}
