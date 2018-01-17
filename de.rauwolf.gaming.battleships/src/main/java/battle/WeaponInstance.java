package main.java.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import main.java.logging.battleLoggers.BattleLogger;
import main.java.ships.InstantiatedFleet;
import main.java.ships.blueprints.SizeClass;
import main.java.ships.shipHulls.DamageType;
import main.java.ships.stats.MutableStat;
import main.java.ships.weapons.WeaponSecondaryEffect;

public class WeaponInstance extends CombatActor {
    private final ShipInstance                              owningShipInstance;

    private final String                                    name;
    private final MutableStat                               accuracy;
    private final MutableStat                               damage;
    private final MutableStat                               armorPenetration;
    private final MutableStat                               preferredTargetProbability;

    private final DamageType                                damageType;
    private final Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects;

    private final List<SizeClass>                           preferredTargetSizes;

    private static final BattleLogger                       logger = BattleLogger.getInstance();

    // TODO: Introduce burst fire mechanic for certain weapons, e.g.
    // Flak-Cannons?

    public WeaponInstance(ShipInstance owningShipInstance, String name, int startingInitiative, int timeCost, int damage, int accuracy, int armorPenetration,
            DamageType damageType, List<SizeClass> preferredTargetSizes, int preferredTargetProbability,
            Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects) {
        super(startingInitiative, timeCost);
        this.owningShipInstance = owningShipInstance;
        this.name = name;
        this.damageType = damageType;
        this.secondaryEffects = secondaryEffects;
        this.preferredTargetSizes = preferredTargetSizes;
        this.preferredTargetProbability = new MutableStat(preferredTargetProbability);
        this.damage = new MutableStat(damage);
        this.accuracy = new MutableStat(accuracy);
        this.armorPenetration = new MutableStat(armorPenetration);
    }

    @Override
    public CombatTarget takeAction(SingleBattle currentBattle) {
        int iniBeforeAction = loseTicks();

        final InstantiatedFleet listOfPotentialTargets = currentBattle.getAllEnemiesOfEmpireX(owningShipInstance.getIdOfOwningEmpire());
        CombatTarget target = chooseTarget(listOfPotentialTargets);

        logger.beginSingleAttack(this, target, iniBeforeAction);

        if (target.reactBeforeAttacker(owningShipInstance)) {
            logger.shipReacts(target);
            if (owningShipInstance.isDestroyed()) {
                logger.shipDestroyed(owningShipInstance);
                return target;
            }
        }

        target.receiveAttack(getShot());

        return target;
    }

    private ShipInstance chooseTarget(final InstantiatedFleet listOfPotentialTargets) {
        final int probability = preferredTargetProbability.getCalculatedValue();
        final int chance = BattleConstants.randomizer.nextInt(BattleConstants.preferredTargetChanceRandomizer);
        if (probability > chance) {
            for (SizeClass preferredSize : preferredTargetSizes) {
                if (listOfPotentialTargets.containsSize(preferredSize)) {
                    final ArrayList<ShipInstance> targets = listOfPotentialTargets.getAllOfSize(preferredSize);
                    logger.preysOnPreferredTargetType(this, probability, chance);
                    return targets.get((int) (targets.size() * Math.random()));
                }
            }
        }
        return listOfPotentialTargets.get((int) (listOfPotentialTargets.size() * BattleConstants.randomizer.nextFloat()));
    }

    private Shot getShot() {
        return new Shot(damage.getCalculatedValue(), armorPenetration.getCalculatedValue(), accuracy.getCalculatedValue(), damageType, secondaryEffects, name);
    }

    @Override
    final public String toString() {
        return name + " of " + owningShipInstance + "(new ini: " + this.getCurrentInitiative() + ")";
    }

    public DamageType getDamageType() {
        return damageType;
    }
}
