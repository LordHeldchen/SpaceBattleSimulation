package de.rauwolf.gaming.battleships.universe;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import de.rauwolf.gaming.battleships.characters.Admiral;
import de.rauwolf.gaming.battleships.ships.Fleet;
import de.rauwolf.gaming.battleships.ships.blueprints.NotEnoughtSlotsException;
import de.rauwolf.gaming.battleships.ships.blueprints.ShipBlueprint;
import de.rauwolf.gaming.battleships.ships.blueprints.SizeClass;
import de.rauwolf.gaming.battleships.ships.blueprints.StandardConfigurationResourceLoader;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongSizeForSlotException;
import de.rauwolf.gaming.battleships.ships.blueprints.exceptions.WrongTypeForSlotException;
import de.rauwolf.gaming.battleships.ships.stats.StatType;

public class StarSystem {
    private static final int     participatingEmpireA = 1;
    private static final int     participatingEmpireB = 2;
    private static final int     participatingEmpireC = 3;
    private static final int     participatingEmpireD = 4;

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

        fleetA.add(standardFighterBlueprint, 2000);
        fleetA.add(heavyFighterBlueprint, 1200);
        fleetA.add(lancerBlueprint, 400);
        fleetA.add(fleetCruiserBP, 200);

        fleetB.add(bomberBlueprint, 800);
        fleetB.add(interceptorBlueprint, 400);
        fleetB.add(hunterBlueprint, 400);
        fleetB.add(piercerFrigateBP, 200);
        fleetB.add(defenderBlueprint, 200);
        fleetB.add(combatDestroyerBlueprint, 200);

        fleetC.add(assFrigBlueprint, 1600);

        fleetD.add(screenBattleshipBP, 80);
        fleetD.add(brawlerBattleshipBP, 80);

        listOfFleets.add(fleetA);
        listOfFleets.add(fleetB);
        listOfFleets.add(fleetC);
        listOfFleets.add(fleetD);

        return listOfFleets;
    }

    public static Set<Fleet> getAllFleetsFromStarSystem()
            throws InstantiationException, NotEnoughtSlotsException, IOException, NumberFormatException, WrongSizeForSlotException, WrongTypeForSlotException {

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
