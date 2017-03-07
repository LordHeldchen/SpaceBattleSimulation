package ships.basicShips;

import java.util.HashMap;
import java.util.Map;

public class HullType {
	private Map<SizeEnum, Integer> availableWeaponSlots = new HashMap<SizeEnum, Integer>();
	private Map<SizeEnum, Integer> availableComponentSlots = new HashMap<SizeEnum, Integer>();

	private final int baseGlanceThreshold;
	private final int baseHitThreshold;
	private final int baseCritThreshold;
	private final int baseHullStrength;
	private final int baseStartInitiative;

	public HullType(int baseGlanceThreshold, int baseHitThreshold, int baseCritThreshold, int baseHullStrength, int baseStartInitiative) {
		this.baseGlanceThreshold = baseGlanceThreshold;
		this.baseHitThreshold = baseHitThreshold;
		this.baseCritThreshold = baseCritThreshold;
		this.baseHullStrength = baseHullStrength;
		this.baseStartInitiative = baseStartInitiative;
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
	
	public void setAvailableWeaponSlotsForSize(SizeEnum size, int num) {
		availableWeaponSlots.put(size, num);
	}
	
	public void setAvailableComponentSlotsForSize(SizeEnum size, int num) {
		availableComponentSlots.put(size, num);
	}

	public int getAvailableWeaponSlotsForSize(SizeEnum size) {
		return availableWeaponSlots.get(size) == null ? 0 : availableWeaponSlots.get(size);
	}
}
