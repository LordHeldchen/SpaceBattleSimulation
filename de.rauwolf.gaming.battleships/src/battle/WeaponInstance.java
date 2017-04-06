package battle;

import java.util.ArrayList;
import java.util.Map;

import logging.battleLogger.BattleLogger;
import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.WeaponBlueprint;

public class WeaponInstance extends CombatActor {
    private ShipInstance    owningShipInstance;
    private WeaponBlueprint weaponBlueprint;

    //TODO: Not sure yet whether the concept of "payload" will remain.
    //private int                   currentPayload;

    public WeaponInstance(WeaponBlueprint weapon, int startInitiative, int initiativeDecay) {
        super(startInitiative, initiativeDecay);

        this.weaponBlueprint = weapon;
    }
    
    public void setOwningShipInstace(ShipInstance instance) {
    	this.owningShipInstance = instance;
    }
    
    public void setLogger(BattleLogger battleLogger) {
    	super.addBattleLoger(battleLogger);
    }

    @Override
    public CombatTarget takeAction() {
        loseInitiative();

        final Fleet listOfPotentialTargets = currentBattle
                        .getAllEnemiesOfEmpireX(owningShipInstance.getIdOfOwningEmpire());
        CombatTarget target = chooseTarget(listOfPotentialTargets);

        logger.beginSingleAttack(this, target);

        if (target.reactBeforeAttacker(owningShipInstance)) {
            logger.shipReacts(target);
            if (owningShipInstance.isDestroyed()) {
                logger.shipDestroyed(owningShipInstance);
                return target;
            }
        }

        target.takeDamage(getShot());
        
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
