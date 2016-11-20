package ships.layouts;

public class ArmorLayout {

    private final String name;

    private final int    maxAmount;
    private final int    strength;

    public ArmorLayout(String name, int maxAmount, int strength) {
        this.name = name;
        this.maxAmount = maxAmount;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getStrength() {
        return strength;
    }

}
