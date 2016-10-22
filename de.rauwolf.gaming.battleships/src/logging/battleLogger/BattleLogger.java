package logging.battleLogger;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import ships.Fleet;
import ships.Ship;

public interface BattleLogger {
    public void showFormup(HashSet<Fleet> allFleets,
                           PriorityQueue<Ship> allShips,
                           Map<Integer, Fleet> enemiesOfEmpireX,
                           HashSet<Ship> participatingFighters);

    public void checkPayloadAfterAttack(Ship highestInit);

    public void shipDestroyed(Ship target);

    public void endOfBattle(HashSet<Fleet> allFleets);

    public void endOfSingleAttack(Ship attacker);

    public void beginSingleAttack(final Ship attacker, final Ship target);

    public void nextRound();

    public void shipReacts(Ship attacker);

    public void evades(Ship ship, boolean doesEvade);

    public void armorDeflectsAllDamage(Ship ship);

    public void takesArmorDamage(Ship ship, int damage);

    public void shieldBreaks(Ship ship);

    public void takesShieldDamage(Ship ship, int i);

    public void regeneratesShield(Ship ship, int regeneration, int shieldNow);

    public void preysOnPreferredTargetType(Ship ship);

    public void hasResupplied(Ship ship, Ship mothership);
}
