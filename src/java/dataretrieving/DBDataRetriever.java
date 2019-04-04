/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dataretrieving;

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
        System.out.println();
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
}
