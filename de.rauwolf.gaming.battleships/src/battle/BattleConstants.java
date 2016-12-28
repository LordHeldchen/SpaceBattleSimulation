package battle;

import java.util.Random;

public class BattleConstants {

    public static final int    evasionRandomizerMaximum     = 100;

    public static final int    penetrationRandomizerMaximum = 100;

    public static final float  glanceMultiplier             = 0.35f;
    public static final float  hitMultiplier                = 1.0f;
    public static final float  critMultiplier               = 1.5f;

    public static final int    cloakingRandomizerMaximum    = 100;
    public static final int    evasionEqualizer             = 60;

    public static final int    battleSpeedRandomizerMaximum = 200;
    public static final Random randomizer                   = new Random();
}
