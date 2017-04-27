package ships.blueprints;

import java.io.IOException;
import java.util.HashMap;
import java.util.IntSummaryStatistics;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import battle.BattleConstants;
import battle.ShipInstance;
import ships.resourceLoader.ComponentResourceLoader;
import ships.resourceLoader.WeaponResourceLoader;
import ships.shipHulls.ComponentType;
import ships.shipHulls.HullSize;
import ships.shipHulls.HullType;
import ships.shipHulls.WeaponSize;

public class Blueprint {
    private final HullType hullType;

    private final Map<WeaponSize, List<WeaponBlueprint>> weapons;
    private final Map<ComponentType, List<ComponentBlueprint>> components;
    private final Map<Blueprint, Integer> fightersInBay;
    
    private PropulsionLayout propulsion;

    private final String name;
    private final String description;

    // Shields
    private final MutableBaseStat maxShieldStrength;
    private final MutableBaseStat shieldRegenerationAmount;
    private final MutableBaseStat shieldBreakDuration;

    // Speed
    private final MutableBaseStat startBattleSpeed;
    private final MutableBaseStat evasion;

    // Armor
    private final MutableBaseStat glanceThreshold;
    private final MutableBaseStat hitThreshold;
    private final MutableBaseStat critThreshold;
    private final MutableBaseStat maxHullStrength;
    private final MutableBaseStat containment;
    
    //XXX MutableBaseStats might be useful for modifiers that are independent of components, e.g. technology?
    //    Otherwise they don't make much sense here, a simple int would do.

    public Blueprint(String name, String description, HullType hullType) {
        this.name = name;
        this.description = description;
        this.hullType = hullType;

        maxShieldStrength = new MutableBaseStat(0);
        shieldRegenerationAmount = new MutableBaseStat(0);
        shieldBreakDuration = new MutableBaseStat(0);
        
        evasion = new MutableBaseStat(hullType.getBaseEvasion());

        // TODO: Boni zu bestimmten Waffenkategorien/-größen etc.?

        glanceThreshold = new MutableBaseStat(hullType.getBaseGlanceThreshold());
        hitThreshold = new MutableBaseStat(hullType.getBaseHitThreshold());
        critThreshold = new MutableBaseStat(hullType.getBaseCritThreshold());
        maxHullStrength = new MutableBaseStat(hullType.getBaseHullStrength());
        containment = new MutableBaseStat(hullType.getBaseContainment());

        startBattleSpeed = new MutableBaseStat(hullType.getBaseStartBattleSpeed());

        weapons = new HashMap<WeaponSize, List<WeaponBlueprint>>();
        for (WeaponSize size : WeaponSize.values()) {
            weapons.put(size, new LinkedList<WeaponBlueprint>());
        }

        components = new HashMap<ComponentType, List<ComponentBlueprint>>();
        for (ComponentType type : ComponentType.values()) {
            components.put(type, new LinkedList<ComponentBlueprint>());
        }
        
        fightersInBay = new HashMap<Blueprint, Integer>();
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

    public int getMaxShieldStrength() {
        return maxShieldStrength.getCalculatedValue();
    }

    public int getShieldRegenerationAmount() {
        return shieldRegenerationAmount.getCalculatedValue();
    }

    public int getShieldBreakDuration() {
        return shieldBreakDuration.getCalculatedValue();
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

    public int getMaxHullStrength() {
        return maxHullStrength.getCalculatedValue();
    }

    public int getContainment() {
        return containment.getCalculatedValue();
    }

    public int getStartBattleSpeed() {
        return startBattleSpeed.getCalculatedValue();
    }

    public Map<Blueprint, Integer> getFighterTypesInBay() {
        return fightersInBay;
    }
    
    public int getValueOfShipInstance() {
        int value = hullType.getValue();
        value += getWeapons().stream().collect(Collectors.summarizingInt(e -> e.getValue())).getSum();
        value += (getComponents().stream().collect(Collectors.summarizingInt(e -> e.getValue())).getSum() * BattleConstants.shipSizeScaling.get(hullType.getHullSize()));
        return value;
    }
    
    public void addFightersToBay(Blueprint fighter, int num) throws InstantiationException {
        int availableShipBaysForSize = hullType.getAvailableShipBaysForSize(fighter.getHullSize());
        for (Blueprint fighterInBay: fightersInBay.keySet()) {
            if (fighterInBay.getHullSize().equals(fighter.getHullSize())) {
                availableShipBaysForSize -= fightersInBay.get(fighterInBay);
            }
        }
        if (num > availableShipBaysForSize) {
            throw new InstantiationException("Too many fighters specified for " + this);
        }
        fightersInBay.put(fighter, fightersInBay.keySet().contains(fighter) ? fightersInBay.get(fighter) + num : num);
    }

    private HullSize getHullSize() {
        return this.hullType.getHullSize();
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

    public void addStandardWeapon(String weaponBlueprintName) throws NotEnoughtSlotsException, NumberFormatException, IOException, InstantiationException {
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
