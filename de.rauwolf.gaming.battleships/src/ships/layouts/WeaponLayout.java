package ships.layouts;

public class WeaponLayout {
    private final String name;

    private final int    accuracy;
    private final int    damage;
    private final int    strength;

    private final int    maxPayload;

    public WeaponLayout(String name, int accuracy, int damage, int strength, int maxPayload) {
        this.name = name;
        this.accuracy = 100;
        this.damage = 50;
        this.strength = 3;
        this.maxPayload = maxPayload;
    }

    public String getName() {
        return name;
    }

    public int getAccuracy() {
        return accuracy;
    }

    public int getDamage() {
        return damage;
    }

    public int getStrength() {
        return strength;
    }

    public int getMaxPayload() {
        return maxPayload;
    }
}
