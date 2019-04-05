package acoalgorithm;

import java.util.HashMap;
import model.*;

public class Ant {
    protected int trailSize;
    protected int[][] trail;
    protected boolean[][][] visited;
    protected City currentCity;
    protected City[] cities;
    private HashMap<Commodity, City[]> purchasePoints;

    public Ant(int jumpsNumber, City[] cities, 
            HashMap<Commodity,City[]> purchasePoints) {
        this.trailSize = jumpsNumber;
        this.trail = new int[trailSize][3];
        this.purchasePoints = purchasePoints;
        this.cities = cities;
        this.visited = initializeVisited();
    }

    protected void tradeWith(int currentIndex, int commodity, int targetCity) {
        trail[currentIndex + 1][0] = commodity;     //city sales index
        trail[currentIndex + 1][1] = targetCity;    //purchasePoints index
        
        if(commodity == -1) {
            currentCity = cities[targetCity];
            trail[currentIndex + 1][2] = targetCity;
        } else {
            System.out.println("*-------------------------------*");
            System.out.println(currentCity.getName());
            System.out.println(currentCity.getSellingCommodity(commodity));
            System.out.println(currentCity.getSellingCommodity(commodity).getName());
            System.out.println(purchasePoints.get(currentCity
                        .getSellingCommodity(commodity)).length);
            System.out.println(purchasePoints.keySet().contains(currentCity
                        .getSellingCommodity(commodity)));
            
            currentCity = purchasePoints.get(currentCity
                        .getSellingCommodity(commodity))[targetCity];
            trail[currentIndex + 1][2] = getIndexOf(cities, currentCity);   //storing the city "cities" index
            visited[trail[currentIndex][2]][commodity][targetCity] = true;
        }
    }

    protected boolean visited(int i, int j, int k) {
        return visited[i][j][k];
    }

    protected double trailProfit(double graph[][][]) {
        double length = 0;
        for (int i = 0; i < trailSize - 1; i++) 
            length += graph[trail[i][2]][trail[i+1][0]][trail[i + 1][1]];   //profit for the specified city-commodity-destiny
        return length;
    }

    protected void clear() {
        for (int i = 0; i < trailSize; i++) {
            for(int j = 0; j < trail[i].length; j++) {
                trail[i][j] = 0;
            }
        }
        
        for(int i = 0; i < visited.length; i++) {
            for (int j = 0; j < visited[i].length; j++) {
                for (int k = 0; k < visited[i][j].length; k++) {
                    visited[i][j][k] = false;
                }
            }
        }
    }
    
    private int getIndexOf(Object[] array, Object element) {
        for(int i = 0; i < array.length; i++) {
            if(array[i] == element) {
                return i;
            }
        }
        return -1;
    }
    
    protected boolean[][][] initializeVisited() {
        boolean[][][] visited = new boolean[cities.length][][];
        for (int i = 0; i < cities.length; i++) {
            visited[i] = new boolean[cities[i].getSales().keySet().size()][];
            for (int j = 0; j < visited[i].length; j++) {
                visited[i][j]
                        = new boolean[purchasePoints.get(cities[i].getSales().keySet()
                                .stream().toArray(Commodity[]::new)[j]).length];
            }
        }
        return visited;
    }
    
    protected City getCurrentCity() {
        return currentCity;
    }
}