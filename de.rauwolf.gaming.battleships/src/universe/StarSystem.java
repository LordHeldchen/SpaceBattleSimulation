package universe;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.NotEnoughtSlotsException;
import ships.resourceLoader.StandardConfigurationResourceLoader;

public class StarSystem {
    private static final int participatingEmpireA = 1;
    private static final int participatingEmpireB = 2;
    private static final int participatingEmpireC = 3;
    private static final int participatingEmpireD = 4;
    
    private static void addNumOfShipsFromBlueprintToFleet(Fleet fleet, int owningEmpire, Blueprint blueprint, int num) {
        for (int i = 0; i < num; i++) {
            fleet.add(blueprint.createInstance(owningEmpire));
        }
    }
    
    public static Set<Fleet> getAllFleetsFromStarSystem() throws InstantiationException, NotEnoughtSlotsException, IOException {
        Blueprint standardFighterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("sfighter");
        Blueprint interceptorBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("interceptor");
        Blueprint heavyFighterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("gboat");
        Blueprint hunterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("hunter");
        Blueprint lancerBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("lbomb");
        Blueprint assFrigBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("assfrig");
        Blueprint defenderBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("defender");
        Blueprint combatDestroyerBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("cdest");
        Blueprint lightFleetCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("lfcrui");
        Blueprint closeCombatBattleshipBP = StandardConfigurationResourceLoader.getStandardConfiguration("brbship");

        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet();
        Fleet fleetB = new Fleet();
        Fleet fleetC = new Fleet();
        Fleet fleetD = new Fleet();
        
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, standardFighterBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, hunterBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, lancerBlueprint, 50);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, lightFleetCruiserBP, 10);
        
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, heavyFighterBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, defenderBlueprint, 20);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, combatDestroyerBlueprint, 20);
        
        addNumOfShipsFromBlueprintToFleet(fleetC, participatingEmpireC, assFrigBlueprint, 100);
        
        addNumOfShipsFromBlueprintToFleet(fleetD, participatingEmpireD, closeCombatBattleshipBP, 3);

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        listOfFleets.add(fleetC);
        listOfFleets.add(fleetD);
        return listOfFleets;
    }
}
