/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataretrieving;

import java.util.ArrayList;
import java.util.HashMap;
import model.City;
import model.Commodity;
import model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author Vaehnor
 */
public class DBDataRetriever {
    public Commodity[] getAllCommodities() {
        JSONArray commodities = 
                new JSONArray(new DBCommodities().findAll_JSON(String.class));
        Commodity[] commArray = new Commodity[commodities.length()];
        for (int i = 0; i < commodities.length(); i++) {
            JSONObject jsonobject = commodities.getJSONObject(i);
            String name = jsonobject.getString("name");
            String type = jsonobject.getString("type");
            commArray[i] = new Commodity(name, type);
        }
        return commArray;
    }
    
    public City[] getAllCities() {
        JSONArray cities = 
                new JSONArray(new DBLocations().findAll_JSON(String.class));
        City[] cityArray = new City[cities.length()];
        for (int i = 0; i < cities.length(); i++) {
            JSONObject jsonobject = cities.getJSONObject(i);
            String name = jsonobject.getString("name");
            
            Double x = jsonobject.getDouble("x");
            Double y = jsonobject.getDouble("y");
            Double z = jsonobject.getDouble("z");
            Location loc = new Location(x, y, z);
            
            String maxSize = jsonobject.getString("maxSize");
            cityArray[i] = new City(name, loc, maxSize);
        }
        
        for(City city : cityArray){
            System.out.println(city.getName());
        }
        return cityArray;
    }
    
    public HashMap<Commodity, Double> getSales(City city, Commodity[] commodities) {
        JSONArray sales = 
                new JSONArray(new DBSales().findAll_JSON(String.class));

        HashMap<Commodity, Double> salesMap = new HashMap<Commodity, Double>();
        for (int i = 0; i < sales.length(); i++) {
            JSONObject jsonobject = sales.getJSONObject(i);
            if(jsonobject.getString("location").equals(city.getName())) {
                
                for(Commodity comm : commodities) {
                    if(comm.getName().equals(jsonobject.getString("commodity"))) {
                        salesMap.put(comm, jsonobject.getDouble("price"));
                    }
                }
            }
        }

        return salesMap;
    }
    
    public HashMap<Commodity, Double> getPurchases(City city, Commodity[] commodities) {
        JSONArray purchases = 
                new JSONArray(new DBPurchasing().findAll_JSON(String.class));
        
        HashMap<Commodity, Double> purchasesMap = new HashMap<>();
        for (int i = 0; i < purchases.length(); i++) {
            JSONObject jsonobject = purchases.getJSONObject(i);
            if(jsonobject.getString("location").equals(city.getName())) {
                
                for(Commodity comm : commodities) {
                    if(comm.getName().equals(jsonobject.getString("commodity"))) {
                        purchasesMap.put(comm, jsonobject.getDouble("price"));
                    }
                }
            }
        }

        return purchasesMap;
    }
    
    public HashMap<Commodity, City[]> getPurchasePoints(Commodity[] commodities, City[] cities) {
        HashMap<Commodity, City[]> purchasePoints = new HashMap<>();
        JSONArray purchases = 
                new JSONArray(new DBPurchasing().findAll_JSON(String.class));
        for(Commodity comm : commodities) {
            ArrayList<City> commCities = new ArrayList<>();
            
            for(int i = 0; i < purchases.length(); i++) {
                JSONObject jsonobject = purchases.getJSONObject(i);
                if(comm.getName().equals(jsonobject.getString("commodity"))) {
                    for(int j = 0; j < cities.length; j++) {
                        if(cities[j].getName().equals(jsonobject.getString("location"))) {
                            commCities.add(cities[j]);
                        }
                    }
                }
            }
            purchasePoints.put(comm, commCities.toArray(new City[commCities.size()]));
        }

        return purchasePoints;
    }
}
