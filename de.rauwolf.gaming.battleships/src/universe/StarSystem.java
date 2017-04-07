package universe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
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
        fighterType.setAvailableWeaponSlotsForSize(SizeEnum.XXS, 1);
        fighterType.setAvailableComponentSlotsForSize(SizeEnum.XXS, 1);
        return fighterType;
    }
    private static HullType getHeavyFighterHullType() {
    	int baseGlanceThreshold = 10;
        int baseHitThreshold = 40;
        int baseCritThreshold = 60;
        int baseHullStrength = 180;
        int baseStartInitiative = 90;
        
        HullType fighterType = new HullType(baseGlanceThreshold, baseHitThreshold, baseCritThreshold, baseHullStrength, baseStartInitiative);
        fighterType.setAvailableWeaponSlotsForSize(SizeEnum.XXS, 2);
        fighterType.setAvailableComponentSlotsForSize(SizeEnum.XXS, 1);
        return fighterType;
    }

    
    private static Map<String, WeaponBlueprint> loadStandardWeaponBlueprints() {
    	URL weaponBlueprintsResource = StarSystem.class.getClassLoader().getResource("weaponBlueprints");
    	Map<String, WeaponBlueprint> weaponBlueprints = new HashMap<String, WeaponBlueprint>();
        try {
			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(weaponBlueprintsResource.openStream()));
			String line;
			while ((line = bufferedReader.readLine()) != null) {
				if (line.startsWith("#")) {
					continue;
				}
				String[] elements = line.split(";");
				String shorthand = elements[0].trim();
				String name = elements[1].trim();
				SizeEnum size = SizeEnum.valueOf(elements[6].trim());
				int accuracy = new Integer(elements[2].trim()).intValue();
				int damage = new Integer(elements[3].trim()).intValue();
				int armorPenetration = new Integer(elements[4].trim()).intValue();
				int timeCost = new Integer(elements[5].trim()).intValue();
				weaponBlueprints.put(shorthand, new WeaponBlueprint(name, size, accuracy, damage, armorPenetration, timeCost));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
        return weaponBlueprints;
    }
    
    public static Set<Fleet> getAllFleetsFromStarSystem() {
    	HullType fighterType = getBasicFighterHullType();
        Blueprint standardFighterBlueprint = new Blueprint("Standard Fighter", "Some Description", fighterType);
        HullType heavyFighterType = getHeavyFighterHullType();
        Blueprint heavyFighterBlueprint = new Blueprint("Heavy Fighter", "More Description", heavyFighterType);
        
        Map<String, WeaponBlueprint> standardWeaponBlueprints = loadStandardWeaponBlueprints();
        try {
			standardFighterBlueprint.addWeapon(standardWeaponBlueprints.get("vulc"));
			heavyFighterBlueprint.addWeapon(standardWeaponBlueprints.get("vulc"));
			heavyFighterBlueprint.addWeapon(standardWeaponBlueprints.get("vulc"));
		} catch (NotEnoughtSlotsException e) {
			e.printStackTrace();
		}

        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet();

        for (int i = 0; i < 500; i++) {
            fleetA.add(new ShipInstance(participatingEmpireA, standardFighterBlueprint));
        }

        Fleet fleetB = new Fleet();
        for (int i = 0; i < 250; i++) {
            fleetB.add(new ShipInstance(participatingEmpireB, heavyFighterBlueprint));
        }

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        return listOfFleets;
    }
}
