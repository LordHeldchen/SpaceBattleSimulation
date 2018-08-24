package de.rauwolf.gaming.battleships.gui.blueprintToText;

import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponBlueprint;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class BlueprintToDetailText implements BlueprintToText {

    @Override
    public String toText(Blueprint blueprint) {
        if (blueprint instanceof WeaponBlueprint) {
            return this.toText((WeaponBlueprint) blueprint);
        }
        return "";
    }

    private String toText(WeaponBlueprint weaponBlueprint) {
        DamageType dmgType = weaponBlueprint.getDmgType();
        List<SizeClass> preferredTargets = weaponBlueprint.getPreferredTargets();
        Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects = weaponBlueprint.getSecondaryEffects();

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(weaponBlueprint.getName() + "\n");
        contentBuilder.append("Size class: " + weaponBlueprint.getSize() + "\n");
        contentBuilder.append("Cost: " + weaponBlueprint.getValue() + "\n\n");

        int contentCount = 0;
        for (StatType stat : StatType.values()) {
            int value = weaponBlueprint.getStatFor(stat);
            if (value > 0) {
                if (contentCount > 0) {
                    contentBuilder.append("\n");
                }
                contentCount++;
                switch (stat.getNumberType()) {
                    case INTEGER:
                        contentBuilder.append("# " + stat + ": " + value);
                        break;
                    case PERCENT:
                        contentBuilder.append("# " + stat + ": " + value + "%");
                        break;
                }
            }
        }
        return contentBuilder.toString();
    }
}
