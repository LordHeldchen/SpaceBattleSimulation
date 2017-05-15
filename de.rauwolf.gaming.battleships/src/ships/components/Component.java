package ships.components;

import ships.shipHulls.ComponentType;
import ships.stats.HullStatType;

public interface Component {
    public String getName();

    public ComponentType getType();

    public Double getFactorFor(HullStatType value);

    public Integer getFlatBonusFor(HullStatType value);

    public int getValue();
}
