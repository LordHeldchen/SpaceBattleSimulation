package universe;

import java.util.HashSet;
import java.util.Set;

import battle.ShipInstance;
import ships.Fleet;
import ships.basicShips.HullType;
import ships.basicShips.SizeEnum;
import ships.blueprints.Blueprint;
import ships.blueprints.NotEnoughtSlotsException;
import ships.blueprints.WeaponBlueprint;

public class StarSystem {
    private static final int participatingEmpireA = 1;
    private static final int participatingEmpireB = 2;
    
    private static HullType getBasicFighterHullType() {
    	int baseGlanceThreshold = 5;
        int baseHitThreshold = 30;
        int baseCritThreshold = 50;
        int baseHullStrength = 100;
        int baseStartInitiative = 100;
        
        HullType fighterType = new HullType(baseGlanceThreshold, baseHitThreshold, baseCritThreshold, baseHullStrength, baseStartInitiative);
        fighterType.setAvailableWeaponSlotsForSize(SizeEnum.XXS, 2);
        fighterType.setAvailableComponentSlotsForSize(SizeEnum.XXS, 1);
        return fighterType;
    }
    
    private static WeaponBlueprint getBasicVulcanBlueprint() {
    	String weaponName = "'Vulcan' Close Combat Laser Guns";
        int accuracy = 75;
        int damage = 20;
        int ap = 10;
        int timeCost = 4;
        SizeEnum size = SizeEnum.XXS;
        return new WeaponBlueprint(weaponName, size, accuracy, damage, ap, timeCost);
    }

    private static Blueprint getStandardFighterBlueprint() {
    	HullType fighterType = getBasicFighterHullType();

        Blueprint fighterBlueprint = new Blueprint("Standard Fighter", "SomeDescription", fighterType);
        
        try {
			fighterBlueprint.addWeapon(getBasicVulcanBlueprint());
			fighterBlueprint.addWeapon(getBasicVulcanBlueprint());
		} catch (NotEnoughtSlotsException e) {
			e.printStackTrace();
		}

        return fighterBlueprint;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem() {
        Blueprint fighterBlueprint = getStandardFighterBlueprint();

        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet();

        for (int i = 0; i < 5; i++) {
            fleetA.add(new ShipInstance(participatingEmpireA, fighterBlueprint));
        }

        Fleet fleetB = new Fleet();
        for (int i = 0; i < 10; i++) {
            fleetB.add(new ShipInstance(participatingEmpireB, fighterBlueprint));
        }

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        return listOfFleets;
    }
}
