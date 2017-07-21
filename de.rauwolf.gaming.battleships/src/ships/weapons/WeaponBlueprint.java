package ships.weapons;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ships.blueprints.Blueprint;
import ships.blueprints.SizeClass;
import ships.shipHulls.DamageType;
import ships.stats.StatType;

public class WeaponBlueprint implements Blueprint {
    private final String                                    shorthand;
    private final String                                    name;
    private final SizeClass                                 size;

    private final int                                       value;

    private final Map<StatType, Integer>                    statMap;

    // TODO: Introduce reload time e.g. for BurstFire of several rounds, and/or
    // a burst fire mechanic?
    private final DamageType                                dmgType;
    private final Map<WeaponSecondaryEffect, List<Integer>> weaponEffects;

    protected List<SizeClass>                               preferredTargetSizes;

    public WeaponBlueprint(String shorthand, String name, SizeClass size, int accuracy, int damage, int armorPenetration, int timeCost, int initiativeBonus,
            int value, DamageType dmgType, List<SizeClass> preferredTargetSizes, Map<WeaponSecondaryEffect, List<Integer>> weaponEffects) {

        this.shorthand = shorthand;
        this.name = name;
        this.size = size;
        this.value = value;
        this.dmgType = dmgType;

        this.statMap = new HashMap<StatType, Integer>();
        statMap.put(StatType.DAMAGE, damage);
        statMap.put(StatType.ACCURACY, accuracy);
        statMap.put(StatType.AP, armorPenetration);
        statMap.put(StatType.TIMECOST, timeCost);
        statMap.put(StatType.INITIATIVE, initiativeBonus);

        this.preferredTargetSizes = preferredTargetSizes;
        this.weaponEffects = weaponEffects;
    }

    @Override
    public String toString() {
        return getName() + " (" + getSize() + ") -->  DMG " + statMap.get(StatType.DAMAGE) + ", AP " + statMap.get(StatType.AP) + ", TC "
                + statMap.get(StatType.TIMECOST);
    }

    @Override
    public Integer getStatFor(StatType stat) {
        if (!statMap.containsKey(stat)) {
            statMap.put(stat, 0);
        }
        return statMap.get(stat);
    }

    public final String getName() {
        return this.name;
    }

    public final String getShorthand() {
        return shorthand;
    }

    @Override
    public final SizeClass getSize() {
        return this.size;
    }

    public List<SizeClass> getPreferredTargets() {
        return this.preferredTargetSizes;
    }

    public Map<WeaponSecondaryEffect, List<Integer>> getSecondaryEffects() {
        return weaponEffects;
    }

    public int getValue() {
        return value;
    }

    public DamageType getDmgType() {
        return dmgType;
    }
}
