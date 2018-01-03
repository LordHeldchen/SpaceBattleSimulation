package main.java.battle;

import main.java.ships.stats.StatType;

public enum HullDamageLevel {
    DEFLECT, GLANCE, HIT, CRIT, NOT_DETERMINED;

    public StatType getDefenseStatAgainstDamageLevel() {
        switch (this) {
            case CRIT:
                return StatType.THRESHOLD_CRIT;
            case HIT:
                return StatType.THRESHOLD_HIT;
            case GLANCE:
            case DEFLECT:
                return StatType.THRESHOLD_GLANCE;
            case NOT_DETERMINED:
            default:
                return null;
        }
    }
}
