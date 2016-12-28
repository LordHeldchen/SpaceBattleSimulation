package ships.blueprints;

import java.util.LinkedList;
import java.util.List;

import ships.basicShips.HullType;

public class Blueprint {
    // For all following elements: May be reduced from Lists to single elements if Ships may not contain more than one.
    private final HullType        hullType;

    private List<WeaponBlueprint> weapons;
    private List<Component>       components;
    private ArmorLayout           armor;
    private ShieldLayout          shields;
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

        maxShieldStrength = new MutableBaseStat(hullType.getMaxShieldStrength());
        shieldRegenerationAmount = new MutableBaseStat(hullType.getShieldRegenerationAmount());
        shieldRegenerationSpeed = new MutableBaseStat(hullType.getShieldRegenerationSpeed());

        //TODO: Boni zu bestimmten Waffenkategorien/-größen etc.
        evasion = new MutableBaseStat(hullType.getMaxShieldStrength());

        glanceThreshold = new MutableBaseStat(hullType.getGlanceThreshold());
        hitThreshold = new MutableBaseStat(hullType.getHitThreshold());
        critThreshold = new MutableBaseStat(hullType.getCritThreshold());
        maxHullStrength = new MutableBaseStat(hullType.getHullStrength());

        startBattleSpeed = new MutableBaseStat(0);
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

    public List<WeaponBlueprint> getWeapons() {
        return weapons;
    }

    public List<Component> getComponents() {
        return components;
    }

    public PropulsionLayout getPropulsion() {
        return propulsion;
    }

    public ArmorLayout getArmor() {
        return armor;
    }

    public ShieldLayout getShields() {
        return shields;
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
}
