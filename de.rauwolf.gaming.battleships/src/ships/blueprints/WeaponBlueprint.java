package ships.blueprints;

import java.util.HashMap;
import java.util.Map;

import ships.shipHulls.WeaponSize;

public class WeaponBlueprint {
    private final String name;
    private final WeaponSize size;
    
    private final int accuracy;
	private final int damage;
	private final int armorPenetration;

	// TODO: Payload remains a questionable concept.
	// private final int maxPayload;

	// TODO: Introduce reload time e.g. for BurstFire of several rounds?
	private final int timeCost;

	protected Map<Class<? extends Blueprint>, Integer> preferredTargets;

	public WeaponBlueprint(String name, WeaponSize size, int accuracy, int damage, int armorPenetration, int timeCost) {
        this.name = name;
        this.size = size;
		this.damage = damage;
		this.accuracy = accuracy;
		this.armorPenetration = armorPenetration;
		this.timeCost = timeCost;

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
	
	// TODO Differentiation per damage type, also for modifiers, might be desirable. 
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
}
