package acoalgorithm;

import model.Transaction;
import java.util.HashMap;
import java.util.Random;
import java.util.stream.IntStream;
import model.City;
import model.Commodity;
import economy.ProfitCalculator;
import java.util.Iterator;
import java.util.Map;


public class AntColonyOptimization {
    public String s="";
    private double startingTrailValue;             //initial value of trails
    private double alpha;           //pheromone importance
    private double beta;            //distance priority
    private double evaporation;
    private double Q;             //pheromone left on trail per ant
    private double antFactor;     //no of ants per node
    private double randomFactor; //introducing randomness
    private int maxIterations;

    private int numberOfAnts;
    private Ant[] ants;
    
    private Map<Transaction, Double> profitGraph;
    private Map<Transaction, Double> trailGraph;
    private Map<Transaction, Double> probabilities;
    private int numberOfNodes;
    
    private Random random = new Random();
    private int jumpIndex;

    private City[] cities;
    private Commodity[] commodities;
    private HashMap<Commodity, City[]> purchasePoints;
    private Transaction[] bestRouteOrder;
    private double bestRouteProfit;
    private double shipCapacity = 100;
    private final int numberOfJumps;
    
    public AntColonyOptimization(ACOParameters acoParameters, ACOModel acoModel,
            double shipCapacity, int nrOfJumps) {
        
        this.startingTrailValue = acoParameters.getStartingTrailValue();
        this.alpha = acoParameters.getAlpha();
        this.beta = acoParameters.getBeta();
        this.evaporation = acoParameters.getEvaporation();
        this.Q=acoParameters.getQ();
        this.antFactor = acoParameters.getAntFactor();
        this.randomFactor = acoParameters.getRandomFactor();
        this.maxIterations = acoParameters.getMaxIterations();
        this.cities = acoModel.getCities();
        this.commodities = acoModel.getCommodities();
        this.purchasePoints = acoModel.getPurchasePoints();
        this.numberOfJumps = nrOfJumps;
        
        this.profitGraph = generateProfitGraph();
        this.numberOfNodes = profitGraph.size();
        numberOfAnts = (int) (numberOfNodes * antFactor);

        this.trailGraph = new HashMap<Transaction, Double>();
        this.probabilities = new HashMap<Transaction, Double>();
        
        ants = new Ant[numberOfAnts];
        IntStream.range(0, numberOfAnts).forEach(i -> ants[i] = new Ant(nrOfJumps));
    }

    public Map<Transaction, Double> generateProfitGraph() {
        Map<Transaction, Double> graph = new HashMap<Transaction, Double>();
        for (City startPoint : cities) {
            Iterator<Commodity> soldCommoditiesIterator = 
                    startPoint.getSalesPrices().keySet().iterator();
            
            while(soldCommoditiesIterator.hasNext()) {
                Commodity tradedCommodity = soldCommoditiesIterator.next();
                City[] possibleBuyers =
                        this.purchasePoints.get(tradedCommodity);
                
                for(City endPoint : possibleBuyers) {
                    Transaction transaction = new Transaction(startPoint,
                                                        endPoint,
                                                        tradedCommodity);
                    
                    Double profit = ProfitCalculator.calculateProfit(startPoint, 
                                                                    endPoint,
                                                                    tradedCommodity,
                                                                    shipCapacity);
                    graph.put(transaction, profit);
                }
            }
        }
        
        return graph;
    }

    /**
     * Use this method to run the main logic
     */
    public Transaction[] solve() {
        clearTrails();
        for(int i = 0; i < maxIterations; i++) {
            setupAnts();
            moveAnts();
            updateTrails();
            updateBest();
        }
        printSolution();
        return bestRouteOrder.clone();
    }

    public void printSolution() {
        this.s = "";
        s+=("***********************************************" +
            "\nBest route total profit: " + bestRouteProfit +
            "\n*********************************************" + "\n");
        
        s+=("\nBest route order: \n");
        for(Transaction transaction : bestRouteOrder) {
            s+=("\n------------------------------------------\n"
               +"\nStart:         " + transaction.getStartPoint().getName()
               +"\nEnd:           " + transaction.getEndPoint().getName()
               +"\nCommodity:     " + transaction.getCommodity().getName()
               +"\nProfit:        " + profitGraph.get(transaction)
               +"\n");
        }
        s+="\n------------------------------------------\n";
        System.out.println(s);
    }
    /**
     * Prepare ants for the simulation
     */
    private void setupAnts() {
        for(int i = 0; i < numberOfAnts; i++) {
            for(Ant ant : ants) {
                ant.clear();
                ant.setCurrentCity(cities[random.nextInt(cities.length)]);
            }
        }
        jumpIndex = 0;
    }

    /**
     * At each iteration, move ants
     */
    private void moveAnts() {
        Transaction transaction;
        for(int i = jumpIndex; i < numberOfJumps; i++) {
            for(Ant ant : ants) {
                transaction = selectNextTransaction(ant);
                ant.performTransaction(transaction);
            }
            jumpIndex++;
        }
    }


