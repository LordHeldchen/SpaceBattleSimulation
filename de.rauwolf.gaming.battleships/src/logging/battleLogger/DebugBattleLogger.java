package logging.battleLogger;

import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

import battle.BattleConstants;
import battle.CombatActor;
import battle.CombatTarget;
import battle.HullDamageLevel;
import battle.ShieldInstance;
import battle.ShipInstance;
import ships.InstantiatedFleet;

public class DebugBattleLogger extends BattleLogger {
    private int round = 0;
    private int initAtBattleStart = 0;

    DebugBattleLogger() {}

    @Override
    public void showFormup(HashSet<InstantiatedFleet> allFleets,
                           HashSet<ShipInstance> allShips,
                           PriorityQueue<CombatActor> combatActors, 
                           Map<Integer, InstantiatedFleet> enemiesOfEmpireX,
                           int numParticipatingFighters) {
        System.out.println("Formup: ");
        for (InstantiatedFleet fleet : allFleets) {
            System.out.println(fleet);
        }

        System.out.println("=== Additional Info ===");
        for (int empireID : enemiesOfEmpireX.keySet()) {
            System.out.println("Enemies of empire " + empireID + ": " + enemiesOfEmpireX.get(empireID).size());
        }
        System.out.println();
        System.out.println("Participating Fighters from carriers: " + numParticipatingFighters);
        System.out.println("Amount of participating Ships: " + allShips.size());
        System.out.println("Amount of active actors: " + combatActors.size());
        System.out.println();
    }

    @Override
    public void shipDestroyed(CombatTarget target) {
        System.out.println("  Target destroyed: " + target);
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
        System.out.println("\nEnd of battle! Ships remaining: ");
        for (InstantiatedFleet fleet : allFleets) {
            System.out.println(fleet);
        }
        
        System.out.println("Ticks gone by: " + (initAtBattleStart - ticksAtBattleEnd) + "\n");
        
        for (String name: weaponEffectiveness.entrySet().stream().sorted(new WeaponStatsOrderer()).map(entry -> entry.getKey()).collect(Collectors.toList())) {
            System.out.println("Effectiveness of " + name + ":");
            System.out.println(weaponEffectiveness.get(name));
        }
    }

    @Override
    public void endOfSingleAttack(ShipInstance attacker) {}

    @Override
    public void beginSingleAttack(final CombatActor attacker, final CombatTarget target, int currentIni) {
        System.out.println("Round " + round + " (ini " + currentIni + "): " + attacker + " goes against " + target);
    }

    @Override
    public void nextRound() {
        round++;
    }

    @Override
    public void shipReacts(CombatTarget target) {
        System.out.println("  " + target + " reacts successfully!");
    }

    @Override
    public void isHit(ShipInstance ship, boolean isHit, int acc, int evade, int addedAccuracy, String weaponName) {
        if (isHit) {
            System.out.println("  " + ship + " is hit (" + acc + ">" + evade + " with bonus of " + addedAccuracy + ")");
        } else {
            System.out.println("  " + ship + " evades all damage (" + acc + "<=" + evade + " with bonus of " + addedAccuracy + ")");
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
        System.out.println("  Armor of " + ship + " deflects all Damage! (" + hitStrength + "<=" + glanceThreshold + ")");
        getWeaponStats(weaponName).hasBeenDeflectedByArmor();
    }

    @Override
    public void shieldDeflectsAllDamage(ShipInstance ship, String weaponName) {
        System.out.println("  Shield of " + ship + " deflects all Damage!");
    }

    @Override
    public void takesHullDamage(ShipInstance ship, double damage, HullDamageLevel damageLevel, int hitStrength, int specificResistance, int threshold, String weaponName) {
        System.out.println("  " + ship + " takes " + (int) damage + " points of " + damageLevel + " damage (" + hitStrength + ">" + (threshold + specificResistance) + ")");
        getWeaponStats(weaponName).hasCausedHullDamage(damage, damageLevel);
    }

    @Override
    public void explodes(ShipInstance ship, double explodeChance, int hitStrength, int critThreshold, int containment, String weaponName) {
        explodeChance = Math.min(BattleConstants.maxChanceExplodeOnCrit, explodeChance);
        System.out.println("  " + ship + " explodes!!! Chance was " + String.format("%1$.2f", explodeChance * 100) + "% (" + hitStrength + ">" + critThreshold + ", " + containment + ")");
        getWeaponStats(weaponName).hasCausedExplosion();
    }

    @Override
    public void shieldBreaks(ShieldInstance shield, int breakDuration) {
        System.out.println("  Shield of " + shield.getOwner() + " breaks for " + breakDuration + " ticks!");
    }

    @Override
    public void takesShieldDamage(ShieldInstance shield, double amount, String weaponName) {
        System.out.println("  Shield of " + shield.getOwner() + " deflects " + (int) amount + " points of damage");
        getWeaponStats(weaponName).hasCausedShieldDamage(amount);
    }

    @Override
    public void regeneratesShield(ShieldInstance shield, double regeneration, double shieldNow) {
        System.out.println("  Shields of " + shield.getOwner() + " regenerated by " + String.format("%1$.1f", regeneration) + " to " + String.format("%1$.1f", shieldNow));
    }

    @Override
    public void preysOnPreferredTargetType(CombatActor actor) {
        System.out.println(actor + " Preys on preferred Target type!");
    }

    @Override
    public void hasResupplied(ShipInstance ship, ShipInstance mothership) {
        System.out.println("  " + ship + ": Resupplied at " + mothership);
    }

	@Override
	public void noActiveParticipantsInCombat() {
		System.out.println("No participants take an active part in this battle.");
	}

    @Override
    public void shipLosesTicks(ShipInstance shipInstance, int lostTicks, int chanceInPermill) {
        System.out.println("  " + shipInstance + " loses " + lostTicks + " ticks due to effect (chance was " + String.format("%1$.2f", ((double) chanceInPermill) / 10) + "%)");
    }

    @Override
    public void shipArmorDegrades(ShipInstance shipInstance, int amount) {
        System.out.println("  " + shipInstance + " loses " + amount + " on all armor thresholds due to effect");
    }
}
