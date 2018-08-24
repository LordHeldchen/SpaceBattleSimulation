package de.rauwolf.gaming.battleships.gui.blueprintToText;

import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;

public interface BlueprintToText {
    String toText(Blueprint blueprint);

    void setCurrentShipBlueprint(ShipBlueprint ship);
}
