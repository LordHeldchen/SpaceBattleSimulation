package ships.hulls;

import java.util.HashMap;
import java.util.Map;

import ships.resourceLoader.WeaponSize;
import ships.shipHulls.ComponentType;

public class HullType {
    private final Map<WeaponSize, Integer> availableWeaponSlots = new HashMap<WeaponSize, Integer>();
    private final Map<ComponentType, Integer> availableComponentSlots = new HashMap<ComponentType, Integer>();
    private final Map<HullSize, Integer> baysBySize = new HashMap<HullSize, Integer>();

    private final int baseGlanceThreshold;
    private final int baseHitThreshold;
    private final int baseCritThreshold;
    private final int baseHullStrength;
    private final int baseContainment;
    private final int baseEPM;
    private final int baseStartInitiative;
    private final int baseEvasion;
    private final int value;
    private final String description;
    private final String name;
    private final HullSize sizeCategory;

    public HullType(String name, HullSize sizeCategory, int baseGlanceThreshold, int baseHitThreshold, int baseCritThreshold, int baseHullStrength,
            int baseContainment, int baseEPM, int baseStartInitiative, int baseEvasion, int value, String description) {
        this.name = name;
        this.sizeCategory = sizeCategory;
        this.baseGlanceThreshold = baseGlanceThreshold;
        this.baseHitThreshold = baseHitThreshold;
        this.baseCritThreshold = baseCritThreshold;
        this.baseHullStrength = baseHullStrength;
        this.baseContainment = baseContainment;
        this.baseEPM = baseEPM;
        this.baseStartInitiative = baseStartInitiative;
        this.baseEvasion = baseEvasion;
        this.value = value;
        this.description = description;
    }

    public String toString() {
        return name;
    }

    public int getBaseGlanceThreshold() {
        return baseGlanceThreshold;
    }

    public int getBaseHitThreshold() {
        return baseHitThreshold;
    }

    public int getBaseCritThreshold() {
        return baseCritThreshold;
    }

    public int getBaseHullStrength() {
        return baseHullStrength;
    }

    public int getBaseStartBattleSpeed() {
        return baseStartInitiative;
    }

    public void setAvailableShipBaysForSize(HullSize size, int num) {
        baysBySize.put(size, num);
    }

    public void setAvailableWeaponSlotsForSize(WeaponSize size, int num) {
        availableWeaponSlots.put(size, num);
    }

    public void setAvailableComponentSlotsForType(ComponentType type, int num) {
        availableComponentSlots.put(type, num);
    }

    public int getAvailableShipBaysForSize(HullSize size) {
        return baysBySize.get(size) == null ? 0 : baysBySize.get(size);
    }

    public int getAvailableWeaponSlotsForSize(WeaponSize size) {
        return availableWeaponSlots.get(size) == null ? 0 : availableWeaponSlots.get(size);
    }

    public int getAvailableComponentSlotsForType(ComponentType type) {
        return availableComponentSlots.get(type) == null ? 0 : availableComponentSlots.get(type);
    }

    public int getBaseEvasion() {
        return baseEvasion;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public int getBaseContainment() {
        return baseContainment;
    }

    public Map<HullSize, Integer> getBaysBySize() {
        return baysBySize;
    }

    public HullSize getHullSize() {
        return sizeCategory;
    }

    public int getBaseEPM() {
        return baseEPM;
    }

    public int getValue() {
        return value;
    }
}
