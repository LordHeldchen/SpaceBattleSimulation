package main.java.ships.components;

import main.java.ships.shipHulls.ComponentType;
import main.java.ships.stats.StatType;

public interface Component {
    public String getShorthand();
    
    public String getName();

    public ComponentType getType();

    public Double getFactorFor(StatType value);

    public Integer getFlatBonusFor(StatType value);

    public int getValue();

}
