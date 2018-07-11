package main.java.universe;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import main.java.characters.Admiral;
import main.java.ships.Fleet;
import main.java.ships.blueprints.NotEnoughtSlotsException;
import main.java.ships.blueprints.ShipBlueprint;
import main.java.ships.blueprints.SizeClass;
import main.java.ships.blueprints.StandardConfigurationResourceLoader;
import main.java.ships.stats.StatType;

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
    private static ShipBlueprint fleetCruiserBP;
    private static ShipBlueprint brawlerBattleshipBP;
    private static ShipBlueprint screenBattleshipBP;

    private static Set<Fleet> prepareMultiwayBattle() {
        Set<Fleet> listOfFleets = new HashSet<Fleet>();

        Fleet fleetA = new Fleet(participatingEmpireA);
        Fleet fleetB = new Fleet(participatingEmpireB);
        Fleet fleetC = new Fleet(participatingEmpireC);
        Fleet fleetD = new Fleet(participatingEmpireD);
        
        Admiral admiralA = new Admiral("Colonel Baker");
        admiralA.setFlatBonusAmountOfTypeForSize(SizeClass.XXS, StatType.MANEUVERABILITY, 10);
        fleetA.setAdmiral(admiralA);

        fleetA.add(standardFighterBlueprint, 5000);
        fleetA.add(heavyFighterBlueprint, 3000);
        fleetA.add(lancerBlueprint, 1000);
        fleetA.add(fleetCruiserBP, 500);

        fleetB.add(bomberBlueprint, 2000);
        fleetB.add(interceptorBlueprint, 1000);
        fleetB.add(hunterBlueprint, 1000);
        fleetB.add(piercerFrigateBP, 500);
        fleetB.add(defenderBlueprint, 500);
        fleetB.add(combatDestroyerBlueprint, 500);

        fleetC.add(assFrigBlueprint, 4000);

        fleetD.add(screenBattleshipBP, 200);
        fleetD.add(brawlerBattleshipBP, 200);

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        listOfFleets.add(fleetC);
        listOfFleets.add(fleetD);

        return listOfFleets;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem() throws InstantiationException, NotEnoughtSlotsException, IOException {
        standardFighterBlueprint = StandardConfigurationResourceLoader.getBlueprint("sfighter");
        interceptorBlueprint = StandardConfigurationResourceLoader.getBlueprint("interceptor");
        heavyFighterBlueprint = StandardConfigurationResourceLoader.getBlueprint("gboat");
        bomberBlueprint = StandardConfigurationResourceLoader.getBlueprint("pbomb");
        hunterBlueprint = StandardConfigurationResourceLoader.getBlueprint("hunter");
        lancerBlueprint = StandardConfigurationResourceLoader.getBlueprint("lbomb");
        assFrigBlueprint = StandardConfigurationResourceLoader.getBlueprint("assfrig");
        piercerFrigateBP = StandardConfigurationResourceLoader.getBlueprint("prcfrig");
        defenderBlueprint = StandardConfigurationResourceLoader.getBlueprint("defender");
        combatDestroyerBlueprint = StandardConfigurationResourceLoader.getBlueprint("cdest");
        fleetCruiserBP = StandardConfigurationResourceLoader.getBlueprint("fcrui");
        brawlerBattleshipBP = StandardConfigurationResourceLoader.getBlueprint("brbship");
        screenBattleshipBP = StandardConfigurationResourceLoader.getBlueprint("scbship");

        return prepareMultiwayBattle();
    }
}
