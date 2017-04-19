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
                           int numParticipatingFighters);

    public void shipDestroyed(CombatTarget targetOfAction);

    public void endOfBattle(HashSet<Fleet> allFleets);

    public void endOfSingleAttack(ShipInstance attacker);

    public void beginSingleAttack(final CombatActor weaponInstance, final CombatTarget target, int currentIni);

    public void nextRound();

    public void shipReacts(CombatTarget target);

    public void evades(ShipInstance ship, boolean doesEvade, int acc, int evade);

    public void armorDeflectsAllDamage(ShipInstance ship, int hitStrength, int glanceThreshold);

    public void takesHullDamage(ShipInstance ship, int damage, HullDamageType glancing, int hitStrength, int threshold);

    public void shieldBreaks(ShieldInstance shieldInstance);

    public void takesShieldDamage(ShieldInstance shieldInstance, int i);

    public void regeneratesShield(ShieldInstance ship, int regeneration, int shieldNow);

    public void preysOnPreferredTargetType(CombatActor ship);

    public void hasResupplied(ShipInstance ship, ShipInstance mothership);

	public void noActiveParticipantsInCombat();

    public void explodes(ShipInstance shipInstance, double explodeChance, int hitStrength, int calculatedValue, int containment);
}
