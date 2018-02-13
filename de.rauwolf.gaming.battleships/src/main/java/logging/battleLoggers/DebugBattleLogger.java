package main.java.logging.battleLoggers;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import main.java.battle.BattleConstants;
import main.java.battle.CombatActor;
import main.java.battle.CombatTarget;
import main.java.battle.HullDamageLevel;
import main.java.battle.ShieldInstance;
import main.java.battle.ShipInstance;
import main.java.ships.InstantiatedFleet;

public class DebugBattleLogger extends BattleLogger {
    private int                 round             = 0;
    private int                 initAtBattleStart = 0;

    private StringBuilder       log;
    private static final String lineSeparator     = System.lineSeparator();

    DebugBattleLogger() {
        log = new StringBuilder();
    }

    private void append(String val) {
        log.append(val + lineSeparator);
    }

    private void newLine() {
        log.append(lineSeparator);
    }

    private Map<String, WeaponStats> weaponEffectiveness = new HashMap<String, WeaponStats>();
    private Map<String, ShipStats> shipEffectiveness = new HashMap<String, ShipStats>();

    private class WeaponStatsOrderer implements Comparator<Entry<String, WeaponStats>> {
        @Override
        public int compare(Entry<String, WeaponStats> o1, Entry<String, WeaponStats> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    private class ShipStatsOrderer implements Comparator<Entry<String, ShipStats>> {
        @Override
        public int compare(Entry<String, ShipStats> o1, Entry<String, ShipStats> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    private WeaponStats getWeaponStats(String weaponName) {
        WeaponStats stats;
        if (weaponEffectiveness.containsKey(weaponName)) {
            stats = weaponEffectiveness.get(weaponName);
        } else {
            stats = new WeaponStats();
            weaponEffectiveness.put(weaponName, stats);
        }
        return stats;
    }
    
    private ShipStats getShipStats(String shipName) {
        ShipStats stats;
        if (shipEffectiveness.containsKey(shipName)) {
            stats = shipEffectiveness.get(shipName);
        } else {
            stats = new ShipStats();
            shipEffectiveness.put(shipName, stats);
        }
        return stats;
    }

    @Override
    public void showFormup(HashSet<InstantiatedFleet> allFleets, HashSet<ShipInstance> allShips, PriorityQueue<CombatActor> combatActors,
            Map<Integer, InstantiatedFleet> enemiesOfEmpireX, int numParticipatingFighters) {
        append("Formup: ");
        for (InstantiatedFleet fleet : allFleets) {
            append(fleet.toString());
        }

        append("=== Additional Info ===");
        for (int empireID : enemiesOfEmpireX.keySet()) {
            append("Enemies of empire " + empireID + ": " + enemiesOfEmpireX.get(empireID).size());
        }
        newLine();
        append("Participating Fighters from carriers: " + numParticipatingFighters);
        append("Amount of participating Ships: " + allShips.size());
        append("Amount of active actors: " + combatActors.size());
        newLine();
    }

    @Override
    public void shipDestroyed(CombatTarget target) {
        append("  Target destroyed: " + target);
    }

    @Override
    public void startOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleStart) {
        round = 0;
        initAtBattleStart = ticksAtBattleStart;
        weaponEffectiveness.clear();
    }

    @Override
    public void endOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleEnd) {
        newLine();
        append("End of battle!");
        append("Ticks gone by: " + (initAtBattleStart - ticksAtBattleEnd) + "\n");

        for (String name : weaponEffectiveness.entrySet().stream().sorted(new WeaponStatsOrderer()).map(entry -> entry.getKey()).collect(Collectors.toList())) {
            append("Effectiveness of " + name + ":");
            append(weaponEffectiveness.get(name).toString());
        }

        for (String name : shipEffectiveness.entrySet().stream().sorted(new ShipStatsOrderer()).map(entry -> entry.getKey()).collect(Collectors.toList())) {
            append("Survivability of " + name + ":");
            append(shipEffectiveness.get(name).toString());
        }

        append("\nShips remaining: ");
        for (InstantiatedFleet fleet : allFleets) {
            append(fleet.toString());
        }

        System.out.println(log);
    }

    @Override
    public void endOfSingleAttack(ShipInstance attacker) {
    }

    @Override
    public void beginSingleAttack(final CombatActor attacker, final CombatTarget target, int currentIni) {
        append("Round " + round + " (ini " + currentIni + "): " + attacker + " goes against " + target);
    }

    @Override
    public void nextRound() {
        round++;
    }

    @Override
    public void shipReacts(CombatTarget target) {
        append("  " + target + " reacts successfully!");
    }

    @Override
    public void isHit(ShipInstance ship, boolean isHit, int acc, int evade, int addedAccuracy, String weaponName) {
        if (isHit) {
            append("  " + ship + " is hit (" + acc + ">" + evade + " with bonus of " + addedAccuracy + ")");
        } else {
            append("  " + ship + " evades all damage (" + acc + "<=" + evade + " with bonus of " + addedAccuracy + ")");
            getWeaponStats(weaponName).hasBeenEvaded();
            getShipStats(ship.getName()).hasBeenEvaded();
        }
    }

    @Override
    public void armorDeflectsAllDamage(ShipInstance ship, int hitStrength, int glanceThreshold, String weaponName) {
        append("  Armor of " + ship + " deflects all Damage! (" + hitStrength + "<=" + glanceThreshold + ")");
        getWeaponStats(weaponName).hasBeenDeflectedByArmor();
        getShipStats(ship.getName()).hasBeenDeflectedByArmor();
    }

    @Override
    public void shieldDeflectsAllDamage(ShipInstance ship, String weaponName) {
        append("  Shield of " + ship + " deflects all Damage!");
    }

    @Override
    public void takesHullDamage(ShipInstance target, double damage, HullDamageLevel damageLevel, int hitStrength, int specificResistance, int threshold,
            String weaponName) {
        append("  " + target + " takes " + (int) damage + " points of " + damageLevel + " damage (" + hitStrength + ">" + (threshold + specificResistance) + ")");
        getWeaponStats(weaponName).hasCausedHullDamage(damage, damageLevel);
        getShipStats(target.getName()).hasTakenHullDamage(damage, damageLevel);
    }

    @Override
    public void explodes(ShipInstance ship, double explodeChance, int hitStrength, double damageAmount, int critThreshold, int containment, String weaponName) {
        explodeChance = Math.min(BattleConstants.MAX_CHANCE_EXPLODE_ON_CRIT, explodeChance);
        append("  " + ship + " explodes!!! Chance was " + String.format("%1$.2f", explodeChance * 100) + "% (" + hitStrength + ">" + critThreshold
                + ", containment " + containment + " fails against " + damageAmount + "dmg at " + String.format("%1$.2f", ship.getCurrentHullStrength()) + " remaining hullStrength)");
        getWeaponStats(weaponName).hasCausedExplosion();
        getShipStats(ship.getName()).hasExploded();
    }

    @Override
    public void shieldBreaks(ShieldInstance shield, int breakDuration) {
        append("  Shield of " + shield.getOwner() + " breaks for " + breakDuration + " ticks!");
    }

    @Override
    public void takesShieldDamage(ShieldInstance shield, double amount, String weaponName) {
        append("  Shield of " + shield.getOwner() + " deflects " + (int) amount + " points of damage");
        getWeaponStats(weaponName).hasCausedShieldDamage(amount);
        getShipStats(shield.getOwner().getName()).hasTakenShieldDamage(amount);
    }

    @Override
    public void regeneratesShield(ShieldInstance shield, double regeneration, double shieldNow) {
        append("  Shields of " + shield.getOwner() + " regenerated by " + String.format("%1$.1f", regeneration) + " to " + String.format("%1$.1f", shieldNow));
    }

    @Override
    public void preysOnPreferredTargetType(CombatActor actor, int probability, int chance) {
        append(actor + " Preys on preferred Target type! (" + probability + " > " + chance + ")");
    }

    @Override
    public void hasResupplied(ShipInstance ship, ShipInstance mothership) {
        append("  " + ship + ": Resupplied at " + mothership);
    }

    @Override
    public void noActiveParticipantsInCombat() {
        append("No participants take an active part in this battle.");
    }

    @Override
    public void shipLosesTicks(ShipInstance shipInstance, int lostTicks, int chanceInPermill) {
        append("  " + shipInstance + " loses " + lostTicks + " ticks due to effect (chance was " + String.format("%1$.2f", ((double) chanceInPermill) / 10)
                + "%)");
    }

    @Override
    public void shipArmorDegrades(ShipInstance shipInstance, int amount) {
        append("  " + shipInstance + " loses " + amount + " on all armor thresholds due to effect");
    }
}
