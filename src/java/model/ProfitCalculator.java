package model;

public class ProfitCalculator {
    
    private static final double COST_PER_DST_UNIT = 1;
    
    public static double calculateDistance(Location start, Location end) {
        return Math.sqrt(Math.pow(end.getX() - start.getX(), 2) + 
                Math.pow(end.getY() - start.getY(), 2) + 
                Math.pow(end.getZ() - start.getZ(), 2));
    }
    
    public static double calculateProfit(City start, City end, Commodity commodity,
                                        double amount) {
        
        double saleProfit = (end.getBuyingPrice(commodity) - 
                            start.getSellingPrice(commodity)) * amount;
        
        double travellingCost = calculateTravellingCost(start.getLocation(), 
                                                        end.getLocation());
        
        return saleProfit - travellingCost;
    }
    
    public static double calculateTravellingCost(Location start, Location end) {
        
        return calculateDistance(start, end) * COST_PER_DST_UNIT;
    }
}
