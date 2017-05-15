package battle;

import java.util.List;
import java.util.Map;

import ships.shipHulls.DamageType;
import ships.weapons.WeaponSecondaryEffect;

public class Shot {
    public double                                          amount;
    public HullDamageLevel                                 hullDamageLevel;
    public int                                             hitStrength;
    public int                                             specificResistanceOfTarget;

    public final int                                       armorPenetration;
    public final int                                       accuracy;
    public final Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects;
    public final DamageType                                damageType;

    public Shot(int amount, int armorPenetration, int accuracy, DamageType damageType, Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects) {
        this.amount = amount;
        this.armorPenetration = armorPenetration;
        this.hitStrength = 0;
        this.accuracy = accuracy;
        this.damageType = damageType;
        this.secondaryEffects = secondaryEffects;
        this.hullDamageLevel = HullDamageLevel.DEFLECT;
    }
}
