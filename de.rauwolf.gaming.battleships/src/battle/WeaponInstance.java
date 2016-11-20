package battle;

import java.util.ArrayList;
import java.util.Map;

import ships.Fleet;
import ships.Ship;
import ships.blueprints.Blueprint;
import ships.blueprints.WeaponBlueprint;

public class WeaponInstance implements CombatActor {
    private final Ship            ship;
    private final Blueprint       shipBlueprint;
    private final WeaponBlueprint weaponBlueprint;

    private int                   currentBattleSpeed;
    private int                   currentPayload;

    public WeaponInstance(WeaponBlueprint weapon, Ship ship) {
        this.weaponBlueprint = weapon;
        this.ship = ship;
        this.shipBlueprint = ship.getBlueprint();

        currentBattleSpeed = shipBlueprint.getStartBattleSpeed().getCalculatedValue()
                        + BattleConstants.randomizer.nextInt(BattleConstants.battleSpeedRandomizerMaximum);
        currentPayload = weapon.getMaxPayload();
    }

    @Override
    public boolean hasPayloadRemaining() {
        return currentPayload > 0;
    }

    @Override
    public Ship chooseTarget(final Fleet listOfPotentialTargets) {
        Map<Class<? extends Blueprint>, Integer> preferredTargets = weaponBlueprint.getPreferredTargets();
        for (Class<? extends Blueprint> preferredType : preferredTargets.keySet()) {
            if (listOfPotentialTargets.containsType(preferredType)
                            && BattleConstants.randomizer.nextInt() < preferredTargets.get(preferredType)) {
                final ArrayList<Ship> targets = listOfPotentialTargets.getAllOfType(preferredType);
                logger.preysOnPreferredTargetType(this);
                return targets.get((int) (targets.size() * Math.random()));
            }
        }
        return listOfPotentialTargets
                        .get((int) (listOfPotentialTargets.size() * BattleConstants.randomizer.nextFloat()));
    }

    @Override
    public final void takeTurn() {
        currentBattleSpeed -= weaponBlueprint.getBattleSpeedDecay();

        if (!hasPayloadRemaining() && ship.getMothership() != null && ship.getMothership().hasSupplyLeft()) {
            for (WeaponBlueprint weapon : currentPayload.keySet()) {
                currentPayload.put(weapon,
                                   mothership.getSupply(weapon.getMaxPayload()
                                                   - currentPayload.get(weapon).intValue()));
                logger.hasResupplied(this, mothership);
            }
        }

        if (blueprint.getMaxShieldStrength() > 0) {
            int regeneration = currentShield + blueprint.getShieldRegeneration() > blueprint.getMaxShieldStrength()
                            ? blueprint.getMaxShieldStrength() - currentShield : blueprint.getShieldRegeneration();
            currentShield += regeneration;
            logger.regeneratesShield(this, regeneration, currentShield);
        }
    }
}
