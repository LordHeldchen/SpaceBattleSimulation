package battle;

import java.util.Map;

import ships.shipHulls.ValueDurationPair;

public class Shot {
    public final int amount;
    public final int armorPenetration;
    public final int accuracy;
    public final Map<WeaponSecondaryEffect, ValueDurationPair> secondaryEffects;

    public Shot(int amount, int armorPenetration, int accuracy, Map<WeaponSecondaryEffect, ValueDurationPair> secondaryEffects) {
        this.amount = amount;
        this.armorPenetration = armorPenetration;
        this.accuracy = accuracy;
        this.secondaryEffects = secondaryEffects;
    }
}
