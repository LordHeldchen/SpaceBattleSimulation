package ships.blueprints;

import java.util.HashMap;
import java.util.Map;

public class WeaponBlueprint {
    private final String                               name;

    private final int                                  accuracy;
    private final int                                  damage;
    private final int                                  armorPenetration;

    private final int                                  maxPayload;

    //TODO: Introduce reload time e.g. for BurstFire of several rounds?
    private final int                                  battlespeedDecay;

    protected Map<Class<? extends Blueprint>, Integer> preferredTargets;

    public WeaponBlueprint(String name, int accuracy, int damage, int armorPenetration, int maxPayload,
                    int battlespeedDecay) {
        this.name = name;
        this.accuracy = accuracy;
        this.damage = damage;
        this.armorPenetration = armorPenetration;
        this.maxPayload = maxPayload;
        this.battlespeedDecay = battlespeedDecay;

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

    public int getMaxPayload() {
        return maxPayload;
    }

    public int getBattleSpeedDecay() {
        return battlespeedDecay;
    }
}
