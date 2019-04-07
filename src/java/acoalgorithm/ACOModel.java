/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acoalgorithm;

import java.util.HashMap;
import model.City;
import model.Commodity;

/**
 *
 * @author Vaehnor
 */
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
