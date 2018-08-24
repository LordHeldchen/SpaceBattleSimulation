package de.rauwolf.gaming.battleships.logging.battleLoggers;

import de.rauwolf.gaming.battleships.battle.HullDamageLevel;

class ShipStats implements Comparable<ShipStats> {

    private int countArmorDeflects = 0;
    
    private int countEvades = 0;

    private double shieldDamageTaken;
    private int countShieldedShots = 0;
    
    private double glanceDamageTaken = 0;
    private int countGlancingShots = 0;
    
    private double hitDamageTaken = 0;
    private int countHittingShots = 0;
    
    private double critDamageTaken = 0;
    private int countCrittingShots = 0;

    private int timesExploded = 0;

    @Override
    public int compareTo(ShipStats other) {
        return (int) (other.getOverallDamageTaken() - this.getOverallDamageTaken());
    }
    
    private double getOverallDamageTaken() {
        return glanceDamageTaken + hitDamageTaken + critDamageTaken + shieldDamageTaken;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        final int hitsTakenThatCausedDamage = countShieldedShots + countGlancingShots + countHittingShots + countCrittingShots;
        final int hitsTaken = hitsTakenThatCausedDamage + countArmorDeflects + timesExploded + countEvades;
        double averageDamageTaken = getOverallDamageTaken() / hitsTakenThatCausedDamage;
        builder.append("  - armor deflects:            " + countArmorDeflects + " of " +  hitsTaken + " shots taken\n");
        builder.append("  - evades:                    " + countEvades + " of " +  hitsTaken + " shots taken\n");
        builder.append("  - exploded vs. hits taken:   " + timesExploded + " of " +  hitsTaken + " shots taken\n");
        builder.append("  - average damage taken/shot: " + String.format("%-8.2f", averageDamageTaken) + "\n");
        builder.append("    - shield damage taken:     " + String.format("%-8.2f", shieldDamageTaken) + "(" + countShieldedShots + " shots)" + "\n");
        builder.append("    - glancing damage taken:   " + String.format("%-8.2f", glanceDamageTaken) + "(" + countGlancingShots + " shots)" + "\n");
        builder.append("    - hitting damage taken:    " + String.format("%-8.2f", hitDamageTaken) + "(" + countHittingShots + " shots)" + "\n");
        builder.append("    - critting damage taken:   " + String.format("%-8.2f", critDamageTaken) + "(" + countCrittingShots + " shots)" + "\n");
        builder.append("  --> overall damage taken:    " + String.format("%-8.2f", getOverallDamageTaken()) + "\n");
        
        return builder.toString();
    }

    public void hasBeenEvaded() {
        countEvades++;
    }

    public void hasBeenDeflectedByArmor() {
        countArmorDeflects++;
    }

    public void hasTakenShieldDamage(double amount) {
        shieldDamageTaken += amount;
        countShieldedShots++;
    }

    public void hasTakenHullDamage(double damage, HullDamageLevel damageLevel) {
        switch (damageLevel) {
            case GLANCE:
                glanceDamageTaken += damage;
                countGlancingShots++;                
                break;
            case HIT:
                hitDamageTaken += damage;
                countHittingShots++;                
                break;
            case CRIT:
                critDamageTaken += damage;
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

    public void hasExploded() {
        timesExploded++;
    }
}
