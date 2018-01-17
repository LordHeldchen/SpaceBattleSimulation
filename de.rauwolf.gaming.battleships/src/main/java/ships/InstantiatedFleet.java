package main.java.ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;
import java.util.Map.Entry;

import main.java.battle.ShipInstance;
import main.java.characters.Admiral;
import main.java.ships.blueprints.ShipBlueprint;
import main.java.ships.blueprints.SizeClass;
import main.java.ships.stats.StatType;

public class InstantiatedFleet implements Iterable<ShipInstance>, Collection<ShipInstance> {
    private HashMap<ShipBlueprint, ArrayList<ShipInstance>> classesOfShipsInFleet;
    private HashMap<SizeClass, ArrayList<ShipInstance>>     sizesOfShipsInFleet;
    private ArrayList<ShipInstance>                         allShipsInFleet;
    private Admiral admiral;

    public InstantiatedFleet() {
        this.classesOfShipsInFleet = new HashMap<ShipBlueprint, ArrayList<ShipInstance>>();
        this.sizesOfShipsInFleet = new HashMap<SizeClass, ArrayList<ShipInstance>>();
        this.allShipsInFleet = new ArrayList<ShipInstance>();
    }
    
    @Override
    public boolean add(ShipInstance ship) {
        ArrayList<ShipInstance> shipClassList;
        if (!classesOfShipsInFleet.containsKey(ship.getBlueprint())) {
            shipClassList = new ArrayList<ShipInstance>();
            classesOfShipsInFleet.put(ship.getBlueprint(), shipClassList);
        } else {
            shipClassList = classesOfShipsInFleet.get(ship.getBlueprint());
        }
        shipClassList.add(ship);
        
        ArrayList<ShipInstance> shipSizeList;
        if (!sizesOfShipsInFleet.containsKey(ship.getSizeClass())) {
            shipSizeList = new ArrayList<ShipInstance>();
            sizesOfShipsInFleet.put(ship.getSizeClass(), shipSizeList);
        } else {
            shipSizeList = sizesOfShipsInFleet.get(ship.getSizeClass());
        }
        shipSizeList.add(ship);
        return allShipsInFleet.add(ship);
    }

    @Override
    public boolean addAll(Collection<? extends ShipInstance> shipCollection) {
        for (ShipInstance ship : shipCollection) {
            ArrayList<ShipInstance> shipClassList;
            if (!classesOfShipsInFleet.containsKey(ship.getClass())) {
                shipClassList = new ArrayList<ShipInstance>();
                classesOfShipsInFleet.put(ship.getBlueprint(), shipClassList);
            } else {
                shipClassList = classesOfShipsInFleet.get(ship.getBlueprint());
            }
            shipClassList.add(ship);

            ArrayList<ShipInstance> shipSizeList;
            if (!sizesOfShipsInFleet.containsKey(ship.getSizeClass())) {
                shipSizeList = new ArrayList<ShipInstance>();
                sizesOfShipsInFleet.put(ship.getSizeClass(), shipSizeList);
            } else {
                shipSizeList = sizesOfShipsInFleet.get(ship.getSizeClass());
            }
            shipSizeList.add(ship);
        }
        return allShipsInFleet.addAll(shipCollection);
    }

    public ShipInstance get(int i) {
        return allShipsInFleet.get(i);
    }

    public boolean containsSize(SizeClass sizeType) {
        return sizesOfShipsInFleet.containsKey(sizeType);
    }

    public final ArrayList<ShipInstance> getAllOfType(Class<? extends ShipBlueprint> type) {
        return classesOfShipsInFleet.get(type);
    }

    public final ArrayList<ShipInstance> getAllOfSize(SizeClass sizeClass) {
        return sizesOfShipsInFleet.get(sizeClass);
    }

    @Override
    public void clear() {
        classesOfShipsInFleet.clear();
        allShipsInFleet.clear();
        sizesOfShipsInFleet.clear();
    }

    @Override
    public int size() {
        return allShipsInFleet.size();
    }

