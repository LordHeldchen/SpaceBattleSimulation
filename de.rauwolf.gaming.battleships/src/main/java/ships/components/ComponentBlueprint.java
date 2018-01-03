package main.java.ships.components;

import java.util.Map;

import main.java.ships.shipHulls.ComponentType;
import main.java.ships.stats.StatType;

public class ComponentBlueprint implements Component {
    private final String shorthand;
    private final String name;
    private final ComponentType type;
    private final int value;
    private final Map<StatType, Double> factors;
    private final Map<StatType, Integer> flatBonuses;

    public ComponentBlueprint(String shorthand, String name, ComponentType type, int value, Map<StatType, Double> factors, Map<StatType, Integer> flatBonuses) {
        this.shorthand = shorthand;
        this.name = name;
        this.type = type;
        this.value = value;
        this.factors = factors;
        this.flatBonuses = flatBonuses;
    }

    @Override
    public String getShorthand() {
        return shorthand;
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
    public Double getFactorFor(StatType value) {
        return factors.get(value);
    }

    @Override
    public Integer getFlatBonusFor(StatType value) {
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
