
package dataretrieving;

import java.util.ArrayList;
import java.util.HashMap;
import model.City;
import model.Commodity;
import model.Location;
import org.json.JSONArray;
import org.json.JSONObject;

public class DBDataRetriever {
    public Commodity[] getAllCommodities() {
        //JSONArray commodities = 
        //        new JSONArray(new DBCommodities().findAll_JSON(String.class));
        
        //------------------------Local test json---------------------------
        String jsontest = "[{\"id\":1,\"name\":\"Agricium\",\"type\":\"Metal\"},{\"id\":2,\"name\":\"Agricultural Supplies\",\"type\":\"Agricultural\"},{\"id\":3,\"name\":\"Aluminium\",\"type\":\"Metal\"},{\"id\":4,\"name\":\"Ammonia\",\"type\":\"Gas\"},{\"id\":5,\"name\":\"Argon\",\"type\":\"Gas\"},{\"id\":6,\"name\":\"Beryl\",\"type\":\"Mineral\"},{\"id\":7,\"name\":\"Carbon\",\"type\":\"Mineral\"},{\"id\":8,\"name\":\"Cobalt\",\"type\":\"Metal\"},{\"id\":9,\"name\":\"Copper\",\"type\":\"Metal\"},{\"id\":10,\"name\":\"Diamond\",\"type\":\"Mineral\"},{\"id\":11,\"name\":\"Dymantium\",\"type\":\"Alloy\"},{\"id\":12,\"name\":\"Fluorine\",\"type\":\"Gas\"},{\"id\":13,\"name\":\"Fuel\",\"type\":\"Processed\"},{\"id\":14,\"name\":\"Gold\",\"type\":\"Metal\"},{\"id\":15,\"name\":\"Hydrogen\",\"type\":\"Gas\"},{\"id\":16,\"name\":\"Iodine\",\"type\":\"Gas\"},{\"id\":17,\"name\":\"Laranite\",\"type\":\"Mineral\"},{\"id\":18,\"name\":\"Medical Supplies\",\"type\":\"Medical\"},{\"id\":19,\"name\":\"Nitrogen\",\"type\":\"Gas\"},{\"id\":20,\"name\":\"Processed Food\",\"type\":\"Food\"},{\"id\":21,\"name\":\"Scrap\",\"type\":\"Scrap\"},{\"id\":22,\"name\":\"Stims\",\"type\":\"Drug\"},{\"id\":23,\"name\":\"Titanium\",\"type\":\"Metal\"},{\"id\":24,\"name\":\"Tungsten\",\"type\":\"Metal\"},{\"id\":25,\"name\":\"Waste\",\"type\":\"Waste\"},{\"id\":26,\"name\":\"WidoW\",\"type\":\"Drug\"}]";
        JSONArray commodities = 
                new JSONArray(jsontest);
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
        //JSONArray cities = 
        //        new JSONArray(new DBLocations().findAll_JSON(String.class));
        
        //----------------------Local test json ----------------------------
        String jsontest = "[{\"id\":1,\"maxSize\":\"Large\",\"name\":\"Area 18\",\"x\":7648.0,\"y\":486465.0,\"z\":3.80004E7},{\"id\":2,\"maxSize\":\"Medium\",\"name\":\"Benson Mining Outpost\",\"x\":-8.43,\"y\":2000220.0,\"z\":95.33},{\"id\":3,\"maxSize\":\"Small\",\"name\":\"CR-L2\",\"x\":-4443.0,\"y\":1.2E7,\"z\":93.5},{\"id\":4,\"maxSize\":\"Large\",\"name\":\"Grim Hex\",\"x\":-30.5,\"y\":0.43,\"z\":-560542.0},{\"id\":5,\"maxSize\":\"Medium\",\"name\":\"HUR-L1\",\"x\":54746.4,\"y\":-4356.0,\"z\":549.55},{\"id\":6,\"maxSize\":\"Large\",\"name\":\"Jumptown\",\"x\":-34.1,\"y\":5.54,\"z\":-653222.0},{\"id\":7,\"maxSize\":\"Large\",\"name\":\"Levski\",\"x\":2005000.0,\"y\":34567.0,\"z\":-234.65},{\"id\":8,\"maxSize\":\"Large\",\"name\":\"Lorville\",\"x\":103.4,\"y\":-345.0,\"z\":2.9E7},{\"id\":9,\"maxSize\":\"Large\",\"name\":\"Port Olisar\",\"x\":0.0,\"y\":0.0,\"z\":0.0},{\"id\":10,\"maxSize\":\"Small\",\"name\":\"Tamdon Plains Aid Shelter\",\"x\":4384.0,\"y\":-103298.0,\"z\":48.32}]";
        JSONArray cities = 
                new JSONArray(jsontest);
        
        
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
        
        return cityArray;
    }
    
