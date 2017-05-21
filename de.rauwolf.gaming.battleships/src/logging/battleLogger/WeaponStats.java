package logging.battleLogger;

import battle.HullDamageLevel;

class WeaponStats {    
    private int countArmorDeflects = 0;

    private double causedShieldDamage;
    private int countShieldedShots = 0;
    
    private double causedGlanceDamage = 0;
    private int countGlancingShots = 0;
    
    private double causedHitDamage = 0;
    private int countHittingShots = 0;
    
    private double causedCritDamage = 0;
    private int countCrittingShots = 0;
    private int countCausedExplosions = 0;
    
    public String toString() {
        //TODO: Average damage per tick (redundant I guess?), evades
        StringBuilder builder = new StringBuilder();
        int shotsTaken = countArmorDeflects + countShieldedShots + countGlancingShots + countHittingShots + countCrittingShots + countCausedExplosions;
        double averageDamage = (causedGlanceDamage + causedHitDamage + causedCritDamage + causedShieldDamage) / shotsTaken;
        builder.append("  - armor deflects:           " + countArmorDeflects + " of " +  shotsTaken + " shots taken\n");
        builder.append("  - average damage:           " + String.format("%-8.2f", averageDamage) + "\n");
        builder.append("    - caused shield damage:   " + String.format("%-8.2f", causedShieldDamage) + "(" + countShieldedShots + " shots)" + "\n");
        builder.append("    - caused glancing damage: " + String.format("%-8.2f", causedGlanceDamage) + "(" + countGlancingShots + " shots)" + "\n");
        builder.append("    - caused hitting damage:  " + String.format("%-8.2f", causedHitDamage) + "(" + countHittingShots + " shots)" + "\n");
        builder.append("    - caused critting damage: " + String.format("%-8.2f", causedCritDamage) + "(" + countCrittingShots + " shots)" + "\n");
        builder.append("  - caused explosions:        " + countCausedExplosions);
        
        return builder.toString();
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
