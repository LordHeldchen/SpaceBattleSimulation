package de.rauwolf.gaming.battleships.ships.blueprints;

import java.util.List;

import de.rauwolf.gaming.battleships.ships.components.ComponentBlueprint;

public interface BlueprintWithSubComponents extends Blueprint {
    public List<ComponentBlueprint> getComponents();

    boolean isDesignValid();
}
