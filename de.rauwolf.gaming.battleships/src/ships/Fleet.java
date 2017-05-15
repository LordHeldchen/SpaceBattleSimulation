package ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Spliterator;

import battle.ShipInstance;
import ships.blueprints.Blueprint;
import ships.hulls.HullSize;

public class Fleet implements Iterable<ShipInstance>, Collection<ShipInstance> {
    private HashMap<Blueprint, ArrayList<ShipInstance>> classesOfShipsInFleet;
    private HashMap<HullSize, ArrayList<ShipInstance>>  sizesOfShipsInFleet;
    private ArrayList<ShipInstance>                     allShipsInFleet;

    public Fleet() {
        this.classesOfShipsInFleet = new HashMap<Blueprint, ArrayList<ShipInstance>>();
        this.sizesOfShipsInFleet = new HashMap<HullSize, ArrayList<ShipInstance>>();
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

    public boolean containsSize(HullSize sizeType) {
        return sizesOfShipsInFleet.containsKey(sizeType);
    }

    public final ArrayList<ShipInstance> getAllOfType(Class<? extends Blueprint> type) {
        return classesOfShipsInFleet.get(type);
    }

    public final ArrayList<ShipInstance> getAllOfSize(HullSize sizeClass) {
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
        //TODO: "Echten" iterator implementieren. Für aktuellen Kontext nicht notwendig, aber Kontrakte gehören eingehalten.
        return allShipsInFleet.iterator();
    }

    @Override
    public Spliterator<ShipInstance> spliterator() {
        return allShipsInFleet.spliterator();
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
        Blueprint blueprint = ship.getBlueprint();
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
        for (Blueprint shipClass : classesOfShipsInFleet.keySet()) {
            classesOfShipsInFleet.get(shipClass).removeAll(shipCollection);
            if (classesOfShipsInFleet.get(shipClass).isEmpty()) {
                classesOfShipsInFleet.remove(shipClass);
            }
        }
        for (HullSize sizeClass: sizesOfShipsInFleet.keySet()) {
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
        for (Blueprint shipClass : classesOfShipsInFleet.keySet()) {
            classesOfShipsInFleet.get(shipClass).retainAll(shipCollection);
            if (classesOfShipsInFleet.get(shipClass).isEmpty()) {
                classesOfShipsInFleet.remove(shipClass);
            }
        }
        for (HullSize sizeClass: sizesOfShipsInFleet.keySet()) {
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
    
    private int getValueOfShipsInBay(Map<Blueprint, Integer> map) {
        int fleetValue = 0;
        for (Blueprint shipClass : map.keySet()) {
            fleetValue += shipClass.getValueOfShipInstance() * map.get(shipClass);
            fleetValue += getValueOfShipsInBay(shipClass.getFighterTypesInBay()) * map.get(shipClass);
        }
        return fleetValue;
    }
    
    private String getTypesOfShipsInBay(Map<Blueprint, Integer> map, int level, int multiplier) {
        String ret = "";
        for (Blueprint shipClass : map.keySet()) {
            ret += String.join("", Collections.nCopies(level, "  ")); 
            ret += "# " + shipClass.getName() + " --> " + map.get(shipClass) * multiplier + "\n";
            ret += getTypesOfShipsInBay(shipClass.getFighterTypesInBay(), level + 1, multiplier * map.get(shipClass));
        }
        return ret;
    }

    public String toString() {
        String ret = allShipsInFleet.size() + " Ships in this fleet:\n";
        int fleetValue = 0;
        for (Blueprint shipClass : classesOfShipsInFleet.keySet()) {
            ret += "# " + shipClass.getName() + " --> " + classesOfShipsInFleet.get(shipClass).size() + "\n";
            ret += getTypesOfShipsInBay(shipClass.getFighterTypesInBay(), 1, classesOfShipsInFleet.get(shipClass).size());
            fleetValue += shipClass.getValueOfShipInstance() * classesOfShipsInFleet.get(shipClass).size();
            fleetValue += getValueOfShipsInBay(shipClass.getFighterTypesInBay()) * classesOfShipsInFleet.get(shipClass).size();
        }
        ret += " --> Fleet value: " + fleetValue + "\n";
        return ret;
    }
}
