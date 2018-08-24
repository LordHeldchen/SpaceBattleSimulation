package de.rauwolf.gaming.battleships.ships;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import de.rauwolf.gaming.battleships.battle.ShipInstance;
import de.rauwolf.gaming.battleships.characters.Admiral;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

import java.util.Set;

public class Fleet {
    private HashMap<ShipBlueprint, Integer> classesOfShipsInFleet;
    private int owningEmpireID;
    
    private Admiral admiral;

    public Fleet(int owningEmpireID) {
        this.owningEmpireID = owningEmpireID;
        this.classesOfShipsInFleet = new HashMap<ShipBlueprint, Integer>();
    }

    public void add(ShipBlueprint blueprint, int amount) {
        if (classesOfShipsInFleet.containsKey(blueprint)) {
            classesOfShipsInFleet.put(blueprint, classesOfShipsInFleet.get(blueprint) + amount);
        } else {
            classesOfShipsInFleet.put(blueprint, amount);
        }
    }

    public InstantiatedFleet instantiate() {
        int instanceNum = 0;

        InstantiatedFleet fleetInstance = new InstantiatedFleet();
        Map<SizeClass, Map<StatType, Integer>> admiralFlatBoni;
        if (admiral != null) {
            admiralFlatBoni = admiral.getAllFlatBoni();
            fleetInstance.setAdmiral(admiral);
        } else {
            admiralFlatBoni = new HashMap<SizeClass, Map<StatType, Integer>>();
            for (SizeClass size: SizeClass.values()) {
                admiralFlatBoni.put(size, new HashMap<StatType, Integer>());
            }
        }

        for (ShipBlueprint blueprint: classesOfShipsInFleet.keySet()) {
            Map<StatType, Integer> boniMap = admiralFlatBoni.get(blueprint.getSize());
            for (int i = 0; i < classesOfShipsInFleet.get(blueprint); i++) {
                ShipInstance shipInstance = blueprint.createInstance(owningEmpireID, (++instanceNum) + "");
                for (Entry<StatType, Integer> bonus: boniMap.entrySet()) {
                    shipInstance.addFlatBonusFor(bonus.getKey(), "admiral", bonus.getValue());
                }
                fleetInstance.add(shipInstance);
            }
        }
        return fleetInstance;
    }
    
    public Set<ShipBlueprint> getTypesOfShipsInFleet() {
        return classesOfShipsInFleet.keySet();
    }

    private int getValueOfShipsInBay(Map<ShipBlueprint, Integer> map) {
        int fleetValue = 0;
        for (ShipBlueprint shipClass : map.keySet()) {
            fleetValue += shipClass.getValueOfShipInstance() * map.get(shipClass);
            fleetValue += getValueOfShipsInBay(shipClass.getFighterTypesInBay()) * map.get(shipClass);
        }
        return fleetValue;
    }
    
    private String getTypesOfShipsInBay(Map<ShipBlueprint, Integer> map, int level, int multiplier) {
        String ret = "";
        for (ShipBlueprint shipClass : map.keySet()) {
            ret += String.join("", Collections.nCopies(level, "  ")); 
            ret += "# " + shipClass.getName() + " --> " + map.get(shipClass) * multiplier + "\n";
            ret += getTypesOfShipsInBay(shipClass.getFighterTypesInBay(), level + 1, multiplier * map.get(shipClass));
        }
        return ret;
    }

    public String toString() {
        String ret = classesOfShipsInFleet.size() + " Ships in this fleet:\n";
        int fleetValue = 0;
        for (ShipBlueprint shipClass : classesOfShipsInFleet.keySet()) {
            ret += "# " + shipClass.getName() + " --> " + classesOfShipsInFleet.get(shipClass) + "\n";
            ret += getTypesOfShipsInBay(shipClass.getFighterTypesInBay(), 1, classesOfShipsInFleet.get(shipClass));
            fleetValue += shipClass.getValueOfShipInstance() * classesOfShipsInFleet.get(shipClass);
            fleetValue += getValueOfShipsInBay(shipClass.getFighterTypesInBay()) * classesOfShipsInFleet.get(shipClass);
        }
        if (admiral != null) {
            ret += admiral.toString();
        }
        ret += " --> Fleet value: " + fleetValue + "\n";
        return ret;
    }

    public Admiral getAdmiral() {
        return admiral;
    }

    public void setAdmiral(Admiral admiral) {
        this.admiral = admiral;
    }
}
