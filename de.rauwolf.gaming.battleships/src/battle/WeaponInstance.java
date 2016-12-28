package battle;

import java.util.ArrayList;
import java.util.Map;

import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.WeaponBlueprint;

public class WeaponInstance extends CombatActor {
    private final ShipInstance    owningShipInstance;
    private final WeaponBlueprint weaponBlueprint;

    //TODO: Not sure yet whether the concept of "payload" will remain.
    private int                   currentPayload;

    public WeaponInstance(WeaponBlueprint weapon, ShipInstance owningShipInstance) {
        super(owningShipInstance.getBlueprint().getStartBattleSpeed().getCalculatedValue()
                        + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum),
                        weapon.getBattleSpeedDecay());

        this.weaponBlueprint = weapon;
        this.owningShipInstance = owningShipInstance;

        this.currentPayload = weapon.getMaxPayload();
    }

    @Override
    public CombatTarget takeAction() {
        loseInitiative();

        final Fleet listOfPotentialTargets = currentBattle
                        .getAllEnemiesOfEmpireX(owningShipInstance.getIdOfOwningEmpire());
        CombatTarget target = chooseTarget(listOfPotentialTargets);

        logger.beginSingleAttack(this, target);

        if (target.reactBeforeAttacker(attacker)) {
            logger.shipReacts(target);
            if (checkDestructionOf(attacker)) {
                logger.shipDestroyed(attacker);
                return;
            }
        }

        target.takeDamage(attacker.getShots());
        checkDestructionOf(target);
        return target;
    }

    private ShipInstance chooseTarget(final Fleet listOfPotentialTargets) {
        Map<Class<? extends Blueprint>, Integer> preferredTargets = weaponBlueprint.getPreferredTargets();
        for (Class<? extends Blueprint> preferredType : preferredTargets.keySet()) {
            if (listOfPotentialTargets.containsType(preferredType)
                            && BattleConstants.randomizer.nextInt() < preferredTargets.get(preferredType)) {
                final ArrayList<ShipInstance> targets = listOfPotentialTargets.getAllOfType(preferredType);
                logger.preysOnPreferredTargetType(this);
                return targets.get((int) (targets.size() * Math.random()));
            }
        }
        return listOfPotentialTargets
                        .get((int) (listOfPotentialTargets.size() * BattleConstants.randomizer.nextFloat()));
    }

    private Shot getShot() {
        return new Shot(weaponBlueprint.getDamage(), weaponBlueprint.getArmorPenetration(),
                        weaponBlueprint.getAccuracy());
    }
}
