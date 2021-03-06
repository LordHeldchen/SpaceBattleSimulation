package de.rauwolf.gaming.battleships.battle;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

import de.rauwolf.gaming.battleships.logging.battleLoggers.BattleLogger;
import de.rauwolf.gaming.battleships.ships.Fleet;
import de.rauwolf.gaming.battleships.ships.InstantiatedFleet;
import de.rauwolf.gaming.battleships.ships.blueprints.NotEnoughtSlotsException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;
import de.rauwolf.gaming.battleships.universe.StarSystem;

public class SingleBattle {
    boolean                            continueCombat           = true;
    int                                shipsRanOutOfPayload     = 0;

    Set<Integer>                       participatingEmpires     = new HashSet<Integer>();

    Map<Integer, InstantiatedFleet>    enemiesOfEmpireX         = new HashMap<Integer, InstantiatedFleet>();
    HashSet<InstantiatedFleet>         allFleets                = new HashSet<InstantiatedFleet>();
    HashSet<ShipInstance>              allShips                 = new HashSet<ShipInstance>();
    int                                numParticipatingFighters = 0;

    private PriorityQueue<CombatActor> combatActors;
    private HashSet<CombatTarget>      combatTargets            = new HashSet<CombatTarget>();

    private static final BattleLogger  logger                   = BattleLogger.getInstance();

    public SingleBattle(Set<Fleet> allFleetsFromStarSystem) {
        combatActors = new PriorityQueue<CombatActor>((o1, o2) -> o2.getCurrentInitiative() - o1.getCurrentInitiative());

        allFleets.addAll(allFleetsFromStarSystem.stream().map(fleet -> fleet.instantiate()).collect(Collectors.toSet()));

        for (InstantiatedFleet fleet : allFleets) {
            for (ShipInstance ship : fleet) {
                participatingEmpires.add(ship.getIdOfOwningEmpire());
            }
        }

        for (int empire : participatingEmpires) {
            enemiesOfEmpireX.put(empire, new InstantiatedFleet());
        }

        for (InstantiatedFleet fleet : allFleets) {
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
        allShips.stream().forEach(s -> s.startBattle());

        enemiesOfEmpireX.values().forEach((InstantiatedFleet f) -> continueCombat &= f.size() > 0);

        if (combatActors.size() > 0 && enemiesOfEmpireX.values().stream().anyMatch((InstantiatedFleet f) -> f.size() > 0)) {
            CombatActor currentActor = combatActors.poll();
            int initiativeOfCurrentRound = currentActor.getCurrentInitiative();
            final int endAtInitiative = initiativeOfCurrentRound - BattleConstants.DURATION_OF_BATTLE_IN_TICKS;

            logger.startOfBattle(allFleets, initiativeOfCurrentRound);
            while (continueCombat) {
                logger.nextRound();

                if (!currentActor.applyRememberedLostTicks()) {
                    CombatTarget targetOfAction = currentActor.takeAction(this);
                    if (targetOfAction != null && targetOfAction.isDestroyed()) {
                        continueCombat &= handleDestructionOf(targetOfAction);
                    }

                    enemiesOfEmpireX.values().forEach((InstantiatedFleet f) -> continueCombat &= f.size() > 0);
                }
                combatActors.add(currentActor);
                currentActor = combatActors.poll();
                while (currentActor.isNoLongerActive() && !combatActors.isEmpty()) {
                    currentActor = combatActors.poll();
                }
                continueCombat &= initiativeOfCurrentRound > endAtInitiative && !combatActors.isEmpty();
            }

        } else {
            logger.noActiveParticipantsInCombat();
        }

        for (ShipInstance ship : allShips) {
            ship.endBattle();
        }
    }

    private boolean handleDestructionOf(CombatTarget targetOfAction) {
        if (targetOfAction.isDestroyed()) {
            logger.targetDestroyed(targetOfAction);
            allShips.remove(targetOfAction);
            for (InstantiatedFleet fleet : allFleets) {
                fleet.remove(targetOfAction);
            }
            for (InstantiatedFleet ships : enemiesOfEmpireX.values()) {
                ships.remove(targetOfAction);
                if (ships.isEmpty()) {
                    // XXX Some empire has no more enemies in this battle, therefore is the victor?
                    return false;
                }
            }
        }
        return true;
    }

    InstantiatedFleet getAllEnemiesOfEmpireX(int empireID) {
        return enemiesOfEmpireX.get(empireID);
    }

    public void endBattle() {
        if (combatActors.size() > 0) {
            logger.endOfBattle(allFleets, combatActors.peek().getCurrentInitiative());
        }
    }

    public void addFleet(InstantiatedFleet participatingFleet) {
        allFleets.add(participatingFleet);
    }

    public void checkSetup() {
        // TODO Auto-generated method stub
        // Should be used to check all constraints (e.g. that there actually are
        // at least two sides and so on).
    }

    public BattleLogger getBattleLogger() {
        return logger;
    }

    // Test Main
    public static void main(String[] args)
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

        Set<Fleet> allFleetsFromStarSystem = StarSystem.getAllFleetsFromStarSystem();
        SingleBattle battle = new SingleBattle(allFleetsFromStarSystem);

        battle.checkSetup();
        battle.fight();
        battle.endBattle();
    }
}
