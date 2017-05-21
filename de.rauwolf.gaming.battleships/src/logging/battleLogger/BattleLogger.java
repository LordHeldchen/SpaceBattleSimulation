package logging.battleLogger;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import battle.CombatActor;
import battle.CombatTarget;
import battle.HullDamageLevel;
import battle.ShieldInstance;
import battle.ShipInstance;
import ships.InstantiatedFleet;

public abstract class BattleLogger {
    private static final BattleLogger logger = new DebugBattleLogger();
    public static final BattleLogger getInstance() {
        return logger;
    }
    
    public abstract void showFormup(HashSet<InstantiatedFleet> allFleets,
                           HashSet<ShipInstance> allShips,
                           PriorityQueue<CombatActor> combatActors,
                           Map<Integer, InstantiatedFleet> enemiesOfEmpireX,
                           int numParticipatingFighters);

    public abstract void shipDestroyed(CombatTarget targetOfAction);

    public abstract void endOfBattle(HashSet<InstantiatedFleet> allFleets);

    public abstract void endOfSingleAttack(ShipInstance attacker);

    public abstract void beginSingleAttack(final CombatActor weaponInstance, final CombatTarget target, int currentIni);

    public abstract void nextRound();

    public abstract void shipReacts(CombatTarget target);

    public abstract void isHit(ShipInstance ship, boolean doesEvade, int acc, int evade, int addedAccuracy);

    public abstract void armorDeflectsAllDamage(ShipInstance ship, int hitStrength, int glanceThreshold, String weaponName);

    public abstract void takesHullDamage(ShipInstance ship, double remainingDamage, HullDamageLevel glancing, int hitStrength, int specificResistance, int threshold, String weaponName);

    public abstract void shieldBreaks(ShieldInstance shieldInstance, int breakDuration);

    public abstract void takesShieldDamage(ShieldInstance shieldInstance, double amount, String weaponName);

    public abstract void regeneratesShield(ShieldInstance ship, double regeneration, double currentShield);

    public abstract void preysOnPreferredTargetType(CombatActor ship);

    public abstract void hasResupplied(ShipInstance ship, ShipInstance mothership);

	public abstract void noActiveParticipantsInCombat();

    public abstract void explodes(ShipInstance shipInstance, double explodeChance, int hitStrength, int calculatedValue, int containment, String weaponName);

    public abstract void shipLosesTicks(ShipInstance shipInstance, int lostTicks, int chanceInPermill);

    public abstract void shipArmorDegrades(ShipInstance shipInstance, int val);

    public abstract void shieldDeflectsAllDamage(ShipInstance ship, String weaponName);
}
