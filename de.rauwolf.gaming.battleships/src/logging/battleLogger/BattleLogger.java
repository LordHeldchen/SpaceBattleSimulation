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

    public void evades(ShipInstance ship, boolean doesEvade, int acc, int evade, int addedAccuracy);

    public void armorDeflectsAllDamage(ShipInstance ship, int hitStrength, int glanceThreshold);

    public void takesHullDamage(ShipInstance ship, double remainingDamage, HullDamageLevel glancing, int hitStrength, int threshold);

    public void shieldBreaks(ShieldInstance shieldInstance, int breakDuration);

    public void takesShieldDamage(ShieldInstance shieldInstance, double d);

    public void regeneratesShield(ShieldInstance ship, double regeneration, double currentShield);

    public void preysOnPreferredTargetType(CombatActor ship);

    public void hasResupplied(ShipInstance ship, ShipInstance mothership);

	public void noActiveParticipantsInCombat();

    public void explodes(ShipInstance shipInstance, double explodeChance, int hitStrength, int calculatedValue, int containment);

    public void shipLosesTicks(ShipInstance shipInstance, int lostTicks, int chanceInPermill);

    public void shipArmorDegrades(ShipInstance shipInstance, int val);
}
