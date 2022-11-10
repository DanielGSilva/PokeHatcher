package iocari.pokehatcher;

public class Upgrade {
    private long lvlNeeded;
    private double value;
    private int order;

    public Upgrade(long lvlNeeded, double value, int order) {
        this.lvlNeeded = lvlNeeded;
        this.value = value;
        this.order = order;
    }

    public long getLvlNeeded() {
        return lvlNeeded;
    }

    public double getValue() {
        return value;
    }

    public int getOrder() {
        return order;
    }
}
