package main.java.logging.battleLoggers;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import main.java.battle.CombatActor;
import main.java.battle.CombatTarget;
import main.java.battle.HullDamageLevel;
import main.java.battle.ShieldInstance;
import main.java.battle.ShipInstance;
import main.java.ships.InstantiatedFleet;

public abstract class BattleLogger {
    private static final BattleLogger logger = new DebugBattleLogger();
    public static final BattleLogger getInstance() {
        return logger;
    }
    
    // Start/End of Battle
    public abstract void startOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleStart);
    public abstract void noActiveParticipantsInCombat();
    public abstract void showFormup(HashSet<InstantiatedFleet> allFleets,
                           HashSet<ShipInstance> allShips,
                           PriorityQueue<CombatActor> combatActors,
                           Map<Integer, InstantiatedFleet> enemiesOfEmpireX,
                           int numParticipatingFighters);
    public abstract void endOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleEnd);

    // Next Round
    public abstract void nextRound();
    public abstract void regeneratesShield(ShieldInstance ship, double regeneration, double currentShield);
    public abstract void hasResupplied(ShipInstance ship, ShipInstance mothership);

    // Actions and Reactions
    public abstract void beginSingleAttack(final CombatActor weaponInstance, final CombatTarget target, int currentIni);
    public abstract void preysOnPreferredTargetType(CombatActor ship);
    public abstract void shipReacts(CombatTarget target);
    public abstract void isHit(ShipInstance ship, boolean doesEvade, int acc, int evade, int addedAccuracy, String weaponName);
    public abstract void endOfSingleAttack(ShipInstance attacker);

    // Take hit on Shield
    public abstract void takesShieldDamage(ShieldInstance shieldInstance, double amount, String weaponName);
    public abstract void shieldDeflectsAllDamage(ShipInstance ship, String weaponName);
    public abstract void shieldBreaks(ShieldInstance shieldInstance, int breakDuration);
    
    // Take hit on Hull
    public abstract void armorDeflectsAllDamage(ShipInstance ship, int hitStrength, int glanceThreshold, String weaponName);
    public abstract void takesHullDamage(ShipInstance ship, double remainingDamage, HullDamageLevel glancing, int hitStrength, int specificResistance, int threshold, String weaponName);
    
    // Destruction
    public abstract void explodes(ShipInstance shipInstance, double explodeChance, int hitStrength, int calculatedValue, int containment, String weaponName);
    public abstract void shipDestroyed(CombatTarget targetOfAction);

    // Effects
    public abstract void shipLosesTicks(ShipInstance shipInstance, int lostTicks, int chanceInPermill);
    public abstract void shipArmorDegrades(ShipInstance shipInstance, int val);
}
