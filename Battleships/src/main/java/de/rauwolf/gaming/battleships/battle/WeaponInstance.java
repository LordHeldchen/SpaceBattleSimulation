package de.rauwolf.gaming.battleships.battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.logging.battleLoggers.BattleLogger;
import de.rauwolf.gaming.battleships.ships.InstantiatedFleet;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.stats.MutableStat;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class WeaponInstance extends CombatActor {
    private final ShipInstance                              owningShipInstance;

    private final String                                    name;
    private final MutableStat                               accuracy;
    private final MutableStat                               damage;
    private final MutableStat                               armorPenetration;

    private final DamageType                                damageType;
    private final Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects;

    private final MutableStat                               preferredTargetProbability;
    private final List<SizeClass>                           preferredTargetSizes;

    private static final BattleLogger                       logger = BattleLogger.getInstance();

    // TODO: Introduce burst fire mechanic for certain weapons, e.g. Flak-Cannons?

    public WeaponInstance(ShipInstance owningShipInstance, //
            String name, //
            int startingInitiative, //
            int timeCost, //
            int damage, //
            int accuracy, //
            int armorPenetration, DamageType damageType, //
            List<SizeClass> preferredTargetSizes, //
            int preferredTargetProbability, //
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

    private void takeShot() {
        ShotBuffer.writeShot(damage.getCalculatedValue(), armorPenetration.getCalculatedValue(), accuracy.getCalculatedValue(), damageType, secondaryEffects,
                name);
    }

    @Override
    public CombatTarget takeAction(SingleBattle currentBattle) {
        int iniBeforeAction = loseTicks();

        final InstantiatedFleet listOfPotentialTargets = currentBattle.getAllEnemiesOfEmpireX(owningShipInstance.getIdOfOwningEmpire());
        CombatTarget target = chooseTarget(listOfPotentialTargets);

        logger.beginSingleAttack(this, target, iniBeforeAction);

        if (target.reactBeforeAttacker(owningShipInstance)) {
            logger.targetReacts(target);
            if (owningShipInstance.isDestroyed()) {
                logger.targetDestroyed(owningShipInstance);
                return target;
            }
        }

        takeShot();
        target.receiveAttack();

        return target;
    }

    @Override
    final public String toString() {
        return name + " of " + owningShipInstance + "(new ini: " + this.getCurrentInitiative() + ")";
    }

    @Override
    public boolean isNoLongerActive() {
        return owningShipInstance.isDestroyed();
    }
}
