package universe;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import ships.Fleet;
import ships.blueprints.ShipBlueprint;
import ships.blueprints.NotEnoughtSlotsException;
import ships.blueprints.StandardConfigurationResourceLoader;

public class StarSystem {
    private static final int participatingEmpireA = 1;
    private static final int participatingEmpireB = 2;
    private static final int participatingEmpireC = 3;
    private static final int participatingEmpireD = 4;

    private static ShipBlueprint standardFighterBlueprint;
    private static ShipBlueprint interceptorBlueprint;
    private static ShipBlueprint heavyFighterBlueprint;
    private static ShipBlueprint bomberBlueprint;
    private static ShipBlueprint hunterBlueprint;
    private static ShipBlueprint lancerBlueprint;
    private static ShipBlueprint assFrigBlueprint;
    private static ShipBlueprint piercerFrigateBP;
    private static ShipBlueprint defenderBlueprint;
    private static ShipBlueprint combatDestroyerBlueprint;
    private static ShipBlueprint lightFleetCruiserBP;
    private static ShipBlueprint supportArtilleryCruiserBP;
    private static ShipBlueprint combatBattleCruiserBP;
    private static ShipBlueprint stunBattlecruiserBP;
    private static ShipBlueprint fireSupprtHeavyCruiserBP;
    private static ShipBlueprint brawlerBattleshipBP;
    private static ShipBlueprint screenBattleshipBP;
    private static ShipBlueprint fireSupportDreadnoughtBP;
    private static ShipBlueprint artilleryDreadnoughtBP;
    private static ShipBlueprint mixedCarrierBP;
    private static ShipBlueprint siegeTitanBP;

    private static Set<Fleet> prepareMultiwayBattle() {
        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet(participatingEmpireA);
        Fleet fleetB = new Fleet(participatingEmpireB);
        Fleet fleetC = new Fleet(participatingEmpireC);
        Fleet fleetD = new Fleet(participatingEmpireD);

        fleetA.add(standardFighterBlueprint, 500);
        fleetA.add(heavyFighterBlueprint, 100);
        fleetA.add(lancerBlueprint, 100);
        fleetA.add(lightFleetCruiserBP, 20);
        fleetA.add(supportArtilleryCruiserBP, 10);
        fleetA.add(fireSupportDreadnoughtBP, 10);

        fleetB.add(bomberBlueprint, 100);
        fleetB.add(interceptorBlueprint, 200);
        fleetB.add(hunterBlueprint, 200);
        fleetB.add(piercerFrigateBP, 50);
        fleetB.add(defenderBlueprint, 50);
        fleetB.add(combatDestroyerBlueprint, 30);
        fleetB.add(combatBattleCruiserBP, 10);
        fleetB.add(artilleryDreadnoughtBP, 5);

        fleetC.add(mixedCarrierBP, 3);
        fleetC.add(assFrigBlueprint, 100);
        fleetC.add(fireSupprtHeavyCruiserBP, 10);
        fleetC.add(siegeTitanBP, 1);

        fleetD.add(screenBattleshipBP, 10);
        fleetD.add(brawlerBattleshipBP, 10);
        fleetD.add(stunBattlecruiserBP, 50);

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
        piercerFrigateBP = StandardConfigurationResourceLoader.getStandardConfiguration("prcfrig");
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

        return prepareMultiwayBattle();
    }
}
