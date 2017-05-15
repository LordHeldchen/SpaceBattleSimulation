package battle;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import logging.battleLogger.BattleLogger;
import ships.Fleet;
import ships.hulls.HullSize;
import ships.shipHulls.DamageType;
import ships.stats.MutableBaseStat;
import ships.weapons.WeaponSecondaryEffect;

public class WeaponInstance extends CombatActor {
    private final ShipInstance                              owningShipInstance;

    private final String                                    name;
    private final MutableBaseStat                           accuracy;
    private final MutableBaseStat                           damage;
    private final MutableBaseStat                           armorPenetration;

    private final DamageType                                damageType;
    private final Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects;

    private final List<HullSize>                            preferredTargetSizes;

    private static final BattleLogger                       logger = BattleLogger.getInstance();

    // TODO: Introduce burst fire mechanic for certain weapons, e.g. Flak-Cannons?
    
    // TODO: More encapsulation so that the MutableBaseStats are only visible during construction?
    public WeaponInstance(ShipInstance owningShipInstance, String name, int startInitiative, int timeCost, int damage, int accuracy, int armorPenetration,
            DamageType damageType, List<HullSize> preferredTargetSizes, Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects) {
        super(startInitiative, timeCost);
        this.owningShipInstance = owningShipInstance;
        this.name = name;
        this.damage = new MutableBaseStat(damage);
        this.accuracy = new MutableBaseStat(accuracy);
        this.armorPenetration = new MutableBaseStat(armorPenetration);
        this.damageType = damageType;
        this.secondaryEffects = secondaryEffects;
        this.preferredTargetSizes = preferredTargetSizes;
    }

    @Override
    public CombatTarget takeAction(SingleBattle currentBattle) {
        int iniBeforeAction = loseTicks();

        final Fleet listOfPotentialTargets = currentBattle.getAllEnemiesOfEmpireX(owningShipInstance.getIdOfOwningEmpire());
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

    private ShipInstance chooseTarget(final Fleet listOfPotentialTargets) {
        for (HullSize preferredSize : preferredTargetSizes) {
            if (listOfPotentialTargets.containsSize(preferredSize)) {
                final ArrayList<ShipInstance> targets = listOfPotentialTargets.getAllOfSize(preferredSize);
                logger.preysOnPreferredTargetType(this);
                return targets.get((int) (targets.size() * Math.random()));
            }
        }
        return listOfPotentialTargets.get((int) (listOfPotentialTargets.size() * BattleConstants.randomizer.nextFloat()));
    }

    private Shot getShot() {
        return new Shot(damage.getCalculatedValue(), armorPenetration.getCalculatedValue(), accuracy.getCalculatedValue(), damageType, secondaryEffects);
    }

    @Override
    final public String toString() {
        return name + " of " + owningShipInstance + "(new ini: " + this.getCurrentInitiative() + ")";
    }

    public DamageType getDamageType() {
        return damageType;
    }
}
