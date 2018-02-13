package main.java.ships.blueprints;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import main.java.battle.BattleConstants;
import main.java.battle.ShipInstance;
import main.java.ships.components.ComponentBlueprint;
import main.java.ships.components.ComponentResourceLoader;
import main.java.ships.hulls.HullType;
import main.java.ships.resourceLoader.WeaponResourceLoader;
import main.java.ships.shipHulls.ComponentType;
import main.java.ships.stats.StatType;
import main.java.ships.weapons.WeaponBlueprint;

public class ShipBlueprint implements Blueprint, Comparable<ShipBlueprint> {
    private final HullType hullType;
    
    private final Map<StatType, Integer> statMap;

    private final Map<SizeClass, List<WeaponBlueprint>> weapons;
    private final Map<ComponentType, List<ComponentBlueprint>> components;
    private final Map<ShipBlueprint, Integer> fightersInBay;

    private final String shorthand;
    private final String name;
    private final String description;
    
    //XXX MutableBaseStats might be useful for modifiers that are independent of components, e.g. technology or admirals.
    //    Otherwise they don't make much sense here, a simple int would do.

    public ShipBlueprint(String shorthand, String name, String description, HullType hullType) {
        this.shorthand = shorthand;
        this.name = name;
        this.description = description;
        this.hullType = hullType;
        this.statMap = new HashMap<StatType, Integer>();
        
        for (StatType stat: StatType.values()) {
            statMap.put(stat, hullType.getBaseStatFor(stat));
        }

        weapons = new HashMap<SizeClass, List<WeaponBlueprint>>();
        for (SizeClass size : SizeClass.values()) {
            weapons.put(size, new LinkedList<WeaponBlueprint>());
        }

        components = new HashMap<ComponentType, List<ComponentBlueprint>>();
        for (ComponentType type : ComponentType.values()) {
            components.put(type, new LinkedList<ComponentBlueprint>());
        }
        
        fightersInBay = new HashMap<ShipBlueprint, Integer>();
    }

    public HullType getHullType() {
        return hullType;
    }

    public String getShorthand() {
        return shorthand;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getStatFor(StatType stat) {
        return statMap.get(stat);
    }

    public Map<ShipBlueprint, Integer> getFighterTypesInBay() {
        return fightersInBay;
    }
    
    public int getValueOfShipInstance() {
        int value = hullType.getValue();
        value += getWeapons().stream().collect(Collectors.summarizingInt(e -> e.getValue())).getSum();
        value += (getComponents().stream().collect(Collectors.summarizingInt(e -> e.getValue())).getSum() * BattleConstants.shipSizeScaling.get(hullType.getHullSize()));
        return value;
    }
    
    public void addFightersToBay(ShipBlueprint fighter, int num) throws InstantiationException {
        int availableShipBaysForSize = hullType.getAvailableShipBaysForSize(fighter.getHullSize());
        for (ShipBlueprint fighterInBay: fightersInBay.keySet()) {
            if (fighterInBay.getHullSize().equals(fighter.getHullSize())) {
                availableShipBaysForSize -= fightersInBay.get(fighterInBay);
            }
        }
        if (num > availableShipBaysForSize) {
            throw new InstantiationException("Too many fighters specified for " + this);
        }
        fightersInBay.put(fighter, fightersInBay.keySet().contains(fighter) ? fightersInBay.get(fighter) + num : num);
    }

    private SizeClass getHullSize() {
        return this.hullType.getHullSize();
    }

    public void addWeapon(WeaponBlueprint weaponBlueprint) throws NotEnoughtSlotsException {
        SizeClass size = weaponBlueprint.getSize();
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

    public ShipInstance createInstance(int owningEmpire, String instanceId) {
        return new ShipInstance(owningEmpire, null, this, instanceId);
    }

    public List<ComponentBlueprint> getComponents() {
        return components.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    public List<WeaponBlueprint> getWeapons() {
        return this.weapons.values().stream().flatMap(List::stream).collect(Collectors.toList());
    }

    @Override
    public String toString() {
        return name + " (" + hullType + ")";
    }

    @Override
    public SizeClass getSize() {
        return hullType.getHullSize();
    }

    @Override
    public int compareTo(ShipBlueprint o) {
        int res = hullType.getHullSize().compareTo(o.hullType.getHullSize());
        if (res == 0) {
            res = hullType.getName().compareTo(o.hullType.getName());
        }
        return res;
    }
}
