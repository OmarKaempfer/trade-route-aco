package economy;

import model.City;
import model.Commodity;
import model.Location;

public class ProfitCalculator {
    
    private static final double COST_PER_DST_UNIT = 1e-7;
    
    /**
     * 
     * @param start a 3 dimensional point
     * @param end a 3 dimensional point
     * @return the distance between the two given points
     */
    public static double calculateDistance(Location start, Location end) {
        return Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + 
                Math.pow(end.getY() - start.getY(), 2) + 
                Math.pow(end.getZ() - start.getZ(), 2));
    }
    
    /**
     * Calculates the profit for the given start, end and commodity, taking into
     * consideration the distance between the points and the profit generated
     * with the trading goods.
     * 
     * @param start
     * @param end
     * @param commodity
     * @param amount
     * @return profit of the transaction
     */
    public static double calculateProfit(City start, City end, Commodity commodity,
                                        double amount) {
        
        double saleProfit = (end.getBuyingPrice(commodity) - 
                            start.getSellingPrice(commodity)) * amount;
        
        double travellingCost = calculateTravellingCost(start.getLocation(), 
                                                        end.getLocation());
        
        return saleProfit - travellingCost;
    }
    
    /**
     * 
     * @param start
     * @param end
     * @return cost for travelling the distance between the given locations
     */
    public static double calculateTravellingCost(Location start, Location end) {
        
        return calculateDistance(start, end) * COST_PER_DST_UNIT;
    }
}
