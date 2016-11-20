package ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Spliterator;

import battle.ShipInstance;
import ships.blueprints.Blueprint;

public class Fleet implements Iterable<ShipInstance>, Collection<ShipInstance> {
    private HashMap<Blueprint, ArrayList<ShipInstance>> classesOfShipsInFleet;
    private ArrayList<ShipInstance>                     allShipsInFleet;

    public Fleet() {
        this.classesOfShipsInFleet = new HashMap<Blueprint, ArrayList<ShipInstance>>();
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
        return allShipsInFleet.add(ship);
    }

    @Override
    public boolean addAll(Collection<? extends ShipInstance> shipCollection) {
        for (ShipInstance ship : shipCollection) {
            ArrayList<ShipInstance> shipClassList;
            if (!classesOfShipsInFleet.containsKey(ship.getClass())) {
                shipClassList = classesOfShipsInFleet.put(ship.getBlueprint(), new ArrayList<ShipInstance>());
            } else {
                shipClassList = classesOfShipsInFleet.get(ship.getBlueprint());
            }
            shipClassList.add(ship);
        }
        return allShipsInFleet.addAll(shipCollection);
    }

    public ShipInstance get(int i) {
        return allShipsInFleet.get(i);
    }

    public boolean containsType(Class<? extends Blueprint> type) {
        return classesOfShipsInFleet.containsKey(type);
    }

    public final ArrayList<ShipInstance> getAllOfType(Class<? extends Blueprint> type) {
        return classesOfShipsInFleet.get(type);
    }

    @Override
    public void clear() {
        classesOfShipsInFleet.clear();
        allShipsInFleet.clear();
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
    public boolean remove(Object ship) {
        if (classesOfShipsInFleet.containsKey(ship.getClass())) {
            classesOfShipsInFleet.get(ship.getClass()).remove(ship);
            if (classesOfShipsInFleet.get(ship.getClass()).isEmpty()) {
                classesOfShipsInFleet.remove(ship.getClass());
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

    public String toString() {
        String ret = allShipsInFleet.size() + " Ships in this fleet:\n";
        for (Blueprint shipClass : classesOfShipsInFleet.keySet()) {
            ret += "# " + shipClass.getName() + " --> " + classesOfShipsInFleet.get(shipClass).size() + "\n";
        }
        return ret;
    }
}
