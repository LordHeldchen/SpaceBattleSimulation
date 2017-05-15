package universe;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ships.Fleet;
import ships.blueprints.Blueprint;
import ships.blueprints.NotEnoughtSlotsException;
import ships.blueprints.StandardConfigurationResourceLoader;

public class StarSystem {
    private static final int participatingEmpireA = 1;
    private static final int participatingEmpireB = 2;
    private static final int participatingEmpireC = 3;
    private static final int participatingEmpireD = 4;

    private static Blueprint standardFighterBlueprint;
    private static Blueprint interceptorBlueprint;
    private static Blueprint heavyFighterBlueprint;
    private static Blueprint bomberBlueprint;
    private static Blueprint hunterBlueprint;
    private static Blueprint lancerBlueprint;
    private static Blueprint assFrigBlueprint;
    private static Blueprint pierceFrigateBP;
    private static Blueprint defenderBlueprint;
    private static Blueprint combatDestroyerBlueprint;
    private static Blueprint lightFleetCruiserBP;
    private static Blueprint supportArtilleryCruiserBP;
    private static Blueprint combatBattleCruiserBP;
    private static Blueprint stunBattlecruiserBP;
    private static Blueprint fireSupprtHeavyCruiserBP;
    private static Blueprint brawlerBattleshipBP;
    private static Blueprint screenBattleshipBP;
    private static Blueprint fireSupportDreadnoughtBP;
    private static Blueprint artilleryDreadnoughtBP;
    private static Blueprint mixedCarrierBP;
    private static Blueprint siegeTitanBP;

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

        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, standardFighterBlueprint, 500);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, heavyFighterBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, lancerBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, lightFleetCruiserBP, 20);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, supportArtilleryCruiserBP, 10);
        addNumOfShipsFromBlueprintToFleet(fleetA, participatingEmpireA, fireSupportDreadnoughtBP, 10);

        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, bomberBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, interceptorBlueprint, 200);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, hunterBlueprint, 200);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, pierceFrigateBP, 100);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, defenderBlueprint, 50);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, combatDestroyerBlueprint, 30);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, combatBattleCruiserBP, 10);
        addNumOfShipsFromBlueprintToFleet(fleetB, participatingEmpireB, artilleryDreadnoughtBP, 5);


        addNumOfShipsFromBlueprintToFleet(fleetC, participatingEmpireC, mixedCarrierBP, 3);
        addNumOfShipsFromBlueprintToFleet(fleetC, participatingEmpireC, assFrigBlueprint, 100);
        addNumOfShipsFromBlueprintToFleet(fleetC, participatingEmpireC, fireSupprtHeavyCruiserBP, 10);
        addNumOfShipsFromBlueprintToFleet(fleetC, participatingEmpireC, siegeTitanBP, 1);

        addNumOfShipsFromBlueprintToFleet(fleetD, participatingEmpireD, screenBattleshipBP, 10);
        addNumOfShipsFromBlueprintToFleet(fleetD, participatingEmpireD, brawlerBattleshipBP, 10);
        addNumOfShipsFromBlueprintToFleet(fleetD, participatingEmpireD, stunBattlecruiserBP, 50);

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        listOfFleets.add(fleetC);
        listOfFleets.add(fleetD);

        return listOfFleets;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem() throws InstantiationException, NotEnoughtSlotsException, IOException {
        standardFighterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("sfighter");
        interceptorBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("interceptor");
        heavyFighterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("gboat");
        bomberBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("pbomb");
        hunterBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("hunter");
        lancerBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("lbomb");
        assFrigBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("assfrig");
        pierceFrigateBP = StandardConfigurationResourceLoader.getStandardConfiguration("prcfrig");
        defenderBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("defender");
        combatDestroyerBlueprint = StandardConfigurationResourceLoader.getStandardConfiguration("cdest");
        lightFleetCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("lfcrui");
        supportArtilleryCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("sacruiser");
        combatBattleCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("cbcruiser");
        stunBattlecruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("stbcruiser");
        fireSupprtHeavyCruiserBP = StandardConfigurationResourceLoader.getStandardConfiguration("fshcruiser");
        brawlerBattleshipBP = StandardConfigurationResourceLoader.getStandardConfiguration("brbship");
        screenBattleshipBP = StandardConfigurationResourceLoader.getStandardConfiguration("scbship");
        fireSupportDreadnoughtBP = StandardConfigurationResourceLoader.getStandardConfiguration("fsdnought");
        artilleryDreadnoughtBP = StandardConfigurationResourceLoader.getStandardConfiguration("ardnought");
        mixedCarrierBP = StandardConfigurationResourceLoader.getStandardConfiguration("mixcarrier");
        siegeTitanBP = StandardConfigurationResourceLoader.getStandardConfiguration("stitan");

        return conductMultiwayBattle();
    }
}
