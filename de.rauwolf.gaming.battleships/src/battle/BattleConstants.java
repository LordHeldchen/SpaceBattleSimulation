package battle;

import java.text.DecimalFormat;
import java.util.Random;

public class BattleConstants {

    public static final Float[][] armorMatrix                  = new Float[20][20];
    public static final int       evasionRandomizerMaximum     = 100;
    public static final int       cloakingRandomizerMaximum    = 100;
    public static final int       evasionEqualizer             = 60;

    public static final int       battleSpeedRandomizerMaximum = 200;
    public static final Random    randomizer                   = new Random();

    static {
        for (int armorStrength = 0; armorStrength < 20; armorStrength++) {
            for (int weaponStrength = 0; weaponStrength < 20; weaponStrength++) {
                armorMatrix[armorStrength][weaponStrength] = weaponStrength >= armorStrength ? (float) 1
                                : (((float) weaponStrength + 10)) / ((float) armorStrength + 11);
            }
        }
    }

    public static void main(String[] args) {
        for (int armorStrength = 0; armorStrength < 20; armorStrength++) {
            System.out.print("Armor " + (armorStrength + 1) + ": ");
            if (armorStrength + 1 < 10) {
                System.out.print(" ");
            }
            for (int weaponStrength = 0; weaponStrength < 20; weaponStrength++) {
                String formattedNumber = new DecimalFormat("#.##").format(armorMatrix[armorStrength][weaponStrength]);
                String padding = "";
                for (int i = 0; i < 4 - formattedNumber.length(); i++) {
                    padding += " ";
                }
                System.out.print(formattedNumber + padding + " ");
            }
            System.out.println();
        }
    }
}
