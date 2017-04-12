package ships.blueprints;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import battle.ShipInstance;
import ships.resourceLoader.ComponentResourceLoader;
import ships.resourceLoader.WeaponResourceLoader;
import ships.shipHulls.ComponentType;
import ships.shipHulls.HullType;
import ships.shipHulls.WeaponSize;

public class Blueprint {
    private final HullType hullType;

    private Map<WeaponSize, List<WeaponBlueprint>> weapons;

    private Map<ComponentType, List<ComponentBlueprint>> components;
    private PropulsionLayout propulsion;

    private final String name;
    private final String description;

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
        evasion = new MutableBaseStat(hullType.getBaseEvasion());

        // TODO: Boni zu bestimmten Waffenkategorien/-größen etc.?

        glanceThreshold = new MutableBaseStat(hullType.getBaseGlanceThreshold());
        hitThreshold = new MutableBaseStat(hullType.getBaseHitThreshold());
        critThreshold = new MutableBaseStat(hullType.getBaseCritThreshold());
        maxHullStrength = new MutableBaseStat(hullType.getBaseHullStrength());

        startBattleSpeed = new MutableBaseStat(hullType.getBaseStartBattleSpeed());

        weapons = new HashMap<WeaponSize, List<WeaponBlueprint>>();
        for (WeaponSize size : WeaponSize.values()) {
            weapons.put(size, new LinkedList<WeaponBlueprint>());
        }

        components = new HashMap<ComponentType, List<ComponentBlueprint>>();
        for (ComponentType type : ComponentType.values()) {
            components.put(type, new LinkedList<ComponentBlueprint>());
        }
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

    public PropulsionLayout getPropulsion() {
        return propulsion;
    }

    public int getEvasion() {
        return evasion.getCalculatedValue();
    }

    MutableBaseStat getBaseEvasion() {
        return evasion;
    }

    public int getMaxShieldStrength() {
        return maxShieldStrength.getCalculatedValue();
    }

    MutableBaseStat getBaseMaxShieldStrength() {
        return maxShieldStrength;
    }

    public int getShieldRegenerationAmount() {
        return shieldRegenerationAmount.getCalculatedValue();
    }

    MutableBaseStat getBaseShieldRegenerationAmount() {
        return shieldRegenerationAmount;
    }

    public int getShieldInitiativeDecay() {
        return shieldRegenerationSpeed.getCalculatedValue();
    }

    MutableBaseStat getBaseShieldInitiativeDecay() {
        return shieldRegenerationSpeed;
    }

    public int getArmorGlanceThreshold() {
        return glanceThreshold.getCalculatedValue();
    }

    MutableBaseStat getBaseArmorGlanceThreshold() {
        return glanceThreshold;
    }

    public int getArmorHitThreshold() {
        return hitThreshold.getCalculatedValue();
    }

    MutableBaseStat getBaseHitGlanceThreshold() {
        return hitThreshold;
    }

    public int getArmorCritThreshold() {
        return critThreshold.getCalculatedValue();
    }

    MutableBaseStat getBaseArmorCritThreshold() {
        return critThreshold;
    }

    public int getMaxHullStrength() {
        return maxHullStrength.getCalculatedValue();
    }

    MutableBaseStat getBaseMaxHullStrength() {
        return maxHullStrength;
    }

    public int getStartBattleSpeed() {
        return startBattleSpeed.getCalculatedValue();
    }

    MutableBaseStat getBaseStartBattleSpeed() {
        return startBattleSpeed;
    }

    public List<Blueprint> getFighterTypesInBay() {
        // TODO: Define, allow configuration, ...
        return new LinkedList<Blueprint>();
    }

    public void addWeapon(WeaponBlueprint weaponBlueprint) throws NotEnoughtSlotsException {
        WeaponSize size = weaponBlueprint.getSize();
        List<WeaponBlueprint> weaponsForSize = weapons.get(size);
        if (weaponsForSize.size() < hullType.getAvailableWeaponSlotsForSize(size)) {
            weaponsForSize.add(weaponBlueprint);
        } else {
            throw new NotEnoughtSlotsException("Not enough weapon slots for " + weaponBlueprint + "(" + size + ") in hull of " + hullType);
        }
    }

    public void addStandardWeapon(String weaponBlueprintName) throws NotEnoughtSlotsException, NumberFormatException, IOException {
        addWeapon(WeaponResourceLoader.getStandardWeaponBlueprint(weaponBlueprintName));
    }

    public void addComponent(ComponentBlueprint componentBlueprint) throws NotEnoughtSlotsException {
        ComponentType type = componentBlueprint.getType();
        List<ComponentBlueprint> componentsForType = components.get(type);

        if (componentsForType.size() < hullType.getAvailableComponentSlotsForType(type)) {
            componentsForType.add(componentBlueprint);
        } else {
            List<ComponentBlueprint> allroundComponents = components.get(ComponentType.ALLROUND);
            if (allroundComponents.size() < hullType.getAvailableComponentSlotsForType(ComponentType.ALLROUND)) {
                allroundComponents.add(componentBlueprint);
            } else {
                throw new NotEnoughtSlotsException("Not enough component slots for " + componentBlueprint + "(" + type + ") in hull of " + hullType);
            }
        }
    }

    public void addStandardComponent(String componentBlueprintName)
            throws NotEnoughtSlotsException, NumberFormatException, InstantiationException, IOException {
        addComponent(ComponentResourceLoader.getStandardComponentBlueprint(componentBlueprintName));
    }

    public ShipInstance createInstance(int owningEmpire) {
        return new ShipInstance(owningEmpire, null, this);
    }

    public List<ComponentBlueprint> getComponents() {
        return components.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<WeaponBlueprint> getWeapons() {
        return this.weapons.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name + "(" + hullType + ")";
    }
}
