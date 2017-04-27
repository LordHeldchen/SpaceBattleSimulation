package ships.blueprints;

import java.util.HashMap;
import java.util.Map;

import battle.WeaponSecondaryEffect;
import ships.shipHulls.ValueDurationPair;
import ships.shipHulls.WeaponSize;

public class WeaponBlueprint {
    private final String                                        name;
    private final WeaponSize                                    size;

    private final int                                           accuracy;
    private final int                                           damage;
    private final int                                           armorPenetration;
    private final int                                           initiativeBonus;
    private final int                                           value;

    // TODO: Introduce reload time e.g. for BurstFire of several rounds, and/or a burst fire mechanic?
    private final int                                           timeCost;
    private final Map<WeaponSecondaryEffect, ValueDurationPair> weaponEffects;

    protected Map<Class<? extends Blueprint>, Integer>          preferredTargets;

    public WeaponBlueprint(String name, WeaponSize size, int accuracy, int damage, int armorPenetration, int timeCost, int initiativeBonus, int value,
            Map<WeaponSecondaryEffect, ValueDurationPair> weaponEffects) {
        this.name = name;
        this.size = size;
        this.damage = damage;
        this.accuracy = accuracy;
        this.armorPenetration = armorPenetration;
        this.timeCost = timeCost;
        this.initiativeBonus = initiativeBonus;
        this.value = value;
        this.weaponEffects = weaponEffects;

        this.preferredTargets = new HashMap<Class<? extends Blueprint>, Integer>();
    }

    public String toString() {
        return getName() + " (" + getSize() + ") -->  DMG " + damage + ", AP " + armorPenetration + ", TC " + getTimeCost();
    }

    public final String getName() {
        return this.name;
    }

    public final WeaponSize getSize() {
        return this.size;
    }

    public Map<Class<? extends Blueprint>, Integer> getPreferredTargets() {
        return this.preferredTargets;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public Map<WeaponSecondaryEffect, ValueDurationPair> getSecondaryEffects() {
        return weaponEffects;
    }

    // TODO Differentiation per damage type, also for modifiers, might be
    // desirable.
    public int getDamage() {
        return this.damage;
    }

    public int getArmorPenetration() {
        return this.armorPenetration;
    }

    public int getBattleSpeedDecay() {
        return this.getTimeCost();
    }

    public int getTimeCost() {
        return timeCost;
    }

    public int getInitiativeBonus() {
        return initiativeBonus;
    }

    public int getValue() {
        return value;
    }
}
