package model;

public class Commodity {
    private final String name;
    private final String type;
    
    public Commodity(String name, String type) {
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }   
    
    public String getType() {
        return type;
    }
}
