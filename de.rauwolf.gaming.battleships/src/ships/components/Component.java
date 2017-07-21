package ships.components;

import ships.shipHulls.ComponentType;
import ships.stats.StatType;

public interface Component {
    public String getShorthand();
    
    public String getName();

    public ComponentType getType();

    public Double getFactorFor(StatType value);

    public Integer getFlatBonusFor(StatType value);

    public int getValue();

}
