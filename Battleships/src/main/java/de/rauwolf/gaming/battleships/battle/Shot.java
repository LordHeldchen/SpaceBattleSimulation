package de.rauwolf.gaming.battleships.battle;

import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class Shot {
    public String                                    weaponName;

    public int                                       armorPenetration;
    public int                                       accuracy;
    public Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects;
    public DamageType                                damageType;

    public double                                    amount;
    public HullDamageLevel                           hullDamageLevel;
    public int                                       hitStrength;
    public int                                       specificResistanceOfTarget;

    public Shot() {
    }

    public Shot(int amount, int armorPenetration, int accuracy, DamageType damageType, Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects, String weaponName) {
        this.amount = amount;
        this.armorPenetration = armorPenetration;
        this.weaponName = weaponName;
        this.hitStrength = 0;
        this.accuracy = accuracy;
        this.damageType = damageType;
        this.secondaryEffects = secondaryEffects;
        this.hullDamageLevel = HullDamageLevel.NOT_DETERMINED;
    }
}
