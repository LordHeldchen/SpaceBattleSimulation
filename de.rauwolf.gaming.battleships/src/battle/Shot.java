package battle;

public class Shot {
    public final int amount;
    public final int armorPenetration;
    public final int accuracy;

    public Shot(int amount, int armorPenetration, int accuracy) {
        this.amount = amount;
        this.armorPenetration = armorPenetration;
        this.accuracy = accuracy;
    }
}