    @Override
    public Iterator<ShipInstance> iterator() {
        return allShipsInFleet.iterator();
    }

    @Override
    public Spliterator<ShipInstance> spliterator() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean contains(Object o) {
        return allShipsInFleet.contains(o);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return allShipsInFleet.containsAll(c);
    }

    @Override
    public boolean remove(Object obj) {
        ShipInstance ship = (ShipInstance) obj;
        ShipBlueprint blueprint = ship.getBlueprint();
        if (classesOfShipsInFleet.containsKey(blueprint)) {
            classesOfShipsInFleet.get(blueprint).remove(ship);
            if (classesOfShipsInFleet.get(blueprint).isEmpty()) {
                classesOfShipsInFleet.remove(blueprint);
            }
        }
        if (sizesOfShipsInFleet.containsKey(ship.getSizeClass())) {
            sizesOfShipsInFleet.get(ship.getSizeClass()).remove(ship);
            if (sizesOfShipsInFleet.get(ship.getSizeClass()).isEmpty()) {
                sizesOfShipsInFleet.remove(ship.getSizeClass());
            }
        }
        return allShipsInFleet.remove(ship);
    }

    @Override
    public boolean removeAll(Collection<?> shipCollection) {
        for (ShipBlueprint shipClass : classesOfShipsInFleet.keySet()) {
            classesOfShipsInFleet.get(shipClass).removeAll(shipCollection);
            if (classesOfShipsInFleet.get(shipClass).isEmpty()) {
                classesOfShipsInFleet.remove(shipClass);
            }
        }
        for (SizeClass sizeClass: sizesOfShipsInFleet.keySet()) {
            sizesOfShipsInFleet.get(sizeClass).removeAll(shipCollection);
            if (sizesOfShipsInFleet.get(sizeClass).isEmpty()) {
                sizesOfShipsInFleet.remove(sizeClass);
            }
        }
        return allShipsInFleet.removeAll(shipCollection);
    }

    @Override
    public boolean isEmpty() {
        return allShipsInFleet.isEmpty();
    }

    @Override
    public boolean retainAll(Collection<?> shipCollection) {
        for (ShipBlueprint shipClass : classesOfShipsInFleet.keySet()) {
            classesOfShipsInFleet.get(shipClass).retainAll(shipCollection);
            if (classesOfShipsInFleet.get(shipClass).isEmpty()) {
                classesOfShipsInFleet.remove(shipClass);
            }
        }
        for (SizeClass sizeClass: sizesOfShipsInFleet.keySet()) {
            sizesOfShipsInFleet.get(sizeClass).retainAll(shipCollection);
        }
        return allShipsInFleet.retainAll(shipCollection);
    }

    @Override
    public Object[] toArray() {
        return allShipsInFleet.toArray();
    }

    @Override
    public <T> T[] toArray(T[] a) {
        return allShipsInFleet.toArray(a);
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
        String ret = allShipsInFleet.size() + " Ships in this fleet:\n";
        int fleetValue = 0;
        for (ShipBlueprint shipClass : classesOfShipsInFleet.keySet()) {

            int shipTypeValue = shipClass.getValueOfShipInstance() * classesOfShipsInFleet.get(shipClass).size();
            shipTypeValue += getValueOfShipsInBay(shipClass.getFighterTypesInBay()) * classesOfShipsInFleet.get(shipClass).size();
            
            ret += "# " + String.format("%1$-" + 30 + "s", shipClass.getName()) + " --> " + classesOfShipsInFleet.get(shipClass).size() + " (value " + shipTypeValue + ")\n";
            ret += getTypesOfShipsInBay(shipClass.getFighterTypesInBay(), 1, classesOfShipsInFleet.get(shipClass).size());
            
            fleetValue += shipTypeValue;
        }
        if (admiral != null) {
            ret += admiral.toString();
        }
        ret += " --> Fleet value: " + fleetValue + "\n";
        return ret;
    }

    public void setAdmiral(Admiral admiral) {
        this.admiral = admiral;
    }
}
