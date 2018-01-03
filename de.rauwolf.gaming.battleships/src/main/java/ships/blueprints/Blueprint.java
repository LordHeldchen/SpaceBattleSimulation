package main.java.ships.blueprints;

import java.util.LinkedList;
import java.util.List;

import main.java.ships.components.ComponentBlueprint;
import main.java.ships.stats.StatType;

public interface Blueprint {
    public Integer getStatFor(StatType stat);

    default public List<ComponentBlueprint> getComponents() {
        return new LinkedList<ComponentBlueprint>();
    }

    public SizeClass getSize();
}
