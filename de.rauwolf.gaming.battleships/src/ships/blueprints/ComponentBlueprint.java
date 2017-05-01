package ships.blueprints;

import java.util.Map;

import ships.shipHulls.HullStatType;
import ships.shipHulls.ComponentType;

public class ComponentBlueprint {
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

    public final String getName() {
        return name;
    }

    public ComponentType getType() {
        return type;
    }

    public Double getFactorFor(HullStatType value) {
        return factors.get(value);
    }

    public Integer getFlatBonusFor(HullStatType value) {
        return flatBonuses.get(value);
    }
    
    @Override
    public String toString() {
        return name;
    }

    public int getValue() {
        return value;
    }
}
