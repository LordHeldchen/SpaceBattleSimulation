package battle;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import logging.battleLogger.BattleLogger;
import ships.Fleet;
import ships.blueprints.NotEnoughtSlotsException;
import universe.StarSystem;

public class SingleBattle {
    boolean continueCombat = true;
    int shipsRanOutOfPayload = 0;

    Set<Integer> participatingEmpires = new HashSet<Integer>();

    Map<Integer, Fleet> enemiesOfEmpireX = new HashMap<Integer, Fleet>();
    HashSet<Fleet> allFleets = new HashSet<Fleet>();
    HashSet<ShipInstance> allShips = new HashSet<ShipInstance>();
    int numParticipatingFighters = 0;

    private PriorityQueue<CombatActor> combatActors = new PriorityQueue<CombatActor>();
    private HashSet<CombatTarget> combatTargets = new HashSet<CombatTarget>();

    private static final BattleLogger logger = BattleLogger.getInstance();

    public SingleBattle(Set<Fleet> allFleetsFromStarSystem) {
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

        logger.showFormup(allFleets, allShips, combatActors, enemiesOfEmpireX, numParticipatingFighters);
    }

    private void integrateShipIntoBattle(ShipInstance ship) {
        combatTargets.add(ship);

        List<CombatActor> combatActorsOfShip = ship.getCombatActors();
        combatActors.addAll(combatActorsOfShip);

        int idOfOwningEmpire = ship.getIdOfOwningEmpire();
        for (int empire : participatingEmpires) {
            if (empire != idOfOwningEmpire) {
                enemiesOfEmpireX.get(empire).add(ship);
            }
        }

        allShips.add(ship);

        for (ShipInstance fighter : ship.getFightersInBay()) {
            numParticipatingFighters++;
            integrateShipIntoBattle(fighter);
        }
    }

    public void fight() {
        if (combatActors.size() > 0) {
            
            final int highestStartingInitiative = combatActors.peek().getCurrentInitiative();
            while (continueCombat) {
                logger.nextRound();

                CombatActor actorWithHighestInit = combatActors.poll();
                
                if (actorWithHighestInit.hasRememberedLostTicks()) {
                    actorWithHighestInit.applyRememberedLostTicks();
                } else {
                    CombatTarget targetOfAction = actorWithHighestInit.takeAction(this);
                    if (targetOfAction != null && targetOfAction.isDestroyed()) {
                        handleDestructionOf(targetOfAction);
                    }

                    enemiesOfEmpireX.values().forEach(
                            (Fleet f) -> continueCombat &= f.size() > 0 && actorWithHighestInit.getCurrentInitiative() >= highestStartingInitiative - 500);
                }
                combatActors.add(actorWithHighestInit);
            }
        } else {
            logger.noActiveParticipantsInCombat();
        }

        for (ShipInstance ship : allShips) {
            ship.endCurrentBattle();
        }
    }

    private void handleDestructionOf(CombatTarget targetOfAction) {
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
            combatActors.removeAll(targetOfAction.getCombatActors());
        }
    }

    Fleet getAllEnemiesOfEmpireX(int empireID) {
        return enemiesOfEmpireX.get(empireID);
    }

    private void endBattle() {
        logger.endOfBattle(allFleets);
    }

    public void addFleet(Fleet participatingFleet) {
        allFleets.add(participatingFleet);
    }

    private void checkSetup() {
        // TODO Auto-generated method stub
        // Should be used to check all constraints (e.g. that there actually are
        // at least two sides and so on).
    }

    public static void main(String[] args) throws InstantiationException, NotEnoughtSlotsException, IOException {
        Set<Fleet> allFleetsFromStarSystem = StarSystem.getAllFleetsFromStarSystem();
        SingleBattle battle = new SingleBattle(allFleetsFromStarSystem);

        battle.checkSetup();
        battle.fight();
        battle.endBattle();
    }

    public BattleLogger getBattleLogger() {
        return logger;
    }
}
