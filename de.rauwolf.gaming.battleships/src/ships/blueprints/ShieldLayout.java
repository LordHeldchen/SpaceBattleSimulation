package ships.blueprints;

public class ShieldLayout {
    private final String name;

    private final int    armorHardening;
    private final int    maxAmount;
    private final int    regeneration;
    private final int    strength;

    public ShieldLayout(String name, int armorHardening, int maxAmount, int regeneration, int strength) {
        this.name = name;
        this.armorHardening = armorHardening;
        this.maxAmount = maxAmount;
        this.regeneration = regeneration;
        this.strength = strength;
    }

    public String getName() {
        return name;
    }

    public int getMaxAmount() {
        return maxAmount;
    }

    public int getRegeneration() {
        return regeneration;
    }

    public int getStrength() {
        return strength;
    }

    public int getArmorHardening() {
        return armorHardening;
    }
}
