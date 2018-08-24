package de.rauwolf.gaming.battleships.gui.blueprintToText;

import java.util.List;
import java.util.Map;

import de.rauwolf.gaming.battleships.battle.BattleConstants;
import de.rauwolf.gaming.battleships.ships.blueprints.Blueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.components.ComponentBlueprint;
import de.rauwolf.gaming.battleships.ships.shipHulls.DamageType;
import de.rauwolf.gaming.battleships.ships.stats.StatType;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponBlueprint;
import de.rauwolf.gaming.battleships.ships.weapons.WeaponSecondaryEffect;

public class BlueprintToDetailText implements BlueprintToText {

    private ShipBlueprint ship;

    @Override
    public void setCurrentShipBlueprint(ShipBlueprint ship) {
        this.ship = ship;
    }

    @Override
    public String toText(Blueprint blueprint) {
        if (blueprint instanceof WeaponBlueprint) {
            return this.toText((WeaponBlueprint) blueprint);
        }
        if (blueprint instanceof ComponentBlueprint) {
            return this.toText((ComponentBlueprint) blueprint);
        }
        return "";
    }

    private String toText(final WeaponBlueprint weaponBlueprint) {
        DamageType dmgType = weaponBlueprint.getDmgType();
        List<SizeClass> preferredTargets = weaponBlueprint.getPreferredTargets();
        Map<WeaponSecondaryEffect, List<Integer>> secondaryEffects = weaponBlueprint.getSecondaryEffects();

        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(weaponBlueprint.getName() + " (Dmg Type: " + dmgType + ")\n");
        contentBuilder.append("Size class: " + weaponBlueprint.getSize() + "\n");
        contentBuilder.append("Cost: " + weaponBlueprint.getValue() + "\n\n");

        StatValueProvider svp = (StatType stat) -> weaponBlueprint.getStatFor(stat);
        writeStatValues(contentBuilder, svp, false);

        return contentBuilder.toString();
    }

    private String toText(ComponentBlueprint componentBlueprint) {
        StringBuilder contentBuilder = new StringBuilder();
        contentBuilder.append(componentBlueprint.getName() + "\n");
        contentBuilder.append("Type: " + componentBlueprint.getType() + "\n");
        contentBuilder.append("Cost: " + componentBlueprint.getValue(ship.getSize()) + "\n\n");

        StatValueProvider svp = (StatType stat) -> {
            int flatBonus = componentBlueprint.getFlatBonusFor(stat);
            if (stat.doesScaleWithSize()) {
                flatBonus *= BattleConstants.shipSizeScaling.get(ship.getSize());
            }
            return flatBonus;
        };
        writeStatValues(contentBuilder, svp, false);

        svp = (StatType stat) -> componentBlueprint.getFactorFor(stat);
        writeStatValues(contentBuilder, svp, true);

        return contentBuilder.toString();
    }

    @FunctionalInterface
    private interface StatValueProvider {
        public Number get(StatType stat);
    }

    private static final double THRESHOLD_RANGE = 0.00001d;

    private boolean isZero(double value) {
        return value >= -THRESHOLD_RANGE && value <= THRESHOLD_RANGE;
    }

    private void writeStatValues(StringBuilder contentBuilder, StatValueProvider valueProdiver, boolean isFactor) {
        int contentCount = 0;

        for (StatType stat : StatType.values()) {
            double value = valueProdiver.get(stat).doubleValue() - (isFactor ? 1 : 0);

            if (!isZero(value)) {
                if (isFactor) {
                    value *= 100;
                }
                long roundedValue = Math.round(value);
                String text = String.valueOf(roundedValue);
                text = text.indexOf(".") < 0 ? text : text.replaceAll("0*$", "").replaceAll("\\.$", "");

                if (isFactor) {
                    text += "%";
                    if (roundedValue > 0) {
                        text = "+" + text;
                    }
                }

                if (contentCount > 0) {
                    contentBuilder.append("\n");
                }
                contentCount++;
                switch (stat.getNumberType()) {
                    case INTEGER:
                        contentBuilder.append("# " + stat + ": " + text);
                        break;
                    case PERCENT:
                        contentBuilder.append("# " + stat + ": " + text + "%");
                        break;
                }
            }
        }
    }
}
