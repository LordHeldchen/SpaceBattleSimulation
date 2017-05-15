package battle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ships.hulls.HullSize;
import ships.shipHulls.DamageType;
import ships.stats.HullStatType;

public class BattleConstants {
    public static final int                         cloakingRandomizerMaximum    = 100;

    public static final int                         penetrationRandomizerMaximum = 100;
    public static final double                      deflectMultiplier            = 0.0d;
    public static final double                      glanceMultiplier             = 0.35d;
    public static final double                      hitMultiplier                = 1.0d;
    public static final double                      critMultiplier               = 1.5d;
    public static final double                      maxChanceExplodeOnCrit       = 1.0d;

    public static final Map<HullDamageLevel, Double> damageLevelToFactorMap       = new HashMap<HullDamageLevel, Double>();

    static {
        damageLevelToFactorMap.put(HullDamageLevel.DEFLECT, deflectMultiplier);
        damageLevelToFactorMap.put(HullDamageLevel.GLANCE, glanceMultiplier);
        damageLevelToFactorMap.put(HullDamageLevel.HIT, hitMultiplier);
        damageLevelToFactorMap.put(HullDamageLevel.CRIT, critMultiplier);
    }

    public static final float                  minChanceToHit               = 0.05f;

    public static final int                    accuracyRandomizerMaximum    = 100;

    public static final int                    battleSpeedRandomizerMaximum = 100;
    public static final Random                 randomizer                   = new Random();

    public static final Map<HullSize, Integer> shipSizeScaling              = new HashMap<HullSize, Integer>();

    static {
        shipSizeScaling.put(HullSize.XXS, 1);
        shipSizeScaling.put(HullSize.XS, 2);
        shipSizeScaling.put(HullSize.S, 4);
        shipSizeScaling.put(HullSize.M, 9);
        shipSizeScaling.put(HullSize.L, 16);
        shipSizeScaling.put(HullSize.XL, 30);
        shipSizeScaling.put(HullSize.XXL, 100);
    }

    public static final Map<DamageType, HullStatType> damageTypeToResistanceType = new HashMap<DamageType, HullStatType>();

    static {
        damageTypeToResistanceType.put(DamageType.ANTIMATTER, HullStatType.RESISTANCE_ANTIMATTER);
        damageTypeToResistanceType.put(DamageType.EM, HullStatType.RESISTANCE_EM);
        damageTypeToResistanceType.put(DamageType.EXPLOSIVE, HullStatType.RESISTANCE_EXPLOSIVE);
        damageTypeToResistanceType.put(DamageType.HEAT, HullStatType.RESISTANCE_HEAT);
        damageTypeToResistanceType.put(DamageType.KINETIC, HullStatType.RESISTANCE_KINETIC);
        damageTypeToResistanceType.put(DamageType.PARTICLE, HullStatType.RESISTANCE_PARTICLE);
    }
}
