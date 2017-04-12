package ships.blueprints;

import java.util.Map;

import ships.shipHulls.ComponentType;

public class ComponentBlueprint {
    private final Map<String, Double> factors;
    private final Map<String, Integer> flatBonuses;
    private final String name;
    private final ComponentType type;

    public ComponentBlueprint(String name, ComponentType type, Map<String, Double> factors, Map<String, Integer> flatBonuses) {
        this.name = name;
        this.type = type;
        this.factors = factors;
        this.flatBonuses = flatBonuses;
    }

    public final String getName() {
        return name;
    }

    public ComponentType getType() {
        return type;
    }

    public Double getFactorFor(String value) {
        return factors.get(value);
    }

    public Integer getFlatBonusFor(String value) {
        return flatBonuses.get(value);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
