package acoalgorithm;

import java.util.HashMap;
import model.City;
import model.Commodity;

public class ACOModel {
    private final City[] cities;
    private final Commodity[] commodities;
    private final HashMap<Commodity, City[]> purchasePoints;

    public ACOModel(City[] cities, Commodity[] commodities, HashMap<Commodity, 
            City[]> purchasePoints) {
        this.cities = cities;
        this.commodities = commodities;
        this.purchasePoints = purchasePoints;
    }

    public City[] getCities() {
        return cities;
    }

    public Commodity[] getCommodities() {
        return commodities;
    }

    public HashMap<Commodity, City[]> getPurchasePoints() {
        return purchasePoints;
    }
    
    
}