    private Transaction selectNextTransaction(Ant ant) {
        City currentCity = ant.getCurrentCity();
        Commodity[] sales = currentCity.getSales();
        
        //If the randomFactor is fulfilled a random city is visited
        if (random.nextDouble() < randomFactor) {
            return generateRandomTransaction(currentCity);
        }
        
        calculateProbabilities(ant);
        double r = random.nextDouble();
        double total = 0;
        
        Iterator<Transaction> transactionsIterator = probabilities.keySet().iterator();
        
        while(transactionsIterator.hasNext()) {
            Transaction transaction = transactionsIterator.next();
            total += probabilities.get(transaction);
            
            if (total >= r) {
                return transaction;
            }
        }
        
        return generateRandomTransaction(currentCity);
    }

    private Transaction generateRandomTransaction(City currentCity) {
        int purchasingCitiesLength = 0;
        City randomCity;
        Commodity randomCommodity = null;
        City[] purchasingCities = null;
        Commodity[] sales = currentCity.getSales();
        
        while(purchasingCitiesLength == 0) {
            randomCommodity =
                    sales[random.nextInt(sales.length)];
            purchasingCities =
                    purchasePoints.get(randomCommodity);
            purchasingCitiesLength = purchasingCities.length;
        }
        
        randomCity = purchasingCities[random.nextInt(purchasingCities.length)];
        
        return new Transaction(currentCity, randomCity, randomCommodity);
    }

    /**
     * Calculate the next city picks probabilities
     */
    public void calculateProbabilities(Ant ant) {

        probabilities.clear();
        double pheromone = 0.0;
        
        pheromone = calculatePheromone(ant);
        City currentCity = ant.getCurrentCity();
        Commodity[] sales = currentCity.getSales();
        
        for(Commodity commodity : sales) {
            City[] possibleBuyers = purchasePoints.get(commodity);
            
            for(City destination : possibleBuyers) {
                double numerator;
                Transaction transaction = new Transaction(currentCity, destination, commodity);
                Double profit = profitGraph.get(transaction);

                if(profit < -0.1) {
                    numerator = Math.pow(trailGraph.get(transaction), alpha) * 
                        Math.pow(1 / -profit, beta);

                } else if(profit > 0.1){
                    numerator = Math.pow(trailGraph.get(transaction), alpha) * 
                        Math.pow(profit, beta);

                } else {
                    numerator = Math.pow(trailGraph.get(transaction), alpha);
                }
                
                probabilities.put(transaction, numerator / pheromone);
            }
        }
    }

    private double calculatePheromone(Ant ant) {
        City currentCity = ant.getCurrentCity();
        Commodity[] sales = currentCity.getSales();
        
        double pheromone = 0;
        for(Commodity commodity : sales) {
            City[] possibleBuyers = purchasePoints.get(commodity);
            
            for(City destination : possibleBuyers) {
                Transaction transaction = new Transaction(currentCity, destination, commodity);
                if(!ant.visited(transaction)) {
                    double profit = profitGraph.get(transaction);
                    
                    if(profit < -0.1) {
                        pheromone
                        += Math.pow(trailGraph.get(transaction), alpha) * 
                            Math.pow(1 / -profit, beta);

                    } else if(profit > 0.1){
                        pheromone
                        += Math.pow(trailGraph.get(transaction), alpha) * 
                            Math.pow(profit, beta);
                        
                    } else {
                        pheromone
                        += Math.pow(trailGraph.get(transaction), alpha);
                    }
                }
            }
        }
        
        return pheromone;
    }
    
    /**
     * Update trails that ants used
     */
    private void updateTrails() {
        Iterator<Transaction> trailsIterator = trailGraph.keySet().iterator();
        
        while(trailsIterator.hasNext()) {
            Transaction trail = trailsIterator.next();
            trailGraph.put(trail, trailGraph.get(trail) * evaporation);
        }
        
        for (Ant ant : ants) {
            double contribution = Q / ant.trailProfit(profitGraph);
            for (Transaction transaction : ant.trail) {
                if(trailGraph.containsKey(transaction)) {
                    trailGraph.put(transaction, trailGraph.get(transaction) + contribution);
                } else {
                    trailGraph.put(transaction, contribution);
                }
            }
        }
    }

    /**
     * Update the best solution
     */
    private void updateBest() {
        if (bestRouteOrder == null) {
            bestRouteOrder = ants[0].trail;
            bestRouteProfit = ants[0].trailProfit(profitGraph);
        }
        
        for (Ant a : ants) {
            if (a.trailProfit(profitGraph) > bestRouteProfit) {
                bestRouteProfit = a.trailProfit(profitGraph);
                bestRouteOrder = a.trail.clone();
            }
        }
    }

    /**
     * Clear trails after simulation
     */
    private void clearTrails() {
        trailGraph = new HashMap<Transaction, Double>();
        for (City startPoint : cities) {
            Iterator<Commodity> soldCommoditiesIterator = 
                    startPoint.getSalesPrices().keySet().iterator();
            
            while(soldCommoditiesIterator.hasNext()) {
                Commodity tradedCommodity = soldCommoditiesIterator.next();
                City[] possibleBuyers =
                        this.purchasePoints.get(tradedCommodity);
                
                for(City endPoint : possibleBuyers) {
                    Transaction transaction = new Transaction(startPoint,
                                                        endPoint,
                                                        tradedCommodity);
                    trailGraph.put(transaction, startingTrailValue);
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
