package de.rauwolf.gaming.battleships.logging.battleLoggers;

import de.rauwolf.gaming.battleships.battle.HullDamageLevel;

class WeaponStats implements Comparable<WeaponStats> {

    private int countArmorDeflects = 0;
    
    private int countEvades = 0;

    private double causedShieldDamage;
    private int countShieldedShots = 0;

    private double causedGlanceDamage = 0;
    private int countGlancingShots = 0;

    private double causedHitDamage = 0;
    private int countHittingShots = 0;

    private double causedCritDamage = 0;
    private int countCrittingShots = 0;
    private int countCausedExplosions = 0;

    @Override
    public int compareTo(WeaponStats other) {
        // Inverse ordering since PriorityQueues require natural ordering.
        // 'Natural' being in this case that having the most is best, i.e. makes
        // you first.
        return (int) (this.getOverallDamageCaused() - other.getOverallDamageCaused());
    }
    
    private double getOverallDamageCaused() {
        return causedGlanceDamage + causedHitDamage + causedCritDamage + causedShieldDamage;
    }
    
    public String toString() {
        StringBuilder builder = new StringBuilder();
        int shotsTakenThatCausedDamage = countShieldedShots + countGlancingShots + countHittingShots + countCrittingShots;
        int shotsTaken = shotsTakenThatCausedDamage + countArmorDeflects + countCausedExplosions + countEvades;
        double averageDamage = (causedGlanceDamage + causedHitDamage + causedCritDamage + causedShieldDamage) / shotsTakenThatCausedDamage;
        builder.append("  - armor deflects:             " + countArmorDeflects + " of " +  shotsTaken + " shots taken\n");
        builder.append("  - evades:                     " + countEvades + " of " +  shotsTaken + " shots taken\n");
        builder.append("  - caused explosions:          " + countCausedExplosions + " of " +  shotsTaken + " shots taken\n");
        builder.append("  - average damage per shot:    " + String.format("%-8.2f", averageDamage) + "\n");
        builder.append("    - caused shield damage:     " + String.format("%-8.2f", causedShieldDamage) + "(" + countShieldedShots + " shots)" + "\n");
        builder.append("    - caused glancing damage:   " + String.format("%-8.2f", causedGlanceDamage) + "(" + countGlancingShots + " shots)" + "\n");
        builder.append("    - caused hitting damage:    " + String.format("%-8.2f", causedHitDamage) + "(" + countHittingShots + " shots)" + "\n");
        builder.append("    - caused critting damage:   " + String.format("%-8.2f", causedCritDamage) + "(" + countCrittingShots + " shots)" + "\n");
        builder.append("  --> overall damage caused:    " + String.format("%-8.2f", getOverallDamageCaused()) + "\n");
        
        return builder.toString();
    }

    public void hasBeenEvaded() {
        countEvades++;
    }

    public void hasBeenDeflectedByArmor() {
        countArmorDeflects++;
    }

    public void hasCausedShieldDamage(double amount) {
        causedShieldDamage += amount;
        countShieldedShots++;
    }

    public void hasCausedHullDamage(double damage, HullDamageLevel damageLevel) {
        switch (damageLevel) {
            case GLANCE:
                causedGlanceDamage += damage;
                countGlancingShots++;                
                break;
            case HIT:
                causedHitDamage += damage;
                countHittingShots++;                
                break;
            case CRIT:
                causedCritDamage += damage;
                countCrittingShots++;
                break;
            case DEFLECT:
                break;
            case NOT_DETERMINED:
                break;
            default:
                break;
        }        
    }

    public void hasCausedExplosion() {
        countCausedExplosions++;
    }
}
