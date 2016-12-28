package battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import logging.battleLogger.BattleLogger;
import logging.battleLogger.DebugBattleLogger;
import ships.Fleet;
import universe.StarSystem;

public class SingleBattle {
    boolean                            continueCombat        = true;
    int                                shipsRanOutOfPayload  = 0;
    final int                          initialAmountOfShips;

    Set<Integer>                       participatingEmpires  = new HashSet<Integer>();

    Map<Integer, Fleet>                enemiesOfEmpireX      = new HashMap<Integer, Fleet>();
    HashSet<Fleet>                     allFleets             = new HashSet<Fleet>();
    HashSet<ShipInstance>              allShips              = new HashSet<ShipInstance>();
    HashSet<ShipInstance>              participatingFighters = new HashSet<ShipInstance>();

    private PriorityQueue<CombatActor> combatActors          = new PriorityQueue<CombatActor>();
    private HashSet<CombatTarget>      combatTargets;

    final BattleLogger                 logger;

    public SingleBattle(Set<Fleet> allFleetsFromStarSystem, BattleLogger logger) {
        this.logger = logger;

        allFleets.addAll(allFleetsFromStarSystem);
        for (Fleet fleet : allFleetsFromStarSystem) {
            for (ShipInstance ship : fleet) {
                participatingEmpires.add(ship.getIdOfOwningEmpire());
                ship.setCurrentBattle(this);
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

        initialAmountOfShips = allShips.size();

        logger.showFormup(allFleets, allShips, enemiesOfEmpireX, participatingFighters);
    }

    private void integrateShipIntoBattle(ShipInstance ship) {
        combatTargets.add(ship);
        combatActors.addAll(ship.getCombatActorsOfShip());
        for (int empire : participatingEmpires) {
            if (empire != ship.getIdOfOwningEmpire()) {
                enemiesOfEmpireX.get(empire).add(ship);
            }
        }
        ship.addBattleLog(logger);
        allShips.add(ship);

        for (ShipInstance fighter : ship.getFightersInBay()) {
            integrateShipIntoBattle(fighter);
        }
    }

    public void fight() {
        while (continueCombat) {
            logger.nextRound();

            CombatActor actorWithHighestInit = combatActors.poll();
            actorWithHighestInit.takeAction();
            combatActors.add(actorWithHighestInit);

            if (hasPayloadRemaining) {
                attack(actorWithHighestInit);
            } else if (!actorWithHighestInit.hasPayloadRemaining()) {
                ++shipsRanOutOfPayload;
            }

            logger.checkPayloadAfterAttack(actorWithHighestInit);

            continueCombat &= shipsRanOutOfPayload < allShips.size() / 2;
        }

        for (ShipInstance ship : allShips) {
            ship.endCurrentBattle();
        }
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
        // Should be used to check all constraints (e.g. that there actually are at least two sides and so on).
    }

    public static void main(String[] args) {
        Set<Fleet> allFleetsFromStarSystem = StarSystem.getAllFleetsFromStarSystem();
        SingleBattle battle = new SingleBattle(allFleetsFromStarSystem, DebugBattleLogger.getNewDebugBattleLogger());

        battle.checkSetup();
        battle.fight();
        battle.endBattle();
    }
}
