public class Upgrade {

    protected String name;
    protected int qt;
    protected int baseCost;
    protected int cost;
    protected int strength;

    public Upgrade( String name,int baseCost, int strength) {

        this.name = name;
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

        cost = (int)(baseCost*Math.pow(1.18,qt));

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }
}
