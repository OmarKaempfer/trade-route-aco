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

    /**
     * Generates the profit graph for the current aco model, this graph links
     * each transaction with its profit, calculated taking into consideration
     * the distance and the goods prices.
     * @return the profit graph
     */
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
     * Main ant colony algorithm logic. With the current aco parameters and aco
     * model it tries to find the most profitable route and prints the solution.
     * @return transactions an array with the best route order found
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

    /**
     * Prints the current best total profit and the best route order.
     */
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
     * Clears each ant current values and initializes each ant to a random city.
     * Now the ants are ready for a simulation.
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
     * Moves the ants making a step in the algorithm. A complete transactions
     * route is performed by each ant.
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

    /**
     * Selects the next transaction to be performed by the given ant. The next
     * transaction will be picked according to the different probabilities
     * calculated with the ant trails and the profit of the transaction. If the
     * random factor is fulfilled, a random transaction will be selected,
     * disregarding the calculated probabilities.
     * 
     * @param ant the ant for which we want its next transaction
     * @return transaction
     */
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

    /**
     * Generates a random valid transaction given the currentCity.
     * @param currentCity the city for which we want a random transaction
     * @return randomTransaction valid random transaction
     */
    private Transaction generateRandomTransaction(City currentCity) {
        int purchasingCitiesLength = 0;
        City randomCity;
        Commodity randomCommodity = null;
        City[] purchasingCities = null;
        Commodity[] sales = currentCity.getSales();
        
        while(purchasingCitiesLength == 0) {
            randomCommodity = sales[random.nextInt(sales.length)];
            purchasingCities = purchasePoints.get(randomCommodity);
            purchasingCitiesLength = purchasingCities.length;
        }
        
        randomCity = purchasingCities[random.nextInt(purchasingCities.length)];
        
        return new Transaction(currentCity, randomCity, randomCommodity);
    }

    /**
     * Calculates the probability for each transaction, it stores those
     * probabilities in the 'probabilities' variable. The result is a graph
     * which links each transaction with its probability of being picked.
     * 
     * @param ant the ant which probabilities we want to calculate
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

    /**
     * Calculates the contribution of the total overall pheromones of the other
     * ants.
     * @param ant
     * @return pheromone 
     */
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
     * Updates the trails used by the ants with their new pheromone values,
     * for each ant that left an specific trail we add its contribution.
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
     * Updates the best solution by storing the best ant trail found and the
     * best total profit found.
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
