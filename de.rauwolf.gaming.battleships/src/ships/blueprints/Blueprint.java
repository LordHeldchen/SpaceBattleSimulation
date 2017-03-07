package ships.blueprints;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import battle.BattleConstants;
import battle.WeaponInstance;
import ships.basicShips.HullType;
import ships.basicShips.SizeEnum;

public class Blueprint {
    // For all following elements: May be reduced from Lists to single elements if Ships may not contain more than one.
    private final HullType        hullType;

    private Map<SizeEnum, List<WeaponBlueprint>> weapons;
    private List<Component>       components;
    private PropulsionLayout      propulsion;

    private final String          name;
    private final String          description;
    
    // Shields
    private final MutableBaseStat maxShieldStrength;
    private final MutableBaseStat shieldRegenerationAmount;
    private final MutableBaseStat shieldRegenerationSpeed;

    // Speed
    private final MutableBaseStat startBattleSpeed;
    private final MutableBaseStat evasion;

    // Armor
    private final MutableBaseStat glanceThreshold;
    private final MutableBaseStat hitThreshold;
    private final MutableBaseStat critThreshold;
    private final MutableBaseStat maxHullStrength;

    public Blueprint(String name, String description, HullType hullType) {
        this.name = name;
        this.description = description;
        this.hullType = hullType;

        maxShieldStrength = new MutableBaseStat(0);
        shieldRegenerationAmount = new MutableBaseStat(0);
        shieldRegenerationSpeed = new MutableBaseStat(0);
        evasion = new MutableBaseStat(0);

        //TODO: Boni zu bestimmten Waffenkategorien/-größen etc.

        glanceThreshold = new MutableBaseStat(hullType.getBaseGlanceThreshold());
        hitThreshold = new MutableBaseStat(hullType.getBaseHitThreshold());
        critThreshold = new MutableBaseStat(hullType.getBaseCritThreshold());
        maxHullStrength = new MutableBaseStat(hullType.getBaseHullStrength());

        startBattleSpeed = new MutableBaseStat(hullType.getBaseStartBattleSpeed());
    }
    
    public HullType getHullType() {
        return hullType;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public List<Component> getComponents() {
        return components;
    }

    public PropulsionLayout getPropulsion() {
        return propulsion;
    }

    public int getEvasion() {
        return evasion.getCalculatedValue();
    }

    public int getMaxShieldStrength() {
        return maxShieldStrength.getCalculatedValue();
    }

    public int getShieldRegenerationAmount() {
        return shieldRegenerationAmount.getCalculatedValue();
    }

    public int getShieldInitiativeDecay() {
        return shieldRegenerationSpeed.getCalculatedValue();
    }

    public int getArmorGlanceThreshold() {
        return glanceThreshold.getCalculatedValue();
    }

    public int getArmorHitThreshold() {
        return hitThreshold.getCalculatedValue();
    }

    public int getArmorCritThreshold() {
        return critThreshold.getCalculatedValue();
    }

    public MutableBaseStat getMaxHullStrength() {
        return maxHullStrength;
    }

    public MutableBaseStat getStartBattleSpeed() {
        return startBattleSpeed;
    }

    public List<Blueprint> getFighterTypesInBay() {
        //TODO: Define, allow configuration, ...
        return new LinkedList<Blueprint>();
    }

	public void addWeapon(WeaponBlueprint weaponBlueprint) throws NotEnoughtSlotsException {
		SizeEnum size = weaponBlueprint.getSize();
		List<WeaponBlueprint> weaponsForSize = weapons.get(size);
		if (weaponsForSize.size() < hullType.getAvailableWeaponSlotsForSize(size)) {
			weaponsForSize.add(weaponBlueprint);
		} else {
			throw new NotEnoughtSlotsException();
		}
	}

	public List<WeaponInstance> getWeaponInstances() {
		int startingBattleSpeed = startBattleSpeed.getCalculatedValue() + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);
		
		List<WeaponInstance> weaponInstances = new LinkedList<WeaponInstance>();
		for (List<WeaponBlueprint> weaponBlueprintList: this.weapons.values()) {
			for (WeaponBlueprint weaponBlueprint: weaponBlueprintList) {
				weaponInstances.add(weaponBlueprint.getInstance(startingBattleSpeed));
			}
		}
		return weaponInstances;
	}
}
