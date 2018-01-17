package main.java.battle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import main.java.ships.blueprints.SizeClass;
import main.java.ships.shipHulls.DamageType;
import main.java.ships.stats.StatType;

public class BattleConstants {
    public static final int                          cloakingRandomizerMaximum    = 100;

    public static final int                          penetrationRandomizerMaximum = 100;
    public static final double                       deflectMultiplier            = 0.0d;
    public static final double                       glanceMultiplier             = 0.35d;
    public static final double                       hitMultiplier                = 1.0d;
    public static final double                       critMultiplier               = 1.5d;
    public static final double                       maxChanceExplodeOnCrit       = 1.0d;

    public static final Map<HullDamageLevel, Double> damageLevelToFactorMap       = new HashMap<HullDamageLevel, Double>();

    static {
        damageLevelToFactorMap.put(HullDamageLevel.DEFLECT, deflectMultiplier);
        damageLevelToFactorMap.put(HullDamageLevel.GLANCE, glanceMultiplier);
        damageLevelToFactorMap.put(HullDamageLevel.HIT, hitMultiplier);
        damageLevelToFactorMap.put(HullDamageLevel.CRIT, critMultiplier);
    }

    public static final float                   minChanceToHit                  = 0.05f;

    public static final int                     accuracyRandomizer              = 100;
    public static final int                     preferredTargetChanceRandomizer = 100;

    public static final int                     battleSpeedRandomizer           = 50;
    public static final Random                  randomizer                      = new Random();

    public static final Map<SizeClass, Integer> shipSizeScaling                 = new HashMap<SizeClass, Integer>();

    static {
        shipSizeScaling.put(SizeClass.XXS, 1);
        shipSizeScaling.put(SizeClass.XS, 2);
        shipSizeScaling.put(SizeClass.S, 4);
        shipSizeScaling.put(SizeClass.M, 9);
        shipSizeScaling.put(SizeClass.L, 16);
        shipSizeScaling.put(SizeClass.XL, 30);
        shipSizeScaling.put(SizeClass.XXL, 100);
    }

    public static final Map<DamageType, StatType> damageTypeToResistanceType = new HashMap<DamageType, StatType>();

    static {
        damageTypeToResistanceType.put(DamageType.ANTIMATTER, StatType.RESISTANCE_ANTIMATTER);
        damageTypeToResistanceType.put(DamageType.EM, StatType.RESISTANCE_EM);
        damageTypeToResistanceType.put(DamageType.EXPLOSIVE, StatType.RESISTANCE_EXPLOSIVE);
        damageTypeToResistanceType.put(DamageType.HEAT, StatType.RESISTANCE_HEAT);
        damageTypeToResistanceType.put(DamageType.KINETIC, StatType.RESISTANCE_KINETIC);
        damageTypeToResistanceType.put(DamageType.PARTICLE, StatType.RESISTANCE_PARTICLE);
    }
}
