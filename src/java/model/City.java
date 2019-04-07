package model;

import java.util.HashMap;

public class City {
    private final String name;
    private final Location location;
    private String maxSize;
    private HashMap<Commodity, Double> salesPrices;
    private HashMap<Commodity, Double> purchasesPrices;
    private Commodity[] sales;
    private Commodity[] purchases;

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

    public Commodity[] getSales() {
        return sales;
    }

    public Commodity[] getPurchases() {
        return purchases;
    }
    
    public HashMap<Commodity, Double> getSalesPrices() {
        return salesPrices;
    }

    public HashMap<Commodity, Double> getPurchasesPrices() {
        return purchasesPrices;
    }
    
    public double getBuyingPrice(Commodity commodity) {
        return purchasesPrices.get(commodity);
    }
    
    public double getSellingPrice(Commodity commodity) {
        return salesPrices.get(commodity);
    }
    
    public Commodity getSellingCommodity(int index) {
        return salesPrices.keySet().stream().toArray(Commodity[] ::new)[index];
    }
    
    public void setSalesPrices(HashMap<Commodity, Double> sales) {
        this.salesPrices = sales;
        this.sales = sales.keySet().toArray(new Commodity[sales.size()]);
    }
    
    public void setPurchasesPrices(HashMap<Commodity, Double> purchases) {
        this.purchasesPrices = purchases;
        this.purchases = purchases.keySet().toArray(new Commodity[purchases.size()]);
    }
}
