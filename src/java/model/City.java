package model;

import java.util.HashMap;

public class City {
    private String name;
    private Location location;
    private String maxSize;
    private HashMap<Commodity, Double> sales;
    private HashMap<Commodity, Double> purchases;

    public City(String name, Location location, String maxSize) {
        this.name = name;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public HashMap<Commodity, Double> getSales() {
        return sales;
    }

    public HashMap<Commodity, Double> getPurchases() {
        return purchases;
    }
    
    public double getBuyingPrice(Commodity commodity) {
        return purchases.get(commodity);
    }
    
    public double getSellingPrice(Commodity commodity) {
        return purchases.get(commodity);
    }
    
    public Commodity getSellingCommodity(int index) {
        return sales.keySet().stream().toArray(Commodity[] ::new)[index];
    }
    
    public void setSales(HashMap<Commodity, Double> sales) {
        this.sales = sales;
    }
    
    public void setPurchases(HashMap<Commodity, Double> purchases) {
        this.purchases = purchases;
    }
}
