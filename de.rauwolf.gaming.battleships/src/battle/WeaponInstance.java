package battle;

import java.util.ArrayList;
import java.util.Map;

import logging.battleLogger.BattleLogger;
import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.WeaponBlueprint;

public class WeaponInstance implements CombatActor {
    private final ShipInstance    ship;
    private final Blueprint       shipBlueprint;
    private final WeaponBlueprint weaponBlueprint;
    private final BattleLogger    logger;

    private int                   currentBattleSpeed;
    private int                   currentPayload;

    public WeaponInstance(WeaponBlueprint weapon, ShipInstance ship, BattleLogger logger) {
        this.weaponBlueprint = weapon;
        this.ship = ship;
        this.shipBlueprint = ship.getBlueprint();
        this.logger = logger;

        currentBattleSpeed = shipBlueprint.getStartBattleSpeed().getCalculatedValue()
                        + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);
        currentPayload = weapon.getMaxPayload();
    }

    @Override
    public final void takeAction() {}

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
