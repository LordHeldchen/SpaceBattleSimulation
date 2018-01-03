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
    private int round = 0;
    private int initAtBattleStart = 0;
    
    private StringBuilder log;
    private static final String lineSeparator = System.lineSeparator();

    DebugBattleLogger() {
        log = new StringBuilder();
    }
    
    private void append(String val) {
        log.append(val + lineSeparator);
    }
    
    private void newLine() {
        log.append(lineSeparator);
    }

    @Override
    public void showFormup(HashSet<InstantiatedFleet> allFleets,
                           HashSet<ShipInstance> allShips,
                           PriorityQueue<CombatActor> combatActors, 
                           Map<Integer, InstantiatedFleet> enemiesOfEmpireX,
                           int numParticipatingFighters) {
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
    
    private class WeaponStatsOrderer implements Comparator<Entry<String, WeaponStats>> {
        @Override
        public int compare(Entry<String, WeaponStats> o1, Entry<String, WeaponStats> o2) {
            return o2.getValue().compareTo(o1.getValue());
        }
    }

    @Override
    public void startOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleStart) {
        round = 0;
        initAtBattleStart = ticksAtBattleStart;
        weaponEffectiveness.clear();
    }

    @Override
    public void endOfBattle(HashSet<InstantiatedFleet> allFleets, int ticksAtBattleEnd) {
        append("\nEnd of battle! Ships remaining: ");
        for (InstantiatedFleet fleet : allFleets) {
            append(fleet.toString());
        }
        
        append("Ticks gone by: " + (initAtBattleStart - ticksAtBattleEnd) + "\n");
        
        for (String name: weaponEffectiveness.entrySet().stream().sorted(new WeaponStatsOrderer()).map(entry -> entry.getKey()).collect(Collectors.toList())) {
            append("Effectiveness of " + name + ":");
            append(weaponEffectiveness.get(name).toString());
        }

        System.out.println(log);
    }

    @Override
    public void endOfSingleAttack(ShipInstance attacker) {}

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
        }
    }
    
    private Map<String, WeaponStats> weaponEffectiveness = new HashMap<String, WeaponStats>();
    
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

    @Override
    public void armorDeflectsAllDamage(ShipInstance ship, int hitStrength, int glanceThreshold, String weaponName) {
        append("  Armor of " + ship + " deflects all Damage! (" + hitStrength + "<=" + glanceThreshold + ")");
        getWeaponStats(weaponName).hasBeenDeflectedByArmor();
    }

    @Override
    public void shieldDeflectsAllDamage(ShipInstance ship, String weaponName) {
        append("  Shield of " + ship + " deflects all Damage!");
    }

    @Override
    public void takesHullDamage(ShipInstance ship, double damage, HullDamageLevel damageLevel, int hitStrength, int specificResistance, int threshold, String weaponName) {
        append("  " + ship + " takes " + (int) damage + " points of " + damageLevel + " damage (" + hitStrength + ">" + (threshold + specificResistance) + ")");
        getWeaponStats(weaponName).hasCausedHullDamage(damage, damageLevel);
    }

    @Override
    public void explodes(ShipInstance ship, double explodeChance, int hitStrength, int critThreshold, int containment, String weaponName) {
        explodeChance = Math.min(BattleConstants.maxChanceExplodeOnCrit, explodeChance);
        append("  " + ship + " explodes!!! Chance was " + String.format("%1$.2f", explodeChance * 100) + "% (" + hitStrength + ">" + critThreshold + ", " + containment + ")");
        getWeaponStats(weaponName).hasCausedExplosion();
    }

    @Override
    public void shieldBreaks(ShieldInstance shield, int breakDuration) {
        append("  Shield of " + shield.getOwner() + " breaks for " + breakDuration + " ticks!");
    }

    @Override
    public void takesShieldDamage(ShieldInstance shield, double amount, String weaponName) {
        append("  Shield of " + shield.getOwner() + " deflects " + (int) amount + " points of damage");
        getWeaponStats(weaponName).hasCausedShieldDamage(amount);
    }

    @Override
    public void regeneratesShield(ShieldInstance shield, double regeneration, double shieldNow) {
        append("  Shields of " + shield.getOwner() + " regenerated by " + String.format("%1$.1f", regeneration) + " to " + String.format("%1$.1f", shieldNow));
    }

    @Override
    public void preysOnPreferredTargetType(CombatActor actor) {
        append(actor + " Preys on preferred Target type!");
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
        append("  " + shipInstance + " loses " + lostTicks + " ticks due to effect (chance was " + String.format("%1$.2f", ((double) chanceInPermill) / 10) + "%)");
    }

    @Override
    public void shipArmorDegrades(ShipInstance shipInstance, int amount) {
        append("  " + shipInstance + " loses " + amount + " on all armor thresholds due to effect");
    }
}
