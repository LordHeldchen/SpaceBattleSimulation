package ships;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Spliterator;

import ships.blueprints.Blueprint;

public class Fleet implements Iterable<Ship>, Collection<Ship> {
    private HashMap<Blueprint, ArrayList<Ship>> classesOfShipsInFleet;
    private ArrayList<Ship>                     allShipsInFleet;

    public Fleet() {
        this.classesOfShipsInFleet = new HashMap<Blueprint, ArrayList<Ship>>();
        this.allShipsInFleet = new ArrayList<Ship>();
    }

    @Override
    public boolean add(Ship ship) {
        ArrayList<Ship> shipClassList;
        if (!classesOfShipsInFleet.containsKey(ship.getBlueprint())) {
            shipClassList = new ArrayList<Ship>();
            classesOfShipsInFleet.put(ship.getBlueprint(), shipClassList);
        } else {
            shipClassList = classesOfShipsInFleet.get(ship.getBlueprint());
        }
        shipClassList.add(ship);
        return allShipsInFleet.add(ship);
    }

    @Override
    public boolean addAll(Collection<? extends Ship> shipCollection) {
        for (Ship ship : shipCollection) {
            ArrayList<Ship> shipClassList;
            if (!classesOfShipsInFleet.containsKey(ship.getClass())) {
                shipClassList = classesOfShipsInFleet.put(ship.getBlueprint(), new ArrayList<Ship>());
            } else {
                shipClassList = classesOfShipsInFleet.get(ship.getBlueprint());
            }
            shipClassList.add(ship);
        }
        return allShipsInFleet.addAll(shipCollection);
    }

    public Ship get(int i) {
        return allShipsInFleet.get(i);
    }

    public boolean containsType(Class<? extends Blueprint> type) {
        return classesOfShipsInFleet.containsKey(type);
    }

    public final ArrayList<Ship> getAllOfType(Class<? extends Blueprint> type) {
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
    public Iterator<Ship> iterator() {
        return allShipsInFleet.iterator();
    }

    @Override
    public Spliterator<Ship> spliterator() {
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
