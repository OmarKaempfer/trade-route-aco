/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package acoalgorithm;

import dataretrieving.DBCommodities;
import dataretrieving.DBDataRetriever;
import dataretrieving.DBLocations;
import dataretrieving.DBPurchasing;
import dataretrieving.DBSales;
import java.util.HashMap;
import model.City;
import model.Commodity;

/**
 *
 * @author Vaehnor
 */
public class Launcher {
        City[] cities;
        Commodity[] commodities;
        

    public static void main(String[] args) {
        System.setErr(System.out);
        launch();
    }
    
    public static void launch() {
        DBDataRetriever dBDataRetriever = new DBDataRetriever();
        City[] cities = dBDataRetriever.getAllCities();
        Commodity[] commodities = dBDataRetriever.getAllCommodities();
        
        for(City city : cities) {
            city.setSales(dBDataRetriever.getSales(city, commodities));
            city.setPurchases(dBDataRetriever.getPurchases(city, commodities));
        }
        AntColonyOptimization aco = new AntColonyOptimization(0, 1, 5, 0.5, 500,
                0.8, 0.01, 1000, cities, 
                dBDataRetriever.getPurchasePoints(commodities, cities), 3);
        aco.setCommodities(commodities);
        aco.solve();
    }
}