    public HashMap<Commodity, Double> getSales(City city, Commodity[] commodities) {
        //JSONArray sales = 
        //        new JSONArray(new DBSales().findAll_JSON(String.class));
        
        //----------------------Local test json ----------------------------
        String jsontest = "[{\"commodity\":\"Agricium\",\"id\":1,\"location\":\"Port Olisar\",\"price\":25.6},{\"commodity\":\"Aluminium\",\"id\":2,\"location\":\"Port Olisar\",\"price\":1.26},{\"commodity\":\"Agricium\",\"id\":3,\"location\":\"Port Olisar\",\"price\":25.6},{\"commodity\":\"Aluminium\",\"id\":4,\"location\":\"Port Olisar\",\"price\":1.26},{\"commodity\":\"Beryl\",\"id\":5,\"location\":\"Port Olisar\",\"price\":4.26},{\"commodity\":\"Tungsten\",\"id\":6,\"location\":\"Port Olisar\",\"price\":3.91},{\"commodity\":\"Titanium\",\"id\":7,\"location\":\"Port Olisar\",\"price\":8.27},{\"commodity\":\"Agricium\",\"id\":8,\"location\":\"Lorville\",\"price\":25.86},{\"commodity\":\"Agricultural Supplies\",\"id\":9,\"location\":\"Lorville\",\"price\":0.95},{\"commodity\":\"Laranite\",\"id\":10,\"location\":\"Lorville\",\"price\":28.35},{\"commodity\":\"Medical Supplies\",\"id\":11,\"location\":\"Lorville\",\"price\":18.1},{\"commodity\":\"WidoW\",\"id\":12,\"location\":\"Lorville\",\"price\":25.09},{\"commodity\":\"WidoW\",\"id\":13,\"location\":\"Area 18\",\"price\":24.0},{\"commodity\":\"Medical Supplies\",\"id\":14,\"location\":\"Area 18\",\"price\":18.03},{\"commodity\":\"Processed Food\",\"id\":15,\"location\":\"Area 18\",\"price\":1.36},{\"commodity\":\"Stims\",\"id\":16,\"location\":\"Area 18\",\"price\":3.4},{\"commodity\":\"Dymantium\",\"id\":17,\"location\":\"Area 18\",\"price\":1.56},{\"commodity\":\"WidoW\",\"id\":18,\"location\":\"Grim Hex\",\"price\":24.0},{\"commodity\":\"Titanium\",\"id\":19,\"location\":\"Grim Hex\",\"price\":8.27},{\"commodity\":\"Tungsten\",\"id\":20,\"location\":\"Grim Hex\",\"price\":3.73},{\"commodity\":\"Copper\",\"id\":21,\"location\":\"Grim Hex\",\"price\":28.0},{\"commodity\":\"Agricium\",\"id\":22,\"location\":\"Grim Hex\",\"price\":25.6},{\"commodity\":\"Beryl\",\"id\":23,\"location\":\"Grim Hex\",\"price\":4.27},{\"commodity\":\"Diamond\",\"id\":24,\"location\":\"Grim Hex\",\"price\":6.9},{\"commodity\":\"Gold\",\"id\":25,\"location\":\"Grim Hex\",\"price\":6.07},{\"commodity\":\"Medical Supplies\",\"id\":26,\"location\":\"Jumptown\",\"price\":18.02},{\"commodity\":\"Agricultural Supplies\",\"id\":27,\"location\":\"Jumptown\",\"price\":1.21},{\"commodity\":\"Cobalt\",\"id\":28,\"location\":\"Jumptown\",\"price\":2.54},{\"commodity\":\"Processed Food\",\"id\":29,\"location\":\"Jumptown\",\"price\":1.36},{\"commodity\":\"Agricium\",\"id\":30,\"location\":\"Levski\",\"price\":25.6},{\"commodity\":\"Ammonia\",\"id\":31,\"location\":\"Levski\",\"price\":8.26},{\"commodity\":\"Carbon\",\"id\":32,\"location\":\"Levski\",\"price\":2.8},{\"commodity\":\"Gold\",\"id\":33,\"location\":\"Levski\",\"price\":6.07},{\"commodity\":\"Laranite\",\"id\":34,\"location\":\"Levski\",\"price\":29.0},{\"commodity\":\"Processed Food\",\"id\":35,\"location\":\"Levski\",\"price\":1.38},{\"commodity\":\"WidoW\",\"id\":36,\"location\":\"Levski\",\"price\":24.03},{\"commodity\":\"Diamond\",\"id\":37,\"location\":\"Levski\",\"price\":6.7},{\"commodity\":\"Fluorine\",\"id\":38,\"location\":\"Levski\",\"price\":4.11},{\"commodity\":\"Agricultural Supplies\",\"id\":39,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":11.25},{\"commodity\":\"Ammonia\",\"id\":40,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":6.03},{\"commodity\":\"WidoW\",\"id\":41,\"location\":\"HUR-L1\",\"price\":25.1},{\"commodity\":\"Medical Supplies\",\"id\":42,\"location\":\"HUR-L1\",\"price\":18.1},{\"commodity\":\"Ammonia\",\"id\":43,\"location\":\"HUR-L1\",\"price\":9.77},{\"commodity\":\"Stims\",\"id\":44,\"location\":\"HUR-L1\",\"price\":4.8},{\"commodity\":\"Processed Food\",\"id\":45,\"location\":\"HUR-L1\",\"price\":1.14},{\"commodity\":\"Fuel\",\"id\":46,\"location\":\"HUR-L1\",\"price\":21.58},{\"commodity\":\"Gold\",\"id\":47,\"location\":\"CR-L2\",\"price\":5.2},{\"commodity\":\"Medical Supplies\",\"id\":48,\"location\":\"CR-L2\",\"price\":18.2},{\"commodity\":\"Nitrogen\",\"id\":49,\"location\":\"CR-L2\",\"price\":3.45},{\"commodity\":\"Dymantium\",\"id\":50,\"location\":\"CR-L2\",\"price\":20.1},{\"commodity\":\"Aluminium\",\"id\":51,\"location\":\"CR-L2\",\"price\":1.25},{\"commodity\":\"Argon\",\"id\":52,\"location\":\"CR-L2\",\"price\":7.9},{\"commodity\":\"Fluorine\",\"id\":57,\"location\":\"Benson Mining Outpost\",\"price\":2.4},{\"commodity\":\"Medical Supplies\",\"id\":58,\"location\":\"Benson Mining Outpost\",\"price\":19.0},{\"commodity\":\"Fuel\",\"id\":59,\"location\":\"Benson Mining Outpost\",\"price\":10.0},{\"commodity\":\"Processed Food\",\"id\":60,\"location\":\"Benson Mining Outpost\",\"price\":1.25}]";
        JSONArray sales = 
                new JSONArray(jsontest);
        
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
        //JSONArray purchases = 
        //        new JSONArray(new DBPurchasing().findAll_JSON(String.class));
        
        String jsontest = "[{\"commodity\":\"Medical Supplies\",\"id\":1,\"location\":\"Port Olisar\",\"price\":17.02},{\"commodity\":\"Waste\",\"id\":2,\"location\":\"Port Olisar\",\"price\":0.1},{\"commodity\":\"Waste\",\"id\":3,\"location\":\"Lorville\",\"price\":0.01},{\"commodity\":\"Aluminium\",\"id\":4,\"location\":\"Lorville\",\"price\":2.0},{\"commodity\":\"Agricium\",\"id\":5,\"location\":\"Area 18\",\"price\":24.41},{\"commodity\":\"Beryl\",\"id\":6,\"location\":\"Area 18\",\"price\":4.21},{\"commodity\":\"Tungsten\",\"id\":7,\"location\":\"Area 18\",\"price\":3.73},{\"commodity\":\"Hydrogen\",\"id\":8,\"location\":\"Area 18\",\"price\":2.41},{\"commodity\":\"Waste\",\"id\":9,\"location\":\"Area 18\",\"price\":0.01},{\"commodity\":\"Argon\",\"id\":10,\"location\":\"Grim Hex\",\"price\":18.23},{\"commodity\":\"Ammonia\",\"id\":11,\"location\":\"Grim Hex\",\"price\":7.87},{\"commodity\":\"Nitrogen\",\"id\":12,\"location\":\"Grim Hex\",\"price\":22.0},{\"commodity\":\"Waste\",\"id\":13,\"location\":\"Jumptown\",\"price\":0.01},{\"commodity\":\"WidoW\",\"id\":14,\"location\":\"Jumptown\",\"price\":14.55},{\"commodity\":\"Stims\",\"id\":15,\"location\":\"Jumptown\",\"price\":9.8},{\"commodity\":\"Agricultural Supplies\",\"id\":21,\"location\":\"Levski\",\"price\":1.12},{\"commodity\":\"Aluminium\",\"id\":22,\"location\":\"Levski\",\"price\":1.21},{\"commodity\":\"Hydrogen\",\"id\":23,\"location\":\"Levski\",\"price\":0.98},{\"commodity\":\"Iodine\",\"id\":24,\"location\":\"Levski\",\"price\":0.39},{\"commodity\":\"Fuel\",\"id\":25,\"location\":\"Levski\",\"price\":1.36},{\"commodity\":\"Medical Supplies\",\"id\":26,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":5.48},{\"commodity\":\"Stims\",\"id\":27,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":4.21},{\"commodity\":\"Processed Food\",\"id\":30,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":0.41},{\"commodity\":\"Hydrogen\",\"id\":31,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":0.7},{\"commodity\":\"Ammonia\",\"id\":32,\"location\":\"HUR-L1\",\"price\":4.28},{\"commodity\":\"Cobalt\",\"id\":33,\"location\":\"HUR-L1\",\"price\":3.25},{\"commodity\":\"Beryl\",\"id\":34,\"location\":\"HUR-L1\",\"price\":4.0},{\"commodity\":\"Laranite\",\"id\":35,\"location\":\"HUR-L1\",\"price\":19.1},{\"commodity\":\"Copper\",\"id\":36,\"location\":\"CR-L2\",\"price\":9.2},{\"commodity\":\"Fuel\",\"id\":37,\"location\":\"CR-L2\",\"price\":15.99},{\"commodity\":\"Argon\",\"id\":38,\"location\":\"CR-L2\",\"price\":1.24},{\"commodity\":\"Diamond\",\"id\":39,\"location\":\"Benson Mining Outpost\",\"price\":3.98},{\"commodity\":\"Carbon\",\"id\":40,\"location\":\"Benson Mining Outpost\",\"price\":0.87},{\"commodity\":\"Dymantium\",\"id\":41,\"location\":\"Benson Mining Outpost\",\"price\":4.7},{\"commodity\":\"Cobalt\",\"id\":42,\"location\":\"Benson Mining Outpost\",\"price\":3.54},{\"commodity\":\"Copper\",\"id\":43,\"location\":\"Benson Mining Outpost\",\"price\":2.29}]";
        JSONArray purchases = 
                new JSONArray(jsontest);
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
        //JSONArray purchases = 
        //        new JSONArray(new DBPurchasing().findAll_JSON(String.class));
        
        String jsontest = "[{\"commodity\":\"Medical Supplies\",\"id\":1,\"location\":\"Port Olisar\",\"price\":17.02},{\"commodity\":\"Waste\",\"id\":2,\"location\":\"Port Olisar\",\"price\":0.1},{\"commodity\":\"Waste\",\"id\":3,\"location\":\"Lorville\",\"price\":0.01},{\"commodity\":\"Aluminium\",\"id\":4,\"location\":\"Lorville\",\"price\":2.0},{\"commodity\":\"Agricium\",\"id\":5,\"location\":\"Area 18\",\"price\":24.41},{\"commodity\":\"Beryl\",\"id\":6,\"location\":\"Area 18\",\"price\":4.21},{\"commodity\":\"Tungsten\",\"id\":7,\"location\":\"Area 18\",\"price\":3.73},{\"commodity\":\"Hydrogen\",\"id\":8,\"location\":\"Area 18\",\"price\":2.41},{\"commodity\":\"Waste\",\"id\":9,\"location\":\"Area 18\",\"price\":0.01},{\"commodity\":\"Argon\",\"id\":10,\"location\":\"Grim Hex\",\"price\":18.23},{\"commodity\":\"Ammonia\",\"id\":11,\"location\":\"Grim Hex\",\"price\":7.87},{\"commodity\":\"Nitrogen\",\"id\":12,\"location\":\"Grim Hex\",\"price\":22.0},{\"commodity\":\"Waste\",\"id\":13,\"location\":\"Jumptown\",\"price\":0.01},{\"commodity\":\"WidoW\",\"id\":14,\"location\":\"Jumptown\",\"price\":14.55},{\"commodity\":\"Stims\",\"id\":15,\"location\":\"Jumptown\",\"price\":9.8},{\"commodity\":\"Agricultural Supplies\",\"id\":21,\"location\":\"Levski\",\"price\":1.12},{\"commodity\":\"Aluminium\",\"id\":22,\"location\":\"Levski\",\"price\":1.21},{\"commodity\":\"Hydrogen\",\"id\":23,\"location\":\"Levski\",\"price\":0.98},{\"commodity\":\"Iodine\",\"id\":24,\"location\":\"Levski\",\"price\":0.39},{\"commodity\":\"Fuel\",\"id\":25,\"location\":\"Levski\",\"price\":1.36},{\"commodity\":\"Medical Supplies\",\"id\":26,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":5.48},{\"commodity\":\"Stims\",\"id\":27,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":4.21},{\"commodity\":\"Processed Food\",\"id\":30,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":0.41},{\"commodity\":\"Hydrogen\",\"id\":31,\"location\":\"Tamdon Plains Aid Shelter\",\"price\":0.7},{\"commodity\":\"Ammonia\",\"id\":32,\"location\":\"HUR-L1\",\"price\":4.28},{\"commodity\":\"Cobalt\",\"id\":33,\"location\":\"HUR-L1\",\"price\":3.25},{\"commodity\":\"Beryl\",\"id\":34,\"location\":\"HUR-L1\",\"price\":4.0},{\"commodity\":\"Laranite\",\"id\":35,\"location\":\"HUR-L1\",\"price\":19.1},{\"commodity\":\"Copper\",\"id\":36,\"location\":\"CR-L2\",\"price\":9.2},{\"commodity\":\"Fuel\",\"id\":37,\"location\":\"CR-L2\",\"price\":15.99},{\"commodity\":\"Argon\",\"id\":38,\"location\":\"CR-L2\",\"price\":1.24},{\"commodity\":\"Diamond\",\"id\":39,\"location\":\"Benson Mining Outpost\",\"price\":3.98},{\"commodity\":\"Carbon\",\"id\":40,\"location\":\"Benson Mining Outpost\",\"price\":0.87},{\"commodity\":\"Dymantium\",\"id\":41,\"location\":\"Benson Mining Outpost\",\"price\":4.7},{\"commodity\":\"Cobalt\",\"id\":42,\"location\":\"Benson Mining Outpost\",\"price\":3.54},{\"commodity\":\"Copper\",\"id\":43,\"location\":\"Benson Mining Outpost\",\"price\":2.29}]";
        JSONArray purchases = 
                new JSONArray(jsontest);
        
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
