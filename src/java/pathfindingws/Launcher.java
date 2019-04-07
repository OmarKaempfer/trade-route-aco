package pathfindingws;

import acoalgorithm.ACOModel;
import acoalgorithm.ACOParameters;
import acoalgorithm.AntColonyOptimization;
import com.google.gson.Gson;
import dataretrieving.DBDataRetriever;
import economy.ProfitCalculator;
import model.City;
import model.Commodity;
import model.Transaction;

public class Launcher {
    public static void main(String[] args) {
        launch(5, 100);
    }
    
    public static String launch(int jumps, int shipCapacity) {
        ACOModel acoModel = generateACOModel();
        ACOParameters acoParameters = new ACOParameters();
        
        Transaction[] transactions = new AntColonyOptimization(acoParameters, acoModel, 
                shipCapacity, jumps).solve();
        
        return transformToWSFormat(transactions, shipCapacity);
    }

    private static ACOModel generateACOModel() {
        DBDataRetriever dBDataRetriever = new DBDataRetriever();
        City[] cities = dBDataRetriever.getAllCities();
        Commodity[] commodities = dBDataRetriever.getAllCommodities();
        for(City city : cities) {
            city.setSalesPrices(dBDataRetriever.getSales(city, commodities));
            city.setPurchasesPrices(dBDataRetriever.getPurchases(city, commodities));
        }
        ACOModel acoModel = new ACOModel(cities, commodities, 
                dBDataRetriever.getPurchasePoints(commodities, cities));
        return acoModel;
    }
    
    private static String transformToWSFormat(Transaction[] transactions, 
                                                             int shipCapacity) {
        TransactionWSFormat[] trArray = new TransactionWSFormat[transactions.length];
        for (int i = 0; i < trArray.length; i++) {
            trArray[i] = new TransactionWSFormat(transactions[i].getStartPoint().getName(), 
                                                 transactions[i].getEndPoint().getName(), 
                                                 transactions[i].getCommodity().getName(),
                        ProfitCalculator.calculateProfit(transactions[i].getStartPoint(), 
                            transactions[i].getEndPoint(),
                            transactions[i].getCommodity(),
                            shipCapacity));
        }
        
        return new Gson().toJson(trArray);
    }
}
