package ships.blueprints;

import java.util.HashMap;
import java.util.Map;

import battle.WeaponInstance;
import ships.basicShips.SizeEnum;

public class WeaponBlueprint {
	private final String name;

	private final int accuracy;
	private final int damage;
	private final int armorPenetration;
	private final SizeEnum size;

	// TODO: Payload remains a questionable concept.
	// private final int maxPayload;

	// TODO: Introduce reload time e.g. for BurstFire of several rounds?
	private final int timeCost;

	protected Map<Class<? extends Blueprint>, Integer> preferredTargets;


	public WeaponBlueprint(String name, SizeEnum size, int accuracy, int damage, int armorPenetration, int timeCost) {
		this.name = name;
		this.size = size;
		this.accuracy = accuracy;
		this.damage = damage;
		this.armorPenetration = armorPenetration;
		this.timeCost = timeCost;

		this.preferredTargets = new HashMap<Class<? extends Blueprint>, Integer>();
	}

	public Map<Class<? extends Blueprint>, Integer> getPreferredTargets() {
		return this.preferredTargets;
	}

	public String getName() {
		return this.name;
	}

	public int getAccuracy() {
		return this.accuracy;
	}

	public int getDamage() {
		return this.damage;
	}

	public int getArmorPenetration() {
		return this.armorPenetration;
	}

	public int getBattleSpeedDecay() {
		return this.timeCost;
	}

	public SizeEnum getSize() {
		return this.size;
	}

	public WeaponInstance getInstance(int startingInitiative) {
		return new WeaponInstance(this, startingInitiative, timeCost);
	}
	
	public String toString() {
		return name + " (" + size + ") -->  DMG " + damage + ", AP " + armorPenetration + ", TC " + timeCost;
	}
}
