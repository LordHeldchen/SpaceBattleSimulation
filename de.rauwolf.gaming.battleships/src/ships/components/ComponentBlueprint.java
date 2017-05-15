package ships.components;

import java.util.Map;

import ships.shipHulls.ComponentType;
import ships.stats.HullStatType;

public class ComponentBlueprint implements Component {
    private final String name;
    private final ComponentType type;
    private final int value;
    private final Map<HullStatType, Double> factors;
    private final Map<HullStatType, Integer> flatBonuses;

    public ComponentBlueprint(String name, ComponentType type, int value, Map<HullStatType, Double> factors, Map<HullStatType, Integer> flatBonuses) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.factors = factors;
        this.flatBonuses = flatBonuses;
    }

    @Override
    public final String getName() {
        return name;
    }

    @Override
    public ComponentType getType() {
        return type;
    }

    @Override
    public Double getFactorFor(HullStatType value) {
        return factors.get(value);
    }

    @Override
    public Integer getFlatBonusFor(HullStatType value) {
        return flatBonuses.get(value);
    }

    @Override
    public int getValue() {
        return value;
    }
    
    @Override
    public String toString() {
        return name;
    }
}
