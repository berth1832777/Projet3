public class Enchant extends Upgrade {

    public Enchant() {

        super("Enchant",100,1);

    }

    public void calculateCost () {

        cost = (int)(baseCost*Math.pow(2,qt));

    }

}
