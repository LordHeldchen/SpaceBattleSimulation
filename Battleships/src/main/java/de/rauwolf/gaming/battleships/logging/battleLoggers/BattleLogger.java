package de.rauwolf.gaming.battleships.logging.battleLoggers;

import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;

import de.rauwolf.gaming.battleships.battle.CombatActor;
import de.rauwolf.gaming.battleships.battle.CombatTarget;
import de.rauwolf.gaming.battleships.battle.HullDamageLevel;
import de.rauwolf.gaming.battleships.battle.ShieldInstance;
import de.rauwolf.gaming.battleships.battle.ShipInstance;
import de.rauwolf.gaming.battleships.ships.InstantiatedFleet;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;

public abstract class BattleLogger {
    private static final BattleLogger logger = new DebugBattleLogger();

    public static final BattleLogger getInstance() {
        return logger;
    }

    // Start/End of Battle
    public abstract void startOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleStart);

    public abstract void noActiveParticipantsInCombat();

    public abstract void showFormup(HashSet<InstantiatedFleet> allFleets, HashSet<ShipInstance> allShips, PriorityQueue<CombatActor> combatActors,
            Map<Integer, InstantiatedFleet> enemiesOfEmpireX, int numParticipatingFighters);

    public abstract void endOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleEnd);

    // Next Round
    public abstract void nextRound();

    public abstract void regeneratesShield(ShieldInstance target, double regeneration, double currentShield);

    public abstract void hasResupplied(ShipBlueprint target, ShipBlueprint mothertarget);

    // Actions and Reactions
    public abstract void beginSingleAttack(final CombatActor weaponInstance, final CombatTarget target, int currentIni);

    public abstract void preysOnPreferredTargetType(CombatActor target, int probability, int chance);

    public abstract void targetReacts(CombatTarget target);

    public abstract void isHit(CombatTarget target, boolean doesEvade, int acc, int evade, int addedAccuracy, String weaponName);

    public abstract void endOfSingleAttack(ShipInstance attacker);

    // Take hit on Shield
    public abstract void takesShieldDamage(ShieldInstance shieldInstance, double amount, String weaponName);

    public abstract void shieldDeflectsAllDamage(ShipInstance target, String weaponName);

    public abstract void shieldBreaks(ShieldInstance shieldInstance, int breakDuration);

    // Take hit on Hull
    public abstract void armorDeflectsAllDamage(CombatTarget target, int hitStrength, int glanceThreshold, String weaponName);

    public abstract void takesHullDamage(CombatTarget target, double remainingDamage, HullDamageLevel glancing, int hitStrength, int specificResistance,
            int threshold, String weaponName);

    // Destruction
    public abstract void explodes(CombatTarget targetInstance, double explodeChance, int hitStrength, double damageAmount, int calculatedValue, int containment,
            String weaponName);

    public abstract void targetDestroyed(CombatTarget targetOfAction);

    // Effects
    public abstract void targetLosesTicks(CombatTarget targetInstance, int lostTicks, int chanceInPermill);

    public abstract void targetArmorDegrades(CombatTarget targetInstance, int val);
}
