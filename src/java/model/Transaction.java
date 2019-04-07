/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import model.City;
import model.Commodity;

public class Transaction {
    private final City startPoint;
    private final Commodity commodity;
    private final City endPoint;

    public Transaction(City startPoint, City endPoint, Commodity commodity) {
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.commodity = commodity;
    }

    public City getStartPoint() {
        return startPoint;
    }

    public Commodity getCommodity() {
        return commodity;
    }

    public City getEndPoint() {
        return endPoint;
    }

    @Override
    public int hashCode() {
        return this.startPoint.getName().hashCode()
                + this.commodity.getName().hashCode()
                + this.commodity.getType().hashCode()
                + this.endPoint.getName().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        
        if (obj == this) { 
            return true; 
        } 
  
        if (!(obj instanceof Transaction)) { 
            return false; 
        } 
        return this.hashCode() == obj.hashCode();
    }
    
    
}
