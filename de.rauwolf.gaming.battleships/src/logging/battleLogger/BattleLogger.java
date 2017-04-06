package logging.battleLogger;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import battle.CombatActor;
import battle.CombatTarget;
import battle.ShieldInstance;
import battle.ShipInstance;
import ships.Fleet;

public interface BattleLogger {
    public void showFormup(HashSet<Fleet> allFleets,
                           HashSet<ShipInstance> allShips,
                           PriorityQueue<CombatActor> combatActors,
                           Map<Integer, Fleet> enemiesOfEmpireX,
                           HashSet<ShipInstance> participatingFighters);

    public void shipDestroyed(CombatTarget targetOfAction);

    public void endOfBattle(HashSet<Fleet> allFleets);

    public void endOfSingleAttack(ShipInstance attacker);

    public void beginSingleAttack(final CombatActor weaponInstance, final CombatTarget target);

    public void nextRound();

    public void shipReacts(CombatTarget target);

    public void evades(ShipInstance ship, boolean doesEvade);

    public void armorDeflectsAllDamage(ShipInstance ship);

    public void takesHullDamage(ShipInstance ship, int damage, HullDamageType glancing);

    public void shieldBreaks(ShieldInstance shieldInstance);

    public void takesShieldDamage(ShieldInstance shieldInstance, int i);

    public void regeneratesShield(ShieldInstance ship, int regeneration, int shieldNow);

    public void preysOnPreferredTargetType(CombatActor ship);

    public void hasResupplied(ShipInstance ship, ShipInstance mothership);
}
