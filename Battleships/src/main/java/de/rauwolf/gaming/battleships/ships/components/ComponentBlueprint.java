package de.rauwolf.gaming.battleships.ships.components;

import java.util.Map;

import de.rauwolf.gaming.battleships.battle.BattleConstants;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

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
    public Integer getFlatBonusFor(StatType value) {
        if (!flatBonuses.containsKey(value)) {
            return 0;
        }
        return flatBonuses.get(value);
    }

    @Override
    public Double getFactorFor(StatType value) {
        if (!factors.containsKey(value)) {
            return 1d;
        }
        return factors.get(value);
    }

    @Override
    public int getValue(SizeClass sizeOfShip) {
        return value * BattleConstants.shipSizeScaling.get(sizeOfShip);
    }
    
    @Override
    public String toString() {
        return name;
    }
}
