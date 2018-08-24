package de.rauwolf.gaming.battleships.battle;

import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class ShotBuffer {
    private static int    BUFFER_SIZE = 4;

    private static Shot[] shotBuffer  = new Shot[BUFFER_SIZE];
    private static int    readMarker  = 0;
    private static int    writeMarker = 1;

    static {
        for (int i = 0; i < BUFFER_SIZE; i++) {
            shotBuffer[i] = new Shot();
        }
    }

    public static boolean writeShot(int amount, //
            int armorPenetration, //
            int accuracy, //
            DamageType damageType, //
            Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects, //
            String weaponName) {

        shotBuffer[writeMarker].amount = amount;
        shotBuffer[writeMarker].armorPenetration = armorPenetration;
        shotBuffer[writeMarker].accuracy = accuracy;
        shotBuffer[writeMarker].damageType = damageType;
        shotBuffer[writeMarker].secondaryEffects = secondaryEffects;
        shotBuffer[writeMarker].weaponName = weaponName;

        shotBuffer[writeMarker].hullDamageLevel = HullDamageLevel.NOT_DETERMINED;
        shotBuffer[writeMarker].hitStrength = 0;
        shotBuffer[writeMarker].specificResistanceOfTarget = 0;

        writeMarker = (writeMarker + 1) % BUFFER_SIZE;

        return writeMarker != readMarker;
    }

    public static Shot readShot() {
        int newReadMarker = (readMarker + 1) % BUFFER_SIZE;
        if (newReadMarker != writeMarker) {
            readMarker = newReadMarker;
            return shotBuffer[readMarker];
        } else {
            return null;
        }
    }
}
