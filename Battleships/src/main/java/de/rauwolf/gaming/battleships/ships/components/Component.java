package de.rauwolf.gaming.battleships.ships.components;

import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

public interface Component extends Blueprint, Typed {
    public String getShorthand();
    
    public Double getFactorFor(StatType value);

    public Integer getFlatBonusFor(StatType value);

    public int getValue(SizeClass sizeOfShip);

}
