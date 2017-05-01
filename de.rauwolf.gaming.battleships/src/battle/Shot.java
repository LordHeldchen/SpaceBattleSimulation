package battle;

import java.util.Map;

import ships.shipHulls.DamageType;
import ships.shipHulls.ValueDurationPair;

public class Shot {
    public final int                                           amount;
    public final int                                           armorPenetration;
    public final int                                           accuracy;
    public final Map<WeaponSecondaryEffect, ValueDurationPair> secondaryEffects;
    public final DamageType                                   damageType;

    public Shot(int amount, int armorPenetration, int accuracy, DamageType damageType, Map<WeaponSecondaryEffect, ValueDurationPair> secondaryEffects) {
        this.amount = amount;
        this.armorPenetration = armorPenetration;
        this.accuracy = accuracy;
        this.damageType = damageType;
        this.secondaryEffects = secondaryEffects;
    }
}
