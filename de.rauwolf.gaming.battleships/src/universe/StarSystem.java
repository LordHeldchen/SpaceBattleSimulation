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
        fleetA.add(heavyFighterBlueprint, 300);
        fleetA.add(lancerBlueprint, 100);
        fleetA.add(lightFleetCruiserBP, 50);
        fleetA.add(supportArtilleryCruiserBP, 20);
        fleetA.add(fireSupportDreadnoughtBP, 20);

        fleetB.add(bomberBlueprint, 100);
        fleetB.add(interceptorBlueprint, 1000);
        fleetB.add(hunterBlueprint, 200);
        fleetB.add(piercerFrigateBP, 50);
        fleetB.add(defenderBlueprint, 50);
        fleetB.add(combatDestroyerBlueprint, 30);
        fleetB.add(combatBattleCruiserBP, 30);
        fleetB.add(artilleryDreadnoughtBP, 5);

        fleetC.add(mixedCarrierBP, 3);
        fleetC.add(assFrigBlueprint, 300);
        fleetC.add(fireSupprtHeavyCruiserBP, 30);
        fleetC.add(siegeTitanBP, 1);

        fleetD.add(screenBattleshipBP, 20);
        fleetD.add(brawlerBattleshipBP, 20);
        fleetD.add(stunBattlecruiserBP, 50);

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        listOfFleets.add(fleetC);
        listOfFleets.add(fleetD);

        return listOfFleets;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem() throws InstantiationException, NotEnoughtSlotsException, IOException {
        standardFighterBlueprint = StandardConfigurationResourceLoader.getBlueprint("XXS - sfighter");
        interceptorBlueprint = StandardConfigurationResourceLoader.getBlueprint("interceptor");
        heavyFighterBlueprint = StandardConfigurationResourceLoader.getBlueprint("gboat");
        bomberBlueprint = StandardConfigurationResourceLoader.getBlueprint("pbomb");
        hunterBlueprint = StandardConfigurationResourceLoader.getBlueprint("hunter");
        lancerBlueprint = StandardConfigurationResourceLoader.getBlueprint("lbomb");
        assFrigBlueprint = StandardConfigurationResourceLoader.getBlueprint("assfrig");
        piercerFrigateBP = StandardConfigurationResourceLoader.getBlueprint("prcfrig");
        defenderBlueprint = StandardConfigurationResourceLoader.getBlueprint("defender");
        combatDestroyerBlueprint = StandardConfigurationResourceLoader.getBlueprint("cdest");
        lightFleetCruiserBP = StandardConfigurationResourceLoader.getBlueprint("lfcrui");
        supportArtilleryCruiserBP = StandardConfigurationResourceLoader.getBlueprint("sacruiser");
        combatBattleCruiserBP = StandardConfigurationResourceLoader.getBlueprint("cbcruiser");
        stunBattlecruiserBP = StandardConfigurationResourceLoader.getBlueprint("stbcruiser");
        fireSupprtHeavyCruiserBP = StandardConfigurationResourceLoader.getBlueprint("fshcruiser");
        brawlerBattleshipBP = StandardConfigurationResourceLoader.getBlueprint("brbship");
        screenBattleshipBP = StandardConfigurationResourceLoader.getBlueprint("scbship");
        fireSupportDreadnoughtBP = StandardConfigurationResourceLoader.getBlueprint("fsdnought");
        artilleryDreadnoughtBP = StandardConfigurationResourceLoader.getBlueprint("ardnought");
        mixedCarrierBP = StandardConfigurationResourceLoader.getBlueprint("mixcarrier");
        siegeTitanBP = StandardConfigurationResourceLoader.getBlueprint("stitan");

        return prepareMultiwayBattle();
    }
}
