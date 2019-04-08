package acoalgorithm;

import model.Transaction;
import java.util.Map;
import model.City;

public class Ant {
    protected int trailSize;
    protected int nrOfJumps;
    protected City currentCity;
    protected Transaction[] trail;
    
    
    public Ant(int nrOfJumps) {
        this.trailSize = 0;
        this.nrOfJumps = nrOfJumps;
        this.trail = new Transaction[nrOfJumps];
    }
    /**
     * Performs the given transaction, increasing the ant trail size
     * and storing the transaction in its trail
     * @param transaction 
     */
    protected void performTransaction(Transaction transaction) {
        if(trailSize < nrOfJumps) {
            trail[trailSize] = transaction;
            currentCity = transaction.getEndPoint();
            trailSize++;
        }
    }

    /**
     * Returns whether the given transaction has already been done or not
     * @param transaction
     * @return 
     */
    protected boolean visited(Transaction transaction) {
        for(Transaction antTransaction : trail) {
            if(transaction.equals(antTransaction)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates the current ant trail total profit
     * @param graph
     * @return profit the total trail profit
     */
    protected double trailProfit(Map<Transaction, Double> graph) {
        double profit = 0;
        
        for(Transaction transaction : trail) {
            if(transaction != null) {
                profit += graph.get(transaction);
            }
        }
        
        return profit;
    }

    protected void clear() {
        this.trail = new Transaction[nrOfJumps];
        this.trailSize = 0;
    }
    
    protected City getCurrentCity() {
        return currentCity;
    }
    
    protected void setCurrentCity(City city) {
        this.currentCity = city;
    }
}