package battle;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ships.shipHulls.HullSize;

public class BattleConstants {
    public static final int                    cloakingRandomizerMaximum    = 101;

    public static final int                    penetrationRandomizerMaximum = 101;
    public static final float                  glanceMultiplier             = 0.35f;
    public static final float                  hitMultiplier                = 1.0f;
    public static final float                  critMultiplier               = 1.5f;
    public static final float                  maxChanceExplodeOnCrit       = 1.0f;

    public static final float                  minChanceToHit               = 0.05f;

    public static final int                    accuracyRandomizerMaximum    = 100;

    public static final int                    battleSpeedRandomizerMaximum = 200;
    public static final Random                 randomizer                   = new Random();
    
    public static final Map<HullSize, Integer> shipSizeScaling              = new HashMap<HullSize, Integer>();
    
    static {
        shipSizeScaling.put(HullSize.XS, 1);
        shipSizeScaling.put(HullSize.S, 4);
        shipSizeScaling.put(HullSize.M, 9);
        shipSizeScaling.put(HullSize.L, 16);
        shipSizeScaling.put(HullSize.XL, 25);
        shipSizeScaling.put(HullSize.XXL, 40);
    }
}
