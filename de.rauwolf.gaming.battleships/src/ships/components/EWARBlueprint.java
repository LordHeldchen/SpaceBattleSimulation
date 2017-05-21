package ships.components;

import java.util.Map;

import battle.CombatActor;
import battle.CombatTarget;
import battle.SingleBattle;
import ships.shipHulls.ComponentType;
import ships.stats.StatType;

public class EWARBlueprint extends CombatActor implements Component {
    private final String                     name;
    private final ComponentType              type;
    private final int                        value;
    private final Map<StatType, Double>  factors;
    private final Map<StatType, Integer> flatBonuses;

    public EWARBlueprint(String name, ComponentType type, int value, Map<StatType, Double> factors, Map<StatType, Integer> flatBonuses,
            int startInitiative, int timeCost) {
        super(startInitiative, timeCost);
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
    protected CombatTarget takeAction(SingleBattle currentBattle) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String toString() {
        return name;
    }
}
