package de.rauwolf.gaming.battleships.ships.hulls;

import java.util.HashMap;
import java.util.Map;

import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

public class HullType {
    private final Map<SizeClass, Integer>     availableWeaponSlots    = new HashMap<SizeClass, Integer>();
    private final Map<ComponentType, Integer> availableComponentSlots = new HashMap<ComponentType, Integer>();
    private final Map<SizeClass, Integer>     baysBySize              = new HashMap<SizeClass, Integer>();

    private final Map<StatType, Integer>      baseStatMap;

    private final String                      shorthand;
    private final String                      name;
    private final int                         value;
    private final String                      description;
    private final SizeClass                   sizeCategory;

    public HullType(String shorthand, String name, SizeClass sizeCategory, int baseGlanceThreshold, int baseHitThreshold, int baseCritThreshold,
            int baseHullStrength, int baseContainment, int baseEPM, int baseStartInitiative, int baseManeuverability, int value, String description) {
        this.shorthand = shorthand;
        this.name = name;
        this.sizeCategory = sizeCategory;
        this.baseStatMap = new HashMap<StatType, Integer>();

        baseStatMap.put(StatType.THRESHOLD_GLANCE, baseGlanceThreshold);
        baseStatMap.put(StatType.THRESHOLD_HIT, baseHitThreshold);
        baseStatMap.put(StatType.THRESHOLD_CRIT, baseCritThreshold);
        baseStatMap.put(StatType.MAX_HULL_STRENGTH, baseHullStrength);
        baseStatMap.put(StatType.CONTAINMENT, baseContainment);
        baseStatMap.put(StatType.EPM, baseEPM);
        baseStatMap.put(StatType.INITIATIVE, baseStartInitiative);
        baseStatMap.put(StatType.MANEUVERABILITY, baseManeuverability);

        this.value = value;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public String getShorthand() {
        return shorthand;
    }

    public int getBaseStatFor(StatType stat) {
        if (baseStatMap.containsKey(stat)) {
            return baseStatMap.get(stat);
        }
        return 0;
    }

    public void setAvailableShipBaysForSize(SizeClass size, int num) {
        baysBySize.put(size, num);
    }

    public void setAvailableWeaponSlotsForSize(SizeClass size, int num) {
        availableWeaponSlots.put(size, num);
    }

    public void setAvailableComponentSlotsForType(ComponentType type, int num) {
        availableComponentSlots.put(type, num);
    }

    public int getAvailableShipBaysForSize(SizeClass size) {
        return baysBySize.get(size) == null ? 0 : baysBySize.get(size);
    }

    public int getAvailableWeaponSlotsForSize(SizeClass size) {
        return availableWeaponSlots.get(size) == null ? 0 : availableWeaponSlots.get(size);
    }

    public int getAvailableComponentSlotsForType(ComponentType type) {
        return availableComponentSlots.get(type) == null ? 0 : availableComponentSlots.get(type);
    }

    public String getDescription() {
        return description;
    }

    public Map<SizeClass, Integer> getBaysBySize() {
        return baysBySize;
    }

    public SizeClass getHullSize() {
        return sizeCategory;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}
