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

public class EmptyBattleLogger extends BattleLogger {
    @Override
    public void showFormup(HashSet<InstantiatedFleet> allFleets, HashSet<ShipInstance> allShips, PriorityQueue<CombatActor> combatActors,
            Map<Integer, InstantiatedFleet> enemiesOfEmpireX, int numParticipatingFighters) {
    }

    @Override
    public void targetDestroyed(CombatTarget target) {
    }

    @Override
    public void startOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleStart) {
    }

    @Override
    public void endOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleEnd) {
    }

    @Override
    public void endOfSingleAttack(ShipInstance attacker) {
    }

    @Override
    public void beginSingleAttack(final CombatActor attacker, final CombatTarget target, int currentIni) {
    }

    @Override
    public void nextRound() {
    }

    @Override
    public void targetReacts(CombatTarget target) {
    }

    @Override
    public void isHit(CombatTarget ship, boolean isHit, int acc, int evade, int addedAccuracy, String weaponName) {
    }

    @Override
    public void armorDeflectsAllDamage(CombatTarget ship, int hitStrength, int glanceThreshold, String weaponName) {
    }

    @Override
    public void shieldDeflectsAllDamage(ShipInstance ship, String weaponName) {
    }

    @Override
    public void takesHullDamage(CombatTarget target, double damage, HullDamageLevel damageLevel, int hitStrength, int specificResistance, int threshold,
            String weaponName) {
    }

    @Override
    public void explodes(CombatTarget ship, double explodeChance, int hitStrength, double damageAmount, int critThreshold, int containment, String weaponName) {
    }

    @Override
    public void shieldBreaks(ShieldInstance shield, int breakDuration) {
    }

    @Override
    public void takesShieldDamage(ShieldInstance shield, double amount, String weaponName) {
    }

    @Override
    public void regeneratesShield(ShieldInstance shield, double regeneration, double shieldNow) {
    }

    @Override
    public void preysOnPreferredTargetType(CombatActor actor, int probability, int chance) {
    }

    @Override
    public void hasResupplied(ShipBlueprint ship, ShipBlueprint mothership) {
    }

    @Override
    public void noActiveParticipantsInCombat() {
    }

    @Override
    public void targetLosesTicks(CombatTarget shipInstance, int lostTicks, int chanceInPermill) {
    }

    @Override
    public void targetArmorDegrades(CombatTarget shipInstance, int amount) {
    }
}
