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
	private final int initiativeDecay;

	protected Map<Class<? extends Blueprint>, Integer> preferredTargets;


	public WeaponBlueprint(String name, SizeEnum size, int accuracy, int damage, int armorPenetration, int battlespeedDecay) {
		this.name = name;
		this.size = size;
		this.accuracy = accuracy;
		this.damage = damage;
		this.armorPenetration = armorPenetration;
		this.initiativeDecay = battlespeedDecay;

		this.preferredTargets = new HashMap<Class<? extends Blueprint>, Integer>();
	}

	public Map<Class<? extends Blueprint>, Integer> getPreferredTargets() {
		return preferredTargets;
	}

	public String getName() {
		return name;
	}

	public int getAccuracy() {
		return accuracy;
	}

	public int getDamage() {
		return damage;
	}

	public int getArmorPenetration() {
		return armorPenetration;
	}

	public int getBattleSpeedDecay() {
		return initiativeDecay;
	}

	public SizeEnum getSize() {
		return this.size;
	}

	public WeaponInstance getInstance(int startingInitiative) {
		return new WeaponInstance(this, startingInitiative, initiativeDecay);
	}
}
