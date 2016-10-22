package battle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import logging.battleLogger.BattleLogger;
import logging.battleLogger.DebugBattleLogger;
import ships.Fleet;
import ships.Ship;
import universe.StarSystem;

public class SingleBattle {
    boolean             continueCombat        = true;
    int                 shipsRanOutOfPayload  = 0;
    final int           initialAmountOfShips;

    Set<Integer>        participatingEmpires  = new HashSet<Integer>();

    Map<Integer, Fleet> enemiesOfEmpireX      = new HashMap<Integer, Fleet>();
    HashSet<Fleet>      allFleets             = new HashSet<Fleet>();
    PriorityQueue<Ship> allShips              = new PriorityQueue<Ship>();
    HashSet<Ship>       participatingFighters = new HashSet<Ship>();

    final BattleLogger  logger;

    public SingleBattle(Set<Fleet> allFleetsFromStarSystem, BattleLogger logger) {
        this.logger = logger;

        allFleets.addAll(allFleetsFromStarSystem);
        for (Fleet fleet : allFleetsFromStarSystem) {
            for (Ship ship : fleet) {
                participatingEmpires.add(ship.getIdOfOwningEmpire());
            }
        }

        for (int empire : participatingEmpires) {
            enemiesOfEmpireX.put(empire, new Fleet());
        }

        for (Fleet fleet : allFleets) {
            for (Ship ship : fleet) {
                ship.prepareForBattle();
                ship.addBattleLog(logger);
                participatingFighters.addAll(ship.getFightersInBay());
                for (int empire : participatingEmpires) {
                    if (empire != ship.getIdOfOwningEmpire()) {
                        enemiesOfEmpireX.get(empire).add(ship);
                    }
                }
            }
            allShips.addAll(fleet);
        }

        for (Ship fighter : participatingFighters) {
            fighter.prepareForBattle();
            fighter.addBattleLog(logger);
            for (int empire : participatingEmpires) {
                if (empire != fighter.getIdOfOwningEmpire()) {
                    enemiesOfEmpireX.get(empire).add(fighter);
                }
            }
        }
        allShips.addAll(participatingFighters);

        initialAmountOfShips = allShips.size();

        logger.showFormup(allFleets, allShips, enemiesOfEmpireX, participatingFighters);
    }

    public void fight() {
        boolean hasPayloadRemaining;
        while (continueCombat) {
            logger.nextRound();

            Ship highestInit = allShips.poll();
            hasPayloadRemaining = highestInit.hasPayloadRemaining();
            highestInit.takeTurn();
            allShips.add(highestInit);

            if (hasPayloadRemaining) {
                attack(highestInit);
            } else if (!highestInit.hasPayloadRemaining()) {
                ++shipsRanOutOfPayload;
            }

            logger.checkPayloadAfterAttack(highestInit);

            continueCombat &= shipsRanOutOfPayload < allShips.size() / 2;
        }
    }

    private void attack(Ship attacker) {
        final Fleet listOfPotentialTargets = enemiesOfEmpireX.get(attacker.getIdOfOwningEmpire());
        Ship target = attacker.chooseTarget(listOfPotentialTargets);

        logger.beginSingleAttack(attacker, target);

        if (target.reactBeforeAttacker(attacker)) {
            logger.shipReacts(target);
            if (checkDestructionOf(attacker)) {
                logger.shipDestroyed(attacker);
                return;
            }
        }

        target.takeDamage(attacker.getShots());
        checkDestructionOf(target);

    }

    private boolean checkDestructionOf(Ship target) {
        if (target.isDestroyed()) {
            logger.shipDestroyed(target);
            for (Fleet ships : enemiesOfEmpireX.values()) {
                ships.remove(target);
                if (ships.isEmpty()) {
                    //Some empire has no more enemies in this battle
                    continueCombat = false;
                }
            }
            allShips.remove(target);
            for (Fleet fleet : allFleets) {
                fleet.remove(target);
            }
            return true;
        }
        return false;
    }

    private void endBattle() {
        logger.endOfBattle(allFleets);
        for (Ship ship : allShips) {
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
