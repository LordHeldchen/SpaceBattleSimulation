package ships;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import ships.blueprints.ShipBlueprint;

public class Fleet {
    private HashMap<ShipBlueprint, Integer> classesOfShipsInFleet;
    private int owningEmpireID;

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
        InstantiatedFleet instance = new InstantiatedFleet();
        for (ShipBlueprint blueprint: classesOfShipsInFleet.keySet()) {
            for (int i = 0; i < classesOfShipsInFleet.get(blueprint); i++) {
                instance.add(blueprint.createInstance(owningEmpireID));
            }
        }
        return instance;
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
        ret += " --> Fleet value: " + fleetValue + "\n";
        return ret;
    }
}
