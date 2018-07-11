package main.java.ships.blueprints;

import java.util.List;

import main.java.ships.components.ComponentBlueprint;
import main.java.ships.stats.StatType;

public interface BlueprintWithSubComponents {
    public SizeClass getSize();

    public Integer getStatFor(StatType stat);

    public List<ComponentBlueprint> getComponents();

    boolean isDesignValid();
}
