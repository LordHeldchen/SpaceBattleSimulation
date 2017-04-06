package battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import logging.battleLogger.BattleLogger;
import logging.battleLogger.DebugBattleLogger;
import ships.Fleet;
import universe.StarSystem;

public class SingleBattle {
	boolean continueCombat = true;
	int shipsRanOutOfPayload = 0;

	Set<Integer> participatingEmpires = new HashSet<Integer>();

	Map<Integer, Fleet> enemiesOfEmpireX = new HashMap<Integer, Fleet>();
	HashSet<Fleet> allFleets = new HashSet<Fleet>();
	HashSet<ShipInstance> allShips = new HashSet<ShipInstance>();
	HashSet<ShipInstance> participatingFighters = new HashSet<ShipInstance>();

	private PriorityQueue<CombatActor> combatActors = new PriorityQueue<CombatActor>();
	private HashSet<CombatTarget> combatTargets = new HashSet<CombatTarget>();

	final BattleLogger logger;

	public SingleBattle(Set<Fleet> allFleetsFromStarSystem, BattleLogger logger) {
		this.logger = logger;

		allFleets.addAll(allFleetsFromStarSystem);
		for (Fleet fleet : allFleetsFromStarSystem) {
			for (ShipInstance ship : fleet) {
				participatingEmpires.add(ship.getIdOfOwningEmpire());
			}
		}

		for (int empire : participatingEmpires) {
			enemiesOfEmpireX.put(empire, new Fleet());
		}

		for (Fleet fleet : allFleets) {
			for (ShipInstance ship : fleet) {
				integrateShipIntoBattle(ship);
			}
		}

		logger.showFormup(allFleets, allShips, combatActors, enemiesOfEmpireX, participatingFighters);
	}

	private void integrateShipIntoBattle(ShipInstance ship) {
		combatTargets.add(ship);
		ship.addBattleLog(logger);

		List<CombatActor> combatActorsOfShip = ship.getCombatActorsOfShip();
		combatActorsOfShip.forEach(combatActor -> combatActor.setCurrentBattle(this));
		combatActorsOfShip.forEach(combatActor -> combatActor.addBattleLoger(logger));
		combatActors.addAll(combatActorsOfShip);

		int idOfOwningEmpire = ship.getIdOfOwningEmpire();
		for (int empire : participatingEmpires) {
			if (empire != idOfOwningEmpire) {
				enemiesOfEmpireX.get(empire).add(ship);
			}
		}

		allShips.add(ship);

		for (ShipInstance fighter : ship.getFightersInBay()) {
			integrateShipIntoBattle(fighter);
		}
	}

	public void fight() {
		final int highestStartingInitiative = combatActors.peek().getCurrentInitiative();
		while (continueCombat) {
			logger.nextRound();

			CombatActor actorWithHighestInit = combatActors.poll();
			CombatTarget targetOfAction = actorWithHighestInit.takeAction();
			combatActors.add(actorWithHighestInit);
			checkDestruction(targetOfAction);

			enemiesOfEmpireX.values().forEach((Fleet f) -> continueCombat &= f.size() > 0
					&& actorWithHighestInit.getCurrentInitiative() >= highestStartingInitiative - 500);
		}

		for (ShipInstance ship : allShips) {
			ship.endCurrentBattle();
		}
	}

	private boolean checkDestruction(CombatTarget targetOfAction) {
		if (targetOfAction.isDestroyed()) {
			logger.shipDestroyed(targetOfAction);
			for (Fleet ships : enemiesOfEmpireX.values()) {
				ships.remove(targetOfAction);
				if (ships.isEmpty()) {
					// Some empire has no more enemies in this battle
					continueCombat = false;
				}
			}
			allShips.remove(targetOfAction);
			for (Fleet fleet : allFleets) {
				fleet.remove(targetOfAction);
			}
			return true;
		}
		return false;
	}

	Fleet getAllEnemiesOfEmpireX(int empireID) {
		return enemiesOfEmpireX.get(empireID);
	}

	private void endBattle() {
		logger.endOfBattle(allFleets);
		for (ShipInstance ship : allShips) {
			ship.removeBattleLogger();
		}
	}

	public void addFleet(Fleet participatingFleet) {
		allFleets.add(participatingFleet);
	}

	private void checkSetup() {
		// TODO Auto-generated method stub
		// Should be used to check all constraints (e.g. that there actually are
		// at least two sides and so on).
	}

	public static void main(String[] args) {
		Set<Fleet> allFleetsFromStarSystem = StarSystem.getAllFleetsFromStarSystem();
		SingleBattle battle = new SingleBattle(allFleetsFromStarSystem, DebugBattleLogger.getNewDebugBattleLogger());

		battle.checkSetup();
		battle.fight();
		battle.endBattle();
	}
}
