package pathfindingws;

public class TransactionWSFormat {
    private String origin;
    private String destination;
    private String commodity;
    private double profit;

    public TransactionWSFormat(String origin, String destination, String commodity, double profit) {
        this.origin = origin;
        this.destination = destination;
        this.commodity = commodity;
        this.profit = profit;
    }

    public String getOrigin() {
        return origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getCommodity() {
        return commodity;
    }

    public void setCommodity(String commodity) {
        this.commodity = commodity;
    }

    public double getProfit() {
        return profit;
    }

    public void setProfit(double profit) {
        this.profit = profit;
    }
}
