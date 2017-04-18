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

    private static Blueprint standardFighterBlueprint;
    private static Blueprint interceptorBlueprint;
    private static Blueprint heavyFighterBlueprint;
    private static Blueprint hunterBlueprint;
    private static Blueprint lancerBlueprint;
    private static Blueprint assFrigBlueprint;
    private static Blueprint defenderBlueprint;
    private static Blueprint combatDestroyerBlueprint;
    private static Blueprint supportArtilleryCruiserBP;
    private static Blueprint lightFleetCruiserBP;
    private static Blueprint closeCombatBattleshipBP;

    private static void addNumOfShipsFromBlueprintToFleet(Fleet fleet, int owningEmpire, Blueprint blueprint, int num) {
        for (int i = 0; i < num; i++) {
            fleet.add(blueprint.createInstance(owningEmpire));
        }
    }

    private static Set<Fleet> conductMultiwayBattle() {
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

    private static Set<Fleet> testBattleships() {
        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet();
        Fleet fleetB = new Fleet();

        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireD, supportArtilleryCruiserBP, 20);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireA, closeCombatBattleshipBP, 10);

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);

        return listOfFleets;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem() throws InstantiationException, NotEnoughtSlotsException, IOException {
//        standardFighterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("sfighter");
//        interceptorBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("interceptor");
//        heavyFighterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("gboat");
//        hunterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("hunter");
//        lancerBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("lbomb");
//        assFrigBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("assfrig");
//        defenderBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("defender");
//        combatDestroyerBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("cdest");
//        lightFleetCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("lfcrui");
        supportArtilleryCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("sacruiser");
        closeCombatBattleshipBP = StandardConfigurationResourceLoader.getStandardConfiguration("brbship");

        return testBattleships();
    }
}
