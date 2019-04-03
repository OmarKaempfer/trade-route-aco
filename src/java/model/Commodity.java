package model;

public class Commodity {
    private String name;
    private double volume;

    public Commodity(String name, double volume) {
        this.name = name;
        this.volume = volume;
    }

    public String getName() {
        return name;
    }

    public double getVolume() {
        return volume;
    }
    
}
