package ships.shipHulls;

import java.util.HashMap;
import java.util.Map;

public class HullType {
	private Map<WeaponSize, Integer> availableWeaponSlots = new HashMap<WeaponSize, Integer>();
	private Map<ComponentType, Integer> availableComponentSlots = new HashMap<ComponentType, Integer>();

	private final int baseGlanceThreshold;
	private final int baseHitThreshold;
	private final int baseCritThreshold;
	private final int baseHullStrength;
	private final int baseStartInitiative;
	private final int baseEvasion;
	private final String description;
	private final String name;

	public HullType(String name, int baseGlanceThreshold, int baseHitThreshold, int baseCritThreshold, int baseHullStrength, int baseStartInitiative, int baseEvasion, String description) {
		this.name = name;
		this.baseGlanceThreshold = baseGlanceThreshold;
		this.baseHitThreshold = baseHitThreshold;
		this.baseCritThreshold = baseCritThreshold;
		this.baseHullStrength = baseHullStrength;
		this.baseStartInitiative = baseStartInitiative;
		this.baseEvasion = baseEvasion;
		this.description = description;
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
	
	public void setAvailableWeaponSlotsForSize(WeaponSize size, int num) {
		availableWeaponSlots.put(size, num);
	}
	
	public void setAvailableComponentSlotsForType(ComponentType type, int num) {
		availableComponentSlots.put(type, num);
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
	
	public String toString() {
	    return name;
	}
}
