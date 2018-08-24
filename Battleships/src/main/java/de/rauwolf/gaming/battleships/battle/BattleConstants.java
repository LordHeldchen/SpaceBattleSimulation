package de.rauwolf.gaming.battleships.battle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

public class BattleConstants {
    public static final int                          CLOAKING_RANDOMIZER_MAXIMUM    = 100;

    public static final int                          PENETRATION_RANDOMIZER_MAXIMUM = 100;
    public static final double                       DEFLECT_MULTIPLIER             = 0.0d;
    public static final double                       GLANCE_MULTIPLIER              = 0.35d;
    public static final double                       HIT_MULTIPLIER                 = 1.0d;
    public static final double                       CRIT_MULTIPLIER                = 1.5d;
    public static final double                       MAX_CHANCE_EXPLODE_ON_CRIT     = 1.0d;
    public static final int                          DURATION_OF_BATTLE_IN_TICKS    = 500;
    public static final int                          MAX_INITIATIVE                 = 300;

    public static final Map<HullDamageLevel, Double> DAMAGE_LEVEL_TO_FACTOR_MAP     = new HashMap<HullDamageLevel, Double>();

    static {
        DAMAGE_LEVEL_TO_FACTOR_MAP.put(HullDamageLevel.DEFLECT, DEFLECT_MULTIPLIER);
        DAMAGE_LEVEL_TO_FACTOR_MAP.put(HullDamageLevel.GLANCE, GLANCE_MULTIPLIER);
        DAMAGE_LEVEL_TO_FACTOR_MAP.put(HullDamageLevel.HIT, HIT_MULTIPLIER);
        DAMAGE_LEVEL_TO_FACTOR_MAP.put(HullDamageLevel.CRIT, CRIT_MULTIPLIER);
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
