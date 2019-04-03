package model;

import java.util.HashMap;

public class City {
    private String name;
    private Location location;
    private HashMap<Commodity, Double> sales;
    private HashMap<Commodity, Double> purchases;

    public City(String name, Location location, HashMap<Commodity, Double> sales,
            HashMap<Commodity, Double> purchases) {
        this.name = name;
        this.location = location;
        this.sales = sales;
        this.purchases = purchases;
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
}
