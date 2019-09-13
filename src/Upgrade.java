public class Upgrade {

    private int qt;
    private int baseCost;
    private int cost;
    private int strength;

    public Upgrade(int baseCost, int strength) {

        this.baseCost = baseCost;
        this.strength = strength;
        this.cost = baseCost;

    }

    public int getQt() {
        return qt;
    }

    public void setQt(int qt) {
        this.qt = qt;
    }

    public int getCost() {

        calculateCost();
        return cost;

    }

    public void calculateCost () {

        cost = (qt+1) * baseCost;

    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
