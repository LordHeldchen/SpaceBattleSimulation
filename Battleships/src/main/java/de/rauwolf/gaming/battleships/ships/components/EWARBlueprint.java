package de.rauwolf.gaming.battleships.ships.components;

import java.util.Map;

import de.rauwolf.gaming.battleships.battle.BattleConstants;
import de.rauwolf.gaming.battleships.battle.CombatActor;
import de.rauwolf.gaming.battleships.battle.CombatTarget;
import de.rauwolf.gaming.battleships.battle.SingleBattle;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.ComponentType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

public class EWARBlueprint extends CombatActor implements Component {
    private final String                 shorthand;
    private final String                 name;
    private final ComponentType          type;
    private final int                    value;
    private final Map<StatType, Double>  factors;
    private final Map<StatType, Integer> flatBonuses;

    public EWARBlueprint(String shorthand, String name, ComponentType type, int value, Map<StatType, Double> factors, Map<StatType, Integer> flatBonuses,
            int startInitiative, int timeCost) {
        super(startInitiative, timeCost);
        this.shorthand = shorthand;
        this.name = name;
        this.type = type;
        this.value = value;
        this.factors = factors;
        this.flatBonuses = flatBonuses;
    }

    @Override
    public final String getShorthand() {
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
    public int getValue(SizeClass sizeOfShip) {
        return value * BattleConstants.shipSizeScaling.get(sizeOfShip);
    }

    @Override
    protected CombatTarget takeAction(SingleBattle currentBattle) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean isNoLongerActive() {
        // TODO Auto-generated method stub
        return false;
    }
}
