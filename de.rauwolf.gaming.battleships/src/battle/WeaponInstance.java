package battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import logging.battleLogger.BattleLogger;
import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.MutableBaseStat;

public class WeaponInstance extends CombatActor {
	private final ShipInstance owningShipInstance;

	private final String name;
	private final MutableBaseStat accuracy;
	private final MutableBaseStat damage;
	private final MutableBaseStat armorPenetration;

	private final Map<Class<? extends Blueprint>, Integer> preferredTargets;

	// TODO: Not sure yet whether the concept of "payload" will remain. Probably replacable/augmentable by some "burst fire" mechanic?
	// private int currentPayload;

	// TODO: More encapsulation so that the MutableBaseStats are only visible during construction?
	public WeaponInstance(ShipInstance owningShipInstance, String name, int startInitiative, int timeCost, int damage, int accuracy, int armorPenetration) {
		super(startInitiative, timeCost);
		this.owningShipInstance = owningShipInstance;
        this.name = name;
		this.damage = new MutableBaseStat(damage);
		this.accuracy = new MutableBaseStat(accuracy);
		this.armorPenetration = new MutableBaseStat(armorPenetration);
		
		this.preferredTargets = new HashMap<Class<? extends Blueprint>, Integer>();
	}

	public void setLogger(BattleLogger battleLogger) {
		super.addBattleLoger(battleLogger);
	}

	@Override
	public CombatTarget takeAction() {
		int iniBeforeAction = loseInitiative();

		final Fleet listOfPotentialTargets = currentBattle
				.getAllEnemiesOfEmpireX(owningShipInstance.getIdOfOwningEmpire());
		CombatTarget target = chooseTarget(listOfPotentialTargets);

		logger.beginSingleAttack(this, target, iniBeforeAction);

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
		return new Shot(damage.getCalculatedValue(), armorPenetration.getCalculatedValue(), accuracy.getCalculatedValue());
	}
	
    @Override
    final public String toString() {
        return name + " of " + owningShipInstance;
    }
}
