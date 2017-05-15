package ships.weapons;

import java.util.List;
import java.util.Map;

import ships.hulls.HullSize;
import ships.resourceLoader.WeaponSize;
import ships.shipHulls.DamageType;

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
    private final DamageType                                    dmgType;
    private final Map<WeaponSecondaryEffect, List<Integer>>     weaponEffects;

    protected List<HullSize>                                    preferredTargetSizes;

    public WeaponBlueprint(String name, WeaponSize size, int accuracy, int damage, int armorPenetration, int timeCost, int initiativeBonus, int value,
            DamageType dmgType, List<HullSize> preferredTargetSizes, Map<WeaponSecondaryEffect, List<Integer>> weaponEffects) {
        this.name = name;
        this.size = size;
        this.damage = damage;
        this.accuracy = accuracy;
        this.armorPenetration = armorPenetration;
        this.timeCost = timeCost;
        this.initiativeBonus = initiativeBonus;
        this.value = value;
        this.dmgType = dmgType;
        this.preferredTargetSizes = preferredTargetSizes;
        this.weaponEffects = weaponEffects;
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

    public List<HullSize> getPreferredTargets() {
        return this.preferredTargetSizes;
    }

    public int getAccuracy() {
        return this.accuracy;
    }

    public Map<WeaponSecondaryEffect, List<Integer>> getSecondaryEffects() {
        return weaponEffects;
    }

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

    public DamageType getDmgType() {
        return dmgType;
    }
}
